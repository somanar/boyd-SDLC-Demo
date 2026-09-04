package com.boyd.demo.orderservice.dto;

import java.math.BigDecimal;

/**
 * Inbound representation of a single line item.
 *
 * NOTE (demo seed): validation annotations are intentionally missing so that
 * /code-review and /security-review have something real to flag.
 */
public class OrderItemRequest {

    private String sku;
    private int quantity;
    private BigDecimal unitPrice;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
