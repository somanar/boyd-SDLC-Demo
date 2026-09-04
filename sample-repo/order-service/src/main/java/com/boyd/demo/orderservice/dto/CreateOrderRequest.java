package com.boyd.demo.orderservice.dto;

import java.util.List;

/**
 * Inbound payload for creating an order.
 *
 * discountCode is optional (PROJ-142); omitting it preserves the original
 * no-discount behavior.
 */
public class CreateOrderRequest {

    private String customerId;
    private List<OrderItemRequest> items;
    private String discountCode;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }
}
