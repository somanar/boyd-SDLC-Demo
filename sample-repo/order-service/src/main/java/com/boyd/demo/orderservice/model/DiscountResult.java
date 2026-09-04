package com.boyd.demo.orderservice.model;

import java.math.BigDecimal;

/**
 * Outcome of successfully applying a discount code to a subtotal.
 */
public class DiscountResult {

    private final String code;
    private final BigDecimal discountAmount;
    private final BigDecimal total;

    public DiscountResult(String code, BigDecimal discountAmount, BigDecimal total) {
        this.code = code;
        this.discountAmount = discountAmount;
        this.total = total;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
