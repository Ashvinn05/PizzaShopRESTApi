package com.cloudgov.pizzashop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cloudgov.pizzashop.service.OrderService;
import com.cloudgov.pizzashop.model.Order;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Order> getAllOrders() {
        log.debug("Handling GET request for all orders");
        return orderService.getAllOrders()
            .doOnNext(order -> log.debug("Found order: {}", order))
            .doOnError(e -> log.error("Error fetching orders: {}", e.getMessage(), e));
    }

    @GetMapping("/status/{status}")
    public Flux<Order> getOrdersByStatus(@PathVariable String status) {
        log.debug("Handling GET request for orders with status: {}", status);
        return orderService.getOrdersByStatus(status)
            .doOnNext(order -> log.debug("Found order: {}", order))
            .doOnError(e -> log.error("Error fetching orders by status: {}", e.getMessage(), e));
    }

    @GetMapping("/{id}")
    public Mono<Order> getOrderById(@PathVariable String id) {
        log.debug("Handling GET request for order with id: {}", id);
        return orderService.getOrderById(id)
            .doOnNext(order -> log.debug("Found order: {}", order))
            .doOnError(e -> log.error("Error fetching order: {}", e.getMessage(), e));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> createOrder(@Valid @RequestBody Order newOrder) {
        log.debug("Handling POST request to create order: {}", newOrder);
        return orderService.createOrder(newOrder)
            .doOnNext(order -> log.debug("Created order: {}", order))
            .doOnError(e -> log.error("Error creating order: {}", e.getMessage(), e));
    }

    @PutMapping("/{id}/status")
    public Mono<Order> updateOrderStatus(@PathVariable String id, @RequestParam String status) {
        log.debug("Handling PUT request to update order status for order: {} to {}", id, status);
        return orderService.updateOrderStatus(id, status)
            .doOnNext(order -> log.debug("Updated order status: {}", order))
            .doOnError(e -> log.error("Error updating order status: {}", e.getMessage(), e));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> cancelOrder(@PathVariable String id) {
        log.debug("Handling DELETE request to cancel order with id: {}", id);
        return orderService.cancelOrder(id)
            .doOnNext(void_ -> log.debug("Canceled order: {}", id))
            .doOnError(e -> log.error("Error canceling order: {}", e.getMessage(), e));
    }
}