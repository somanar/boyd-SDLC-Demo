package com.boyd.demo.orderservice.dto;

import java.util.List;

/**
 * Inbound payload for creating an order.
 *
 * NOTE (demo seed): there is no discount-code field yet. Adding coupon support
 * is the feature ticket (PROJ-142) that the demo carries through every skill.
 */
public class CreateOrderRequest {

    private String customerId;
    private List<OrderItemRequest> items;

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
}
