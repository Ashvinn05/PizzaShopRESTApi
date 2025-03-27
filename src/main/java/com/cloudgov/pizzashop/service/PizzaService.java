package com.cloudgov.pizzashop.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cloudgov.pizzashop.model.Pizza;
import com.cloudgov.pizzashop.repository.PizzaRepository;
import com.cloudgov.pizzashop.exception.NotFoundException;

/**
 * Service class for pizza-related operations.
 */
@Service
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    private final Logger log = LoggerFactory.getLogger(PizzaService.class);

    /**
     * Constructor for PizzaService.
     * 
     * @param pizzaRepository the pizza repository
     */
    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    /**
     * Retrieves all pizzas from the database.
     * 
     * @return a Flux of pizzas
     */
    public Flux<Pizza> getAllPizzas() {
        log.info("[START] getAllPizzas - Fetching all pizzas from database");
        long startTime = System.currentTimeMillis();
        
        return pizzaRepository.findAll()
            .doOnNext(pizza -> log.debug("Found pizza: {}", pizza))
            .switchIfEmpty(Flux.empty())
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
     * Retrieves a pizza by its ID.
     * 
     * @param id the ID of the pizza
     * @return a Mono of the pizza
     */
    public Mono<Pizza> getPizzaById(String id) {
        log.info("[START] getPizzaById - Fetching pizza with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return pizzaRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Pizza not found with id: " + id)))
            .doOnSuccess(pizza -> {
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
     * 
     * @param pizza the pizza to create
     * @return a Mono of the created pizza
     */
    public Mono<Pizza> createPizza(Pizza pizza) {
        log.info("[START] createPizza - Creating new pizza: {}", pizza);
        long startTime = System.currentTimeMillis();
        
        return validatePizza(pizza)
            .then(pizzaRepository.findByName(pizza.getName()))
            .hasElement()
            .flatMap(exists -> {
                if (exists) {
                    log.error("[ERROR] createPizza - Pizza with name '{}' already exists", pizza.getName());
                    throw new IllegalArgumentException("Pizza with name '" + pizza.getName() + "' already exists");
                }
                return pizzaRepository.save(pizza)
                    .doOnSuccess(savedPizza -> {
                        long duration = System.currentTimeMillis() - startTime;
                        log.info("[END] createPizza - Successfully created pizza. Duration: {}ms", duration);
                    })
                    .doOnError(e -> {
                        log.error("[ERROR] createPizza - Error creating pizza: {}", pizza, e);
                        throw new RuntimeException("Failed to create pizza", e);
                    });
            });
    }

    /**
     * Updates an existing pizza.
     * 
     * @param id   the ID of the pizza to update
     * @param pizza the updated pizza
     * @return a Mono of the updated pizza
     */
    public Mono<Pizza> updatePizza(String id, Pizza pizza) {
        log.info("[START] updatePizza - Updating pizza with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return pizzaRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Pizza not found with id: " + id)))
            .flatMap(existing -> {
                log.debug("Updating existing pizza: {}", existing);
                return pizzaRepository.save(pizza)
                    .doOnSuccess(savedPizza -> {
                        long duration = System.currentTimeMillis() - startTime;
                        log.info("[END] updatePizza - Successfully updated pizza. Duration: {}ms", duration);
                    })
                    .doOnError(e -> {
                        log.error("[ERROR] updatePizza - Error updating pizza: {}", pizza, e);
                        throw new RuntimeException("Failed to update pizza", e);
                    });
            });
    }

    /**
     * Deletes a pizza by its ID.
     * 
     * @param id the ID of the pizza to delete
     * @return a Mono of Void
     */
    public Mono<Void> deletePizza(String id) {
        log.info("[START] deletePizza - Deleting pizza with id: {}", id);
        long startTime = System.currentTimeMillis();
        
        return pizzaRepository.findById(id)
            .switchIfEmpty(Mono.error(new NotFoundException("Pizza not found with id: " + id)))
            .flatMap(pizza -> pizzaRepository.delete(pizza)
                .doOnSuccess(v -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("[END] deletePizza - Successfully deleted pizza. Duration: {}ms", duration);
                })
                .doOnError(e -> {
                    log.error("[ERROR] deletePizza - Error deleting pizza: {}", id, e);
                    throw new RuntimeException("Failed to delete pizza", e);
                }));
    }

    /**
     * Validates a pizza.
     * 
     * @param pizza the pizza to validate
     * @return a Mono of Void
     */
    private Mono<Void> validatePizza(Pizza pizza) {
        if (pizza.getName() == null || pizza.getName().isEmpty()) {
            log.error("[ERROR] validatePizza - Pizza name is required");
            return Mono.error(new IllegalArgumentException("Pizza name is required"));
        }
        return Mono.empty();
    }
}