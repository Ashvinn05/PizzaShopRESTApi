package com.cloudgov.pizzashop.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cloudgov.pizzashop.model.Order;
import com.cloudgov.pizzashop.repository.OrderRepository;
import com.cloudgov.pizzashop.exception.NotFoundException;
import java.util.Date;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Flux<Order> getAllOrders() {
        log.debug("Fetching all orders from database");
        return orderRepository.findAll()
            .doOnNext(order -> log.debug("Found order: {}", order))
            .switchIfEmpty(Flux.empty())
            .doOnError(e -> log.error("Error fetching orders: {}", e.getMessage(), e));
    }

    public Flux<Order> getOrdersByStatus(String status) {
        log.debug("Fetching orders with status: {}", status);
        return orderRepository.findByStatus(status)
            .doOnNext(order -> log.debug("Found order: {}", order))
            .switchIfEmpty(Flux.empty())
            .doOnError(e -> log.error("Error fetching orders by status: {}", e.getMessage(), e));
    }

    public Mono<Order> getOrderById(String id) {
        log.debug("Fetching order with id: {}", id);
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Order not found: " + id)))
            .doOnError(e -> log.error("Error fetching order: {}", e.getMessage(), e));
    }

    public Mono<Order> createOrder(Order newOrder) {
        log.debug("Creating new order: {}", newOrder);
        if (newOrder.getPizzas() == null || newOrder.getPizzas().isEmpty()) {
            return Mono.error(new IllegalArgumentException("At least one pizza is required"));
        }
        
        newOrder.setTimestamp(new Date());
        return orderRepository.save(newOrder)
            .doOnNext(order -> log.debug("Created order: {}", order))
            .doOnError(e -> log.error("Error creating order: {}", e.getMessage(), e));
    }

    public Mono<Order> updateOrderStatus(String id, String newStatus) {
        log.debug("Updating order status for order: {} to {}", id, newStatus);
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Order not found: " + id)))
            .flatMap(order -> {
                order.setStatus(newStatus);
                return orderRepository.save(order);
            })
            .doOnNext(order -> log.debug("Updated order status: {}", order))
            .doOnError(e -> log.error("Error updating order status: {}", e.getMessage(), e));
    }

    public Mono<Void> cancelOrder(String id) {
        log.debug("Canceling order with id: {}", id);
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Order not found: " + id)))
            .flatMap(order -> orderRepository.delete(order))
            .doOnNext(void_ -> log.debug("Canceled order: {}", id))
            .doOnError(e -> log.error("Error canceling order: {}", e.getMessage(), e));
    }
}