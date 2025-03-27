package com.cloudgov.pizzashop.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cloudgov.pizzashop.model.Pizza;
import com.cloudgov.pizzashop.repository.PizzaRepository;
import com.cloudgov.pizzashop.exception.NotFoundException;


@Service
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    private final Logger log = LoggerFactory.getLogger(PizzaService.class);


    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public Flux<Pizza> getAllPizzas() {
        log.debug("Fetching all pizzas from database");
        return pizzaRepository.findAll()
            .doOnNext(pizza -> log.debug("Found pizza: {}", pizza))
            .switchIfEmpty(Flux.empty())
            .doOnError(e -> log.error("Error fetching pizzas: {}", e.getMessage(), e));
    }

    public Mono<Pizza> getPizzaById(String id) {
        log.debug("Fetching pizza with id: {}", id);
        return pizzaRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Pizza not found: " + id)))
            .doOnError(e -> log.error("Error fetching pizza: {}", e.getMessage(), e));
    }

    public Mono<Pizza> createPizza(Pizza pizza) {
        log.debug("Creating new pizza: {}", pizza);
        return validatePizza(pizza)
            .then(pizzaRepository.findByName(pizza.getName()))
            .hasElement()
            .flatMap(exists -> {
                if (exists) {
                    log.error("Pizza with name '{}' already exists", pizza.getName());
                    return Mono.error(new IllegalArgumentException("Pizza with name '" + pizza.getName() + "' already exists"));
                }
                return pizzaRepository.save(pizza);
            });
    }

    public Mono<Pizza> updatePizza(String id, Pizza pizza) {
        log.debug("Updating pizza with id: {}", id);
        return pizzaRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Pizza not found: " + id)))
            .flatMap(existing -> {
                log.debug("Updating existing pizza: {}", existing);
                return pizzaRepository.save(pizza);
            })
            .doOnError(e -> log.error("Error updating pizza: {}", e.getMessage(), e));
    }

    public Mono<Void> deletePizza(String id) {
        log.debug("Deleting pizza with id: {}", id);
        return pizzaRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Pizza not found: " + id)))
            .flatMap(pizza -> pizzaRepository.delete(pizza))
            .doOnError(e -> log.error("Error deleting pizza: {}", e.getMessage(), e));
    }

    private Mono<Void> validatePizza(Pizza pizza) {
        if (pizza.getName() == null || pizza.getName().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Pizza name is required"));
        }
        return Mono.empty();
    }
}