package com.boyd.demo.orderservice.dto;

import com.boyd.demo.orderservice.model.Order;

import java.math.BigDecimal;

/**
 * Outbound representation of an order.
 */
public class OrderResponse {

    private final String id;
    private final String customerId;
    private final BigDecimal subtotal;
    private final String discountCode;
    private final BigDecimal discountAmount;
    private final BigDecimal total;
    private final String status;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.customerId = order.getCustomerId();
        this.subtotal = order.getSubtotal();
        this.discountCode = order.getAppliedDiscountCode();
        this.discountAmount = order.getDiscountAmount();
        this.total = order.getTotal();
        this.status = order.getStatus().name();
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }
}
