package com.boyd.demo.orderservice.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * An order placed by a customer.
 */
public class Order {

    private final String id;
    private final String customerId;
    private final List<OrderItem> items;
    private final BigDecimal subtotal;
    private final BigDecimal total;
    private final OrderStatus status;
    private final Instant createdAt;

    public Order(String id,
                 String customerId,
                 List<OrderItem> items,
                 BigDecimal subtotal,
                 BigDecimal total,
                 OrderStatus status,
                 Instant createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.subtotal = subtotal;
        this.total = total;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
