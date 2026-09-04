package com.boyd.demo.orderservice.service;

import com.boyd.demo.orderservice.exception.DiscountNotEligibleException;
import com.boyd.demo.orderservice.exception.ExpiredDiscountCodeException;
import com.boyd.demo.orderservice.exception.InvalidDiscountCodeException;
import com.boyd.demo.orderservice.model.DiscountCode;
import com.boyd.demo.orderservice.model.DiscountResult;
import com.boyd.demo.orderservice.model.DiscountType;
import com.boyd.demo.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Applies discount codes to an order subtotal (PROJ-142).
 *
 * Demo seed: codes are held in a small in-memory table for this ticket's
 * scope; a real code-management store is a separate effort
 * (see demo-run/03-technical-design.md, Open Decision #4). Usage counts do
 * not survive a restart (Open Decision #2).
 *
 * All percentage math goes through calculateDiscountAmount, which always
 * specifies a rounding mode and scale, so a division can never throw
 * ArithmeticException regardless of the discount percentage (including
 * 100%-off) — this is the fix for the class of defect behind INC-207.
 */
@Service
public class DiscountService {

    private static final Logger log = LoggerFactory.getLogger(DiscountService.class);
    private static final int SCALE = 2;

    private final Map<String, DiscountCode> codes = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> usageCounts = new ConcurrentHashMap<>();
    private final OrderRepository orderRepository;

    public DiscountService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        seedCodes();
    }

    private void seedCodes() {
        Instant farFuture = Instant.parse("2030-01-01T00:00:00Z");
        register(new DiscountCode("SAVE10", DiscountType.PERCENT, new BigDecimal("10"), farFuture, false));
        register(new DiscountCode("5OFF", DiscountType.FIXED, new BigDecimal("5.00"), farFuture, false));
        register(new DiscountCode("WELCOME15", DiscountType.PERCENT, new BigDecimal("15"), farFuture, true));
    }

    private void register(DiscountCode code) {
        String key = normalize(code.getCode());
        codes.put(key, code);
        usageCounts.put(key, new AtomicInteger(0));
    }

    /**
     * Validates and applies a discount code to a subtotal.
     *
     * @throws InvalidDiscountCodeException if the code is unknown
     * @throws ExpiredDiscountCodeException if the code has expired
     * @throws DiscountNotEligibleException if the code is restricted to
     *                                      first-time customers and the
     *                                      customer is not first-time
     */
    public DiscountResult apply(String rawCode, BigDecimal subtotal, String customerId) {
        String key = normalize(rawCode);
        DiscountCode code = codes.get(key);
        if (code == null) {
            throw new InvalidDiscountCodeException(rawCode);
        }
        if (code.getExpiresAt() != null && Instant.now().isAfter(code.getExpiresAt())) {
            throw new ExpiredDiscountCodeException(rawCode);
        }
        if (code.isFirstTimeOnly() && orderRepository.existsByCustomerId(customerId)) {
            throw new DiscountNotEligibleException(rawCode);
        }

        BigDecimal discountAmount = calculateDiscountAmount(code, subtotal);
        BigDecimal total = subtotal.subtract(discountAmount);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            discountAmount = subtotal.setScale(SCALE, RoundingMode.HALF_UP);
            total = BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
        } else {
            discountAmount = discountAmount.setScale(SCALE, RoundingMode.HALF_UP);
            total = total.setScale(SCALE, RoundingMode.HALF_UP);
        }

        usageCounts.get(key).incrementAndGet();
        log.info("Applied discount code type={} percentOrFixedValue={}", code.getType(), code.getValue());

        return new DiscountResult(code.getCode(), discountAmount, total);
    }

    public int usageCount(String rawCode) {
        AtomicInteger counter = usageCounts.get(normalize(rawCode));
        return counter == null ? 0 : counter.get();
    }

    private BigDecimal calculateDiscountAmount(DiscountCode code, BigDecimal subtotal) {
        if (code.getType() == DiscountType.FIXED) {
            return code.getValue();
        }
        // PERCENT: value is a whole-number percentage, e.g. 10 means 10%.
        // scale + rounding mode are always specified so this can never throw
        // ArithmeticException, including for a 100%-off code.
        return subtotal
                .multiply(code.getValue())
                .divide(BigDecimal.valueOf(100), SCALE, RoundingMode.HALF_UP);
    }

    private String normalize(String code) {
        return code == null ? "" : code.trim().toUpperCase();
    }
}
