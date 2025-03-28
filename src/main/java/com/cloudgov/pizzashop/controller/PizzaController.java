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
import com.cloudgov.pizzashop.model.ApiResponse;

import java.util.List;

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
     * Returns a reactive stream of pizzas wrapped in an ApiResponse.
     * 
     * @return Mono containing ApiResponse with a list of pizzas
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<List<Pizza>>> getAllPizzas() {
        log.info("[START] getAllPizzas - Handling GET request for all pizzas");
        long startTime = System.currentTimeMillis();
        
        return pizzaService.getAllPizzas()
            .collectList()
            .map(pizzas -> ApiResponse.success(pizzas))
            .doOnNext(response -> log.debug("Found pizzas: {}", response.getData()))
            .doOnError(e -> {
                log.error("[ERROR] getAllPizzas - Error fetching pizzas", e);
                throw new RuntimeException("Failed to fetch pizzas", e);
            })
            .doOnSuccess(response -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] getAllPizzas - Successfully fetched pizzas. Duration: {}ms", duration);
            });
    }

    /**
     * Retrieves a specific pizza by its ID.
     * Returns a Mono containing the pizza wrapped in an ApiResponse if found.
     * Throws NotFoundException if the pizza is not found.
     * 
     * @param id the ID of the pizza to retrieve
     * @return Mono containing ApiResponse with the pizza
     */
    @GetMapping("/{id}")
    public Mono<ApiResponse<Pizza>> getPizzaById(@PathVariable String id) {
        log.info("[START] getPizzaById - Handling GET request for pizza with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return pizzaService.getPizzaById(id)
            .map(pizza -> ApiResponse.success(pizza))
            .doOnNext(response -> {
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
     * Validates the input and returns the created pizza wrapped in an ApiResponse.
     * Returns a 201 Created status on success.
     * 
     * @param newPizza the pizza object to create
     * @return Mono containing ApiResponse with the created pizza
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiResponse<Pizza>> createPizza(@Valid @RequestBody Pizza newPizza) {
        log.info("[START] createPizza - Handling POST request to create pizza: {}", newPizza);
        long startTime = System.currentTimeMillis();
        
        return pizzaService.createPizza(newPizza)
            .map(pizza -> ApiResponse.success(pizza, "Pizza created successfully"))
            .doOnNext(response -> {
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
     * Validates the input and returns the updated pizza wrapped in an ApiResponse.
     * 
     * @param id the ID of the pizza to update
     * @param updatedPizza the updated pizza object
     * @return Mono containing ApiResponse with the updated pizza
     */
    @PutMapping("/{id}")
    public Mono<ApiResponse<Pizza>> updatePizza(@PathVariable String id, @Valid @RequestBody Pizza updatedPizza) {
        log.info("[START] updatePizza - Handling PUT request to update pizza with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return pizzaService.updatePizza(id, updatedPizza)
            .map(pizza -> ApiResponse.success(pizza, "Pizza updated successfully"))
            .doOnNext(response -> {
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
     * Returns an ApiResponse with a success message.
     * 
     * @param id the ID of the pizza to delete
     * @return Mono containing ApiResponse with a success message
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ApiResponse<String>> deletePizza(@PathVariable String id) {
        log.info("[START] deletePizza - Handling DELETE request for pizza with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return pizzaService.deletePizza(id)
            .then(Mono.just(ApiResponse.success("", "Pizza deleted successfully")))
            .doOnNext(response -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("[END] deletePizza - Successfully deleted pizza. Duration: {}ms", duration);
            })
            .doOnError(e -> {
                log.error("[ERROR] deletePizza - Error deleting pizza: {}", id, e);
                throw new RuntimeException("Failed to delete pizza", e);
            });
    }
}