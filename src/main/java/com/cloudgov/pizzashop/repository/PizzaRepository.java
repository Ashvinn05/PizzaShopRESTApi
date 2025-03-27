package com.cloudgov.pizzashop.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import com.cloudgov.pizzashop.model.Pizza;
import reactor.core.publisher.Mono;

/**
 * Repository interface for pizza-related database operations.
 * Provides methods to interact with the pizzas collection in MongoDB.
 * Uses reactive programming with Project Reactor's Mono.
 */
@Repository
public interface PizzaRepository extends ReactiveMongoRepository<Pizza, String> {
    /**
     * Finds a pizza by its name.
     * 
     * @param name the name of the pizza to find
     * @return Mono containing the pizza if found, or empty if not found
     */
    Mono<Pizza> findByName(String name);
}