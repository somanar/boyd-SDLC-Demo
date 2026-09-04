package com.boyd.demo.orderservice.service;

import com.boyd.demo.orderservice.dto.CreateOrderRequest;
import com.boyd.demo.orderservice.dto.OrderItemRequest;
import com.boyd.demo.orderservice.model.Order;
import com.boyd.demo.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Existing test coverage is intentionally thin: a single happy-path case and
 * no coverage of empty carts, invalid quantities, or the (not yet built)
 * discount logic. /generate-unit-tests fills these gaps during the demo.
 */
class OrderServiceTest {

    private final OrderRepository orderRepository = new OrderRepository();
    private final OrderService orderService = new OrderService(orderRepository, new DiscountService(orderRepository));

    @Test
    void createsOrderWithComputedSubtotal() {
        OrderItemRequest item = new OrderItemRequest();
        item.setSku("SKU-1");
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("10.00"));

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("cust-1");
        request.setItems(List.of(item));

        Order order = orderService.createOrder(request);

        assertEquals(new BigDecimal("20.00"), order.getSubtotal());
        assertEquals(new BigDecimal("20.00"), order.getTotal());
    }
}
