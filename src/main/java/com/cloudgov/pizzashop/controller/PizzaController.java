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

/**
 * Controller for managing pizza-related operations in the pizza shop.
 * Provides REST endpoints for creating, retrieving, updating, and deleting pizzas.
 * Uses reactive programming with Project Reactor's Flux and Mono.
 */
@RestController
@RequestMapping("/pizzas")
public class PizzaController {
    private final PizzaService pizzaService;
    private static final Logger log = LoggerFactory.getLogger(PizzaController.class);

    /**
     * Constructor for PizzaController.
     * 
     * @param pizzaService the service layer for pizza operations
     */
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    /**
     * Retrieves all pizzas from the database.
     * Returns a reactive stream of pizzas.
     * 
     * @return Flux of Pizza objects
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Pizza> getAllPizzas() {
        log.info("[START] getAllPizzas - Handling GET request for all pizzas");
        long startTime = System.currentTimeMillis();
        
        return pizzaService.getAllPizzas()
            .doOnNext(pizza -> log.debug("Found pizza: {}", pizza))
            .doOnError(e -> {
                log.error("[ERROR] getAllPizzas - Error fetching pizzas", e);
                throw new RuntimeException("Failed to fetch pizzas", e);
            })
            .doOnComplete(() -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getAllPizzas - Successfully fetched pizzas. Duration: {}ms", duration);
            });
    }

    /**
     * Retrieves a specific pizza by its ID.
     * Returns a Mono containing the pizza if found, or throws an exception if not found.
     * 
     * @param id the ID of the pizza to retrieve
     * @return Mono containing the Pizza object
     */
    @GetMapping("/{id}")
    public Mono<Pizza> getPizzaById(@PathVariable String id) {
        log.info("[START] getPizzaById - Handling GET request for pizza with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return pizzaService.getPizzaById(id)
            .doOnNext(pizza -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getPizzaById - Successfully fetched pizza. Duration: {}ms", duration);
            })
            .doOnError(e -> {
                log.error("[ERROR] getPizzaById - Error fetching pizza with id: {}", id, e);
                throw new RuntimeException("Failed to fetch pizza", e);
            });
    }

    /**
     * Creates a new pizza.
     * Validates the input and returns the created pizza with a 201 Created status.
     * 
     * @param newPizza the pizza object to create
     * @return Mono containing the created Pizza object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pizza> createPizza(@Valid @RequestBody Pizza newPizza) {
        log.info("[START] createPizza - Handling POST request to create pizza: {}", newPizza);
        long startTime = System.currentTimeMillis();
        
        return pizzaService.createPizza(newPizza)
            .doOnNext(pizza -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] createPizza - Successfully created pizza. Duration: {}ms", duration);
            })
            .doOnError(e -> {
                log.error("[ERROR] createPizza - Error creating pizza: {}", newPizza, e);
                throw new RuntimeException("Failed to create pizza", e);
            });
    }

    /**
     * Updates an existing pizza.
     * Validates the input and returns the updated pizza.
     * 
     * @param id the ID of the pizza to update
     * @param updatedPizza the updated pizza object
     * @return Mono containing the updated Pizza object
     */
    @PutMapping("/{id}")
    public Mono<Pizza> updatePizza(@PathVariable String id, @Valid @RequestBody Pizza updatedPizza) {
        log.info("[START] updatePizza - Handling PUT request to update pizza with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return pizzaService.updatePizza(id, updatedPizza)
            .doOnNext(pizza -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] updatePizza - Successfully updated pizza. Duration: {}ms", duration);
            })
            .doOnError(e -> {
                log.error("[ERROR] updatePizza - Error updating pizza with id: {}", id, e);
                throw new RuntimeException("Failed to update pizza", e);
            });
    }

    /**
     * Deletes a pizza by its ID.
     * Returns a Mono<Void> to indicate successful deletion.
     * 
     * @param id the ID of the pizza to delete
     * @return Mono<Void>
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletePizza(@PathVariable String id) {
        log.info("[START] deletePizza - Handling DELETE request for pizza with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return pizzaService.deletePizza(id)
            .doOnNext(void_ -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] deletePizza - Successfully deleted pizza. Duration: {}ms", duration);
            })
            .doOnError(e -> {
                log.error("[ERROR] deletePizza - Error deleting pizza: {}", id, e);
                throw new RuntimeException("Failed to delete pizza", e);
            });
    }
}