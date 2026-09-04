package com.boyd.demo.orderservice.controller;

import com.boyd.demo.orderservice.dto.CreateOrderRequest;
import com.boyd.demo.orderservice.dto.OrderResponse;
import com.boyd.demo.orderservice.model.Order;
import com.boyd.demo.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for orders.
 *
 * Demo seed: getOrder returns any order by id with no check that the caller
 * owns it (an insecure direct object reference for /security-review to catch).
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderResponse(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String id) {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(new OrderResponse(order));
    }
}
