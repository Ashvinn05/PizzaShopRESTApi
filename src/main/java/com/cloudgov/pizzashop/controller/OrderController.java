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

/**
 * Controller for managing order-related operations in the pizza shop.
 * Provides REST endpoints for creating, retrieving, updating, and managing orders.
 * Uses reactive programming with Project Reactor's Flux and Mono.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    /**
     * Constructor for OrderController.
     * 
     * @param orderService the service layer for order operations
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieves all orders from the database.
     * Returns a reactive stream of orders.
     * 
     * @return Flux of Order objects
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Order> getAllOrders() {
        log.info("[START] getAllOrders - Handling GET request for all orders");
        long startTime = System.currentTimeMillis();
        
        return orderService.getAllOrders()
            .doOnNext(order -> log.debug("Found order: {}", order))
            .doOnError(e -> {
                log.error("[ERROR] getAllOrders - Error fetching orders", e);
                throw new RuntimeException("Failed to fetch orders", e);
            })
            .doOnComplete(() -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getAllOrders - Successfully fetched orders. Duration: {}ms", duration);
            });
    }

    /**
     * Retrieves orders by status.
     * Returns a reactive stream of orders matching the specified status.
     * 
     * @param status the status to filter orders by
     * @return Flux of Order objects
     */
    @GetMapping("/status/{status}")
    public Flux<Order> getOrdersByStatus(@PathVariable String status) {
        log.info("[START] getOrdersByStatus - Handling GET request for orders with status: {}", status);
        long startTime = System.currentTimeMillis();
        
        return orderService.getOrdersByStatus(status)
            .doOnNext(order -> log.debug("Found order: {}", order))
            .doOnError(e -> {
                log.error("[ERROR] getOrdersByStatus - Error fetching orders by status: {}", status, e);
                throw new RuntimeException("Failed to fetch orders by status", e);
            })
            .doOnComplete(() -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getOrdersByStatus - Successfully fetched orders. Duration: {}ms", duration);
            });
    }

    /**
     * Retrieves a specific order by its ID.
     * Returns a Mono containing the order if found, or throws an exception if not found.
     * 
     * @param id the ID of the order to retrieve
     * @return Mono containing the Order object
     */
    @GetMapping("/{id}")
    public Mono<Order> getOrderById(@PathVariable String id) {
        log.info("[START] getOrderById - Handling GET request for order with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return orderService.getOrderById(id)
            .doOnNext(order -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getOrderById - Successfully fetched order. Duration: {}ms", duration);
            })
            .doOnError(e -> {
                log.error("[ERROR] getOrderById - Error fetching order with id: {}", id, e);
                throw new RuntimeException("Failed to fetch order", e);
            });
    }

    /**
     * Creates a new order.
     * Validates the input and returns the created order with a 201 Created status.
     * 
     * @param newOrder the order object to create
     * @return Mono containing the created Order object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> createOrder(@Valid @RequestBody Order newOrder) {
        log.info("[START] createOrder - Handling POST request to create order: {}", newOrder);
        long startTime = System.currentTimeMillis();
        
        return orderService.createOrder(newOrder)
            .doOnNext(order -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] createOrder - Successfully created order. Duration: {}ms", duration);
            })
            .doOnError(e -> {
                log.error("[ERROR] createOrder - Error creating order: {}", newOrder, e);
                throw new RuntimeException("Failed to create order", e);
            });
    }

    /**
     * Updates the status of an existing order.
     * Returns the updated order with the new status.
     * 
     * @param id the ID of the order to update
     * @param status the new status to set
     * @return Mono containing the updated Order object
     */
    @PutMapping("/{id}/status")
    public Mono<Order> updateOrderStatus(@PathVariable String id, @RequestParam String status) {
        log.info("[START] updateOrderStatus - Handling PUT request to update order status for order: {} to {}", id, status);
        long startTime = System.currentTimeMillis();
        
        return orderService.updateOrderStatus(id, status)
            .doOnNext(order -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] updateOrderStatus - Successfully updated order status. Duration: {}ms", duration);
            })
            .doOnError(e -> {
                log.error("[ERROR] updateOrderStatus - Error updating order status: {}", id, e);
                throw new RuntimeException("Failed to update order status", e);
            });
    }

    /**
     * Cancels an order by its ID.
     * Returns a Mono<Void> to indicate successful cancellation.
     * 
     * @param id the ID of the order to cancel
     * @return Mono<Void>
     */
    @DeleteMapping("/{id}")
    public Mono<Void> cancelOrder(@PathVariable String id) {
        log.info("[START] cancelOrder - Handling DELETE request to cancel order with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return orderService.cancelOrder(id)
            .doOnNext(void_ -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] cancelOrder - Successfully canceled order. Duration: {}ms", duration);
            })
            .doOnError(e -> {
                log.error("[ERROR] cancelOrder - Error canceling order: {}", id, e);
                throw new RuntimeException("Failed to cancel order", e);
            });
    }
}