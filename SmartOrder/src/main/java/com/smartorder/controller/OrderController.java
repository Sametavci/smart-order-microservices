package com.smartorder.controller;

import com.smartorder.model.Order;
import com.smartorder.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        System.out.println("📩 [OrderController] Received POST /orders request");
        return orderService.createOrder(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        System.out.println("📩 [OrderController] Received GET /orders request");
        return orderService.getAllOrder();
    }
}
