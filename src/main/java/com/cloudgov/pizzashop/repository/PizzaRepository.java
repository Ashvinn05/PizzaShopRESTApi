package com.cloudgov.pizzashop.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import com.cloudgov.pizzashop.model.Pizza;
import reactor.core.publisher.Mono;

@Repository
public interface PizzaRepository extends ReactiveMongoRepository<Pizza, String> {
    Mono<Pizza> findByName(String name);
}