package com.boyd.demo.orderservice.service;

import com.boyd.demo.orderservice.dto.CreateOrderRequest;
import com.boyd.demo.orderservice.dto.OrderItemRequest;
import com.boyd.demo.orderservice.exception.OrderNotFoundException;
import com.boyd.demo.orderservice.model.Order;
import com.boyd.demo.orderservice.model.OrderItem;
import com.boyd.demo.orderservice.model.OrderStatus;
import com.boyd.demo.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Core order logic.
 *
 * Demo seeds intentionally left in this class so the SDLC skills have real
 * findings to surface:
 *   - createOrder does not validate empty item lists or non-positive quantities.
 *   - The customer identifier is written to the application log at INFO.
 *   - There is no discount handling yet (that is feature ticket PROJ-142).
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(CreateOrderRequest request) {
        // Demo seed: sensitive customer data logged in plain text.
        log.info("Creating order for customer {} with {} items",
                request.getCustomerId(),
                request.getItems() == null ? 0 : request.getItems().size());

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemRequest itemRequest : request.getItems()) {
            items.add(new OrderItem(
                    itemRequest.getSku(),
                    itemRequest.getQuantity(),
                    itemRequest.getUnitPrice()));
        }

        BigDecimal subtotal = calculateSubtotal(items);

        // Demo seed: no discount applied yet; total simply mirrors subtotal.
        BigDecimal total = subtotal;

        Order order = new Order(
                UUID.randomUUID().toString(),
                request.getCustomerId(),
                items,
                subtotal,
                total,
                OrderStatus.CREATED,
                Instant.now());

        return orderRepository.save(order);
    }

    public Order getOrder(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem item : items) {
            subtotal = subtotal.add(item.lineTotal());
        }
        return subtotal;
    }
}
