package com.boyd.demo.orderservice.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * A discount code available for application to an order (PROJ-142).
 */
public class DiscountCode {

    private final String code;
    private final DiscountType type;
    private final BigDecimal value;
    private final Instant expiresAt;
    private final boolean firstTimeOnly;

    public DiscountCode(String code, DiscountType type, BigDecimal value, Instant expiresAt, boolean firstTimeOnly) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.expiresAt = expiresAt;
        this.firstTimeOnly = firstTimeOnly;
    }

    public String getCode() {
        return code;
    }

    public DiscountType getType() {
        return type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isFirstTimeOnly() {
        return firstTimeOnly;
    }
}
