package com.cloudgov.pizzashop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cloudgov.pizzashop.service.PizzaService;
import com.cloudgov.pizzashop.model.Pizza;

@RestController
@RequestMapping("/pizzas")
public class PizzaController {
    private final PizzaService pizzaService;
    private static final Logger log = LoggerFactory.getLogger(PizzaController.class);

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Pizza> getAllPizzas() {
        log.debug("Handling GET request for all pizzas");
        return pizzaService.getAllPizzas()
            .doOnNext(pizza -> log.debug("Found pizza: {}", pizza))
            .doOnError(e -> log.error("Error fetching pizzas: {}", e.getMessage(), e));
    }

    @GetMapping("/{id}")
    public Mono<Pizza> getPizzaById(@PathVariable String id) {
        log.debug("Handling GET request for pizza with id: {}", id);
        return pizzaService.getPizzaById(id)
            .doOnNext(pizza -> log.debug("Found pizza: {}", pizza))
            .doOnError(e -> log.error("Error fetching pizza: {}", e.getMessage(), e));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pizza> createPizza(@Valid @RequestBody Pizza newPizza) {
        log.debug("Handling POST request to create pizza: {}", newPizza);
        return pizzaService.createPizza(newPizza)
            .doOnNext(pizza -> log.debug("Created pizza: {}", pizza))
            .doOnError(e -> log.error("Error creating pizza: {}", e.getMessage(), e));
    }

    @PutMapping("/{id}")
    public Mono<Pizza> updatePizza(@PathVariable String id, @Valid @RequestBody Pizza updatedPizza) {
        log.debug("Handling PUT request to update pizza with id: {}", id);
        return pizzaService.updatePizza(id, updatedPizza)
            .doOnNext(pizza -> log.debug("Updated pizza: {}", pizza))
            .doOnError(e -> log.error("Error updating pizza: {}", e.getMessage(), e));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletePizza(@PathVariable String id) {
        log.debug("Handling DELETE request for pizza with id: {}", id);
        return pizzaService.deletePizza(id)
            .doOnError(e -> log.error("Error deleting pizza: {}", e.getMessage(), e));
    }
}