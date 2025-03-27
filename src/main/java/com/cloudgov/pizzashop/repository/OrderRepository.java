package com.cloudgov.pizzashop.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import com.cloudgov.pizzashop.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository interface for order-related database operations.
 * Provides methods to interact with the orders collection in MongoDB.
 * Uses reactive programming with Project Reactor's Mono and Flux.
 */
@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
    /**
     * Finds an order by its ID.
     * 
     * @param id the ID of the order to find
     * @return Mono containing the order if found, or empty if not found
     */
    Mono<Order> findById(String id);

    /**
     * Finds orders by customer email.
     * 
     * @param email the customer email to search for
     * @return Flux containing all orders for the specified email
     */
    Flux<Order> findByCustomerEmail(String email);

    /**
     * Finds orders by status.
     * 
     * @param status the status to filter orders by
     * @return Flux containing all orders with the specified status
     */
    Flux<Order> findByStatus(String status);
}