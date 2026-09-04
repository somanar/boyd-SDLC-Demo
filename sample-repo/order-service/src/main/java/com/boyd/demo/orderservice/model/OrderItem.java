package com.boyd.demo.orderservice.model;

import java.math.BigDecimal;

/**
 * A single line item on an order.
 */
public class OrderItem {

    private final String sku;
    private final int quantity;
    private final BigDecimal unitPrice;

    public OrderItem(String sku, int quantity, BigDecimal unitPrice) {
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
