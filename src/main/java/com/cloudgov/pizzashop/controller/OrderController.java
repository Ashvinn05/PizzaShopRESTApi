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
import com.cloudgov.pizzashop.model.ApiResponse;
import com.cloudgov.pizzashop.exception.NotFoundException;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;

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
     * Returns a reactive stream of orders wrapped in an ApiResponse.
     * 
     * @return Mono containing ApiResponse with a list of orders
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<List<Order>>> getAllOrders() {
        log.info("[START] getAllOrders - Handling GET request for all orders");
        long startTime = System.currentTimeMillis();
        
        return orderService.getAllOrders()
            .collectList()
            .map(orders -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getAllOrders - Successfully fetched orders. Duration: {}ms", duration);
                return ApiResponse.success(orders);
            })
            .onErrorMap(e -> {
                log.error("[ERROR] getAllOrders - Error fetching orders", e);
                if (e instanceof NotFoundException) {
                    throw (NotFoundException) e;
                }
                throw new RuntimeException("Failed to fetch orders", e);
            });
    }

    /**
     * Retrieves orders by status.
     * Returns a reactive stream of orders matching the specified status.
     * 
     * @param status the status to filter orders by
     * @return Mono containing ApiResponse with a list of orders
     */
    @GetMapping("/status/{status}")
    public Mono<ApiResponse<List<Order>>> getOrdersByStatus(@PathVariable String status) {
        log.info("[START] getOrdersByStatus - Handling GET request for orders with status: {}", status);
        long startTime = System.currentTimeMillis();
        
        return orderService.getOrdersByStatus(status)
            .collectList()
            .map(orders -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getOrdersByStatus - Successfully fetched orders. Duration: {}ms", duration);
                return ApiResponse.success(orders);
            })
            .onErrorMap(e -> {
                log.error("[ERROR] getOrdersByStatus - Error fetching orders by status: {}", status, e);
                if (e instanceof NotFoundException) {
                    throw (NotFoundException) e;
                }
                throw new RuntimeException("Failed to fetch orders by status", e);
            });
    }

    /**
     * Retrieves a specific order by its ID.
     * Returns a Mono containing the order wrapped in an ApiResponse if found.
     * Throws NotFoundException if the order is not found.
     * 
     * @param id the ID of the order to retrieve
     * @return Mono containing ApiResponse with the order
     */
    @GetMapping("/{id}")
    public Mono<ApiResponse<Order>> getOrderById(@PathVariable String id) {
        log.info("[START] getOrderById - Handling GET request for order with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return orderService.getOrderById(id)
            .map(order -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getOrderById - Successfully fetched order. Duration: {}ms", duration);
                return ApiResponse.success(order);
            })
            .onErrorResume(e -> {
                log.error("[ERROR] getOrderById - Error fetching order with id: {}", id, e);
                if (e instanceof NotFoundException) {
                    return Mono.error(e);
                }
                if (e instanceof IllegalArgumentException) {
                    return Mono.error(e);
                }
                return Mono.error(new RuntimeException("Failed to fetch order", e));
            });
    }

    /**
     * Creates a new order.
     * Validates the input and returns the created order wrapped in an ApiResponse.
     * Returns a 201 Created status on success.
     * 
     * @param newOrder the order object to create
     * @return Mono containing ApiResponse with the created order
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponse<Order>> createOrder(@Valid @RequestBody Order newOrder) {
        if (newOrder == null) {
            throw new ServerWebInputException("Request body cannot be empty");
        }
        
        log.info("[START] createOrder - Handling POST request to create order: {}", newOrder);
        long startTime = System.currentTimeMillis();
        
        return orderService.createOrder(newOrder)
            .map(order -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] createOrder - Successfully created order. Duration: {}ms", duration);
                return ApiResponse.success(order, "Order created successfully");
            })
            .onErrorResume(e -> {
                log.error("[ERROR] createOrder - Error creating order: {}", newOrder, e);
                if (e instanceof NotFoundException) {
                    return Mono.error(new NotFoundException("Pizza not found with id: " + e.getMessage()));
                }
                if (e instanceof IllegalArgumentException) {
                    return Mono.error(new IllegalArgumentException("Invalid request: " + e.getMessage()));
                }
                return Mono.error(new RuntimeException("Failed to create order: " + e.getMessage()));
            });
    }

    /**
     * Updates the status of an existing order.
     * Returns the updated order wrapped in an ApiResponse.
     * 
     * @param id the ID of the order to update
     * @param status the new status to set
     * @return Mono containing ApiResponse with the updated order
     */
    @PutMapping("/{id}/status")
    public Mono<ApiResponse<Order>> updateOrderStatus(@PathVariable String id, @RequestParam String status) {
        if (status == null || status.isEmpty()) {
            throw new ServerWebInputException("Status parameter cannot be empty");
        }
        
        log.info("[START] updateOrderStatus - Handling PUT request to update order status for order: {} to {}", id, status);
        long startTime = System.currentTimeMillis();
        
        return orderService.updateOrderStatus(id, status)
            .map(order -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] updateOrderStatus - Successfully updated order status. Duration: {}ms", duration);
                return ApiResponse.success(order, "Order status updated successfully");
            })
            .onErrorResume(e -> {
                log.error("[ERROR] updateOrderStatus - Error updating order status: {}", id, e);
                if (e instanceof NotFoundException) {
                    return Mono.error(e);
                }
                if (e instanceof IllegalArgumentException) {
                    return Mono.error(e);
                }
                return Mono.error(new RuntimeException("Failed to update order status", e));
            });
    }

    /**
     * Cancels an order by its ID.
     * Returns an ApiResponse with a success message.
     * 
     * @param id the ID of the order to cancel
     * @return Mono containing ApiResponse with a success message
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ApiResponse<Void>> cancelOrder(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            throw new ServerWebInputException("ID parameter cannot be empty");
        }
        
        log.info("[START] cancelOrder - Handling DELETE request to cancel order with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return orderService.cancelOrder(id)
            .then(Mono.just(ApiResponse.success((Void)null, "Order cancelled successfully")))
            .doOnNext(response -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] cancelOrder - Successfully cancelled order. Duration: {}ms", duration);
            })
            .onErrorResume(e -> {
                log.error("[ERROR] cancelOrder - Error cancelling order: {}", id, e);
                if (e instanceof NotFoundException) {
                    return Mono.error(e);
                }
                if (e instanceof IllegalArgumentException) {
                    return Mono.error(e);
                }
                return Mono.error(new RuntimeException("Failed to cancel order", e));
            });
    }
}