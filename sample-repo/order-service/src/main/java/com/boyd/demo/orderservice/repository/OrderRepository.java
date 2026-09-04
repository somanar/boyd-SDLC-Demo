package com.boyd.demo.orderservice.repository;

import com.boyd.demo.orderservice.model.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory order store. Good enough for the demo; a real
 * implementation would be backed by a database.
 */
@Repository
public class OrderRepository {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public Order save(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    public boolean existsByCustomerId(String customerId) {
        return orders.values().stream()
                .anyMatch(order -> order.getCustomerId().equals(customerId));
    }
}
