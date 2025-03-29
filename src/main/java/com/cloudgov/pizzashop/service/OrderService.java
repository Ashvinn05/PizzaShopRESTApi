package com.cloudgov.pizzashop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cloudgov.pizzashop.model.Order;
import com.cloudgov.pizzashop.repository.OrderRepository;
import com.cloudgov.pizzashop.exception.NotFoundException;
import com.cloudgov.pizzashop.service.PizzaService;
import java.util.Date;

/**
 * Service class for order-related operations.
 * Provides methods for managing orders including creation, retrieval, status updates, and cancellation.
 */
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PizzaService pizzaService;
    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    /**
     * Constructor for OrderService.
     * 
     * @param orderRepository the order repository
     * @param pizzaService the pizza service
     */
    @Autowired
    public OrderService(OrderRepository orderRepository, PizzaService pizzaService) {
        this.orderRepository = orderRepository;
        this.pizzaService = pizzaService;
    }

    /**
     * Retrieves all orders from the database.
     * 
     * @return a Flux of orders
     */
    public Flux<Order> getAllOrders() {
        log.info("[START] getAllOrders - Fetching all orders from database");
        long startTime = System.currentTimeMillis();
        
        return orderRepository.findAll()
            .doOnNext(order -> log.debug("Found order: {}", order))
            .switchIfEmpty(Flux.empty())
            .doOnError(e -> {
                log.error("[ERROR] getAllOrders - Error fetching orders", e);
                if (e instanceof IllegalArgumentException) {
                    throw new NotFoundException("Invalid order data");
                }
                throw new RuntimeException("Failed to fetch orders", e);
            })
            .doOnComplete(() -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getAllOrders - Successfully fetched orders. Duration: {}ms", duration);
            });
    }

    /**
     * Retrieves orders by their status.
     * 
     * @param status the status to filter orders by
     * @return a Flux of orders with the specified status
     */
    public Flux<Order> getOrdersByStatus(String status) {
        log.info("[START] getOrdersByStatus - Fetching orders with status: {}", status);
        long startTime = System.currentTimeMillis();
        
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }
        
        return orderRepository.findByStatus(status)
            .doOnNext(order -> log.debug("Found order: {}", order))
            .switchIfEmpty(Flux.empty())
            .doOnError(e -> {
                log.error("[ERROR] getOrdersByStatus - Error fetching orders by status: {}", status, e);
                if (e instanceof IllegalArgumentException) {
                    throw new NotFoundException("Invalid status format");
                }
                throw new RuntimeException("Failed to fetch orders by status", e);
            })
            .doOnComplete(() -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getOrdersByStatus - Successfully fetched orders. Duration: {}ms", duration);
            });
    }

    /**
     * Retrieves an order by its ID.
     * 
     * @param id the ID of the order
     * @return a Mono of the order
     * @throws NotFoundException if the order is not found
     */
    public Mono<Order> getOrderById(String id) {
        log.info("[START] getOrderById - Fetching order with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Order not found with id: " + id)))
            .doOnSuccess(order -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getOrderById - Successfully fetched order. Duration: {}ms", duration);
            })
            .doOnError(e -> log.error("[ERROR] getOrderById - Error fetching order with id: {}", id, e))
            .onErrorMap(e -> {
                if (e instanceof NotFoundException) {
                    return e;
                }
                if (e instanceof IllegalArgumentException) {
                    return new NotFoundException("Invalid order ID format: " + id);
                }
                return new RuntimeException("Failed to fetch order", e);
            });
    }

    /**
     * Creates a new order.
     * Validates that the order contains at least one pizza and that all pizzas exist in the database.
     * 
     * @param newOrder the order to create
     * @return a Mono of the created order
     * @throws IllegalArgumentException if the order is invalid
     * @throws NotFoundException if any pizza ID is not found
     */
    public Mono<Order> createOrder(Order newOrder) {
        log.info("[START] createOrder - Creating new order: {}", newOrder);
        long startTime = System.currentTimeMillis();
        
        if (newOrder.getPizzas() == null || newOrder.getPizzas().isEmpty()) {
            log.error("[ERROR] createOrder - At least one pizza is required");
            throw new IllegalArgumentException("At least one pizza is required");
        }
        
        if (newOrder.getStatus() == null || newOrder.getStatus().isEmpty()) {
            newOrder.setStatus("PENDING");
        }
        
        // Validate that all pizza IDs exist
        return Flux.fromIterable(newOrder.getPizzas())
            .flatMap(pizzaId -> pizzaService.getPizzaById(pizzaId)
                .doOnNext(pizza -> log.debug("Validated pizza: {}", pizza))
                .switchIfEmpty(Mono.error(new NotFoundException("Pizza not found with id: " + pizzaId))))
            .then()
            .then(Mono.defer(() -> {
                newOrder.setTimestamp(new Date());
                return orderRepository.save(newOrder)
                    .doOnSuccess(order -> {
                        long duration = System.currentTimeMillis() - startTime;
                        log.info("[END] createOrder - Successfully created order. Duration: {}ms", duration);
                    })
                    .onErrorMap(e -> {
                        log.error("[ERROR] createOrder - Error creating order: {}", newOrder, e);
                        if (e instanceof IllegalArgumentException) {
                            return e;
                        }
                        return new RuntimeException("Failed to create order", e);
                    });
            }));
    }

    /**
     * Updates the status of an existing order.
     * 
     * @param id the ID of the order to update
     * @param newStatus the new status for the order
     * @return a Mono of the updated order
     * @throws NotFoundException if the order is not found
     */
    public Mono<Order> updateOrderStatus(String id, String newStatus) {
        log.info("[START] updateOrderStatus - Updating order status for order: {} to {}", id, newStatus);
        long startTime = System.currentTimeMillis();
        
        if (newStatus == null || newStatus.isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }
        
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Order not found with id: " + id)))
            .flatMap(order -> {
                order.setStatus(newStatus);
                return orderRepository.save(order);
            })
            .doOnSuccess(order -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] updateOrderStatus - Successfully updated order status. Duration: {}ms", duration);
            })
            .onErrorMap(e -> {
                log.error("[ERROR] updateOrderStatus - Error updating order status: {}", id, e);
                if (e instanceof NotFoundException) {
                    return e;
                }
                if (e instanceof IllegalArgumentException) {
                    return new NotFoundException("Invalid status format");
                }
                return new RuntimeException("Failed to update order status", e);
            });
    }

    /**
     * Cancels an existing order.
     * 
     * @param id the ID of the order to cancel
     * @return a Mono of Void
     * @throws NotFoundException if the order is not found
     */
    public Mono<Void> cancelOrder(String id) {
        log.info("[START] cancelOrder - Canceling order with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Order not found with id: " + id)))
            .flatMap(order -> orderRepository.delete(order))
            .doOnSuccess(v -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] cancelOrder - Successfully canceled order. Duration: {}ms", duration);
            })
            .onErrorMap(e -> {
                log.error("[ERROR] cancelOrder - Error canceling order: {}", id, e);
                if (e instanceof NotFoundException) {
                    return e;
                }
                if (e instanceof IllegalArgumentException) {
                    return new NotFoundException("Invalid order ID format: " + id);
                }
                return new RuntimeException("Failed to cancel order", e);
            });
    }
}