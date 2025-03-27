package com.cloudgov.pizzashop.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import com.cloudgov.pizzashop.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
    Mono<Order> findById(String id);
    Flux<Order> findByCustomerEmail(String email);
    Flux<Order> findByStatus(String status);
}