/**
 * Database initialization component that sets up the application's database with
 * initial data for pizzas and orders.
 *
 * This class implements ApplicationRunner to automatically initialize the database
 * when the Spring Boot application starts.
 */
package com.cloudgov.pizzashop;

import com.cloudgov.pizzashop.model.Config;
import com.cloudgov.pizzashop.model.Order;
import com.cloudgov.pizzashop.model.Pizza;
import com.cloudgov.pizzashop.repository.ConfigRepository;
import com.cloudgov.pizzashop.repository.OrderRepository;
import com.cloudgov.pizzashop.repository.PizzaRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Component that initializes the database with sample data for pizzas and orders.
 *
 * This class reads JSON files from the classpath to populate the database with
 * initial data, ensuring that the initialization only happens once.
 */
@Component
public class DatabaseInitializer implements ApplicationRunner {
    private final PizzaRepository pizzaRepository;
    private final OrderRepository orderRepository;
    private final ConfigRepository configRepository;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    /**
     * Constructor for DatabaseInitializer.
     *
     * @param pizzaRepository   Repository for managing pizza documents
     * @param orderRepository   Repository for managing order documents
     * @param configRepository  Repository for managing configuration documents
     * @param objectMapper      Jackson object mapper for JSON processing
     */
    public DatabaseInitializer(PizzaRepository pizzaRepository, OrderRepository orderRepository,
                              ConfigRepository configRepository, ObjectMapper objectMapper) {
        this.pizzaRepository = pizzaRepository;
        this.orderRepository = orderRepository;
        this.configRepository = configRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Application startup method that checks if the database is already initialized
     * and initializes it with sample data if not.
     *
     * @param args Application arguments (not used)
     */
    @Override
    public void run(ApplicationArguments args) {
        log.info("[START] run - Starting database initialization");
        long startTime = System.currentTimeMillis();

        configRepository.findByKey("isInitialized")
            .switchIfEmpty(Mono.just(new Config("isInitialized", false)))
            .flatMap(config -> {
                if (config.isValue()) {
                    log.info("[END] run - Database already initialized, skipping");
                    return Mono.empty();
                } else {
                    return initializeDatabase()
                        .doOnSuccess(v -> {
                            long duration = System.currentTimeMillis() - startTime;
                            log.info("[END] run - Successfully completed database initialization. Duration: {}ms", duration);
                        });
                }
            })
            .doOnError(e -> {
                log.error("[ERROR] run - Initialization failed: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
            })
            .subscribe();
    }

    /**
     * Initializes the database with sample data for pizzas and orders.
     *
     * This method:
     * 1. Reads pizza data from JSON file
     * 2. Saves pizzas to the database
     * 3. Reads order data from JSON file
     * 4. Links orders with pizza IDs
     * 5. Saves orders to the database
     * 6. Marks database as initialized
     *
     * @return A Mono that completes when initialization is done
     */
    private Mono<Void> initializeDatabase() {
        log.info("[START] initializeDatabase - Starting database initialization");
        long startTime = System.currentTimeMillis();

        try (InputStream pizzaStream = new ClassPathResource("data/pizza.json").getInputStream()) {
            List<Pizza> pizzas = objectMapper.readValue(pizzaStream, new TypeReference<List<Pizza>>(){});

            return pizzaRepository.deleteAll()
                .thenMany(Flux.fromIterable(pizzas))
                .flatMap(pizzaRepository::save)
                .collectList()
                .flatMap(savedPizzas -> {
                    try (InputStream orderStream = new ClassPathResource("data/order.json").getInputStream()) {
                        List<Order> orders = objectMapper.readValue(orderStream, new TypeReference<List<Order>>(){});

                        if (!savedPizzas.isEmpty()) {
                            orders.get(0).setPizzas(List.of(savedPizzas.get(0).getId()));
                            orders.get(1).setPizzas(List.of(savedPizzas.get(1).getId(), savedPizzas.get(2).getId()));
                            orders.get(2).setPizzas(List.of(savedPizzas.get(2).getId()));
                            orders.get(3).setPizzas(List.of(savedPizzas.get(0).getId(), savedPizzas.get(1).getId()));
                        }

                        return orderRepository.deleteAll()
                            .thenMany(Flux.fromIterable(orders))
                            .flatMap(orderRepository::save)
                            .then();
                    } catch (IOException e) {
                        log.error("[ERROR] initializeDatabase - Failed to read order JSON file: {}", e.getMessage(), e);
                        throw new RuntimeException("Failed to read order JSON file: " + e.getMessage(), e);
                    }
                })
                .then(configRepository.save(new Config("isInitialized", true)))
                .doOnSuccess(v -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("[END] initializeDatabase - Successfully completed initialization. Duration: {}ms", duration);
                    log.info("[INFO] initializeDatabase - Database initialized with {} pizzas and 4 orders", pizzas.size());
                })
                .then();
        } catch (IOException e) {
            log.error("[ERROR] initializeDatabase - Failed to read pizza JSON file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read pizza JSON file: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("[ERROR] initializeDatabase - Failed to initialize database: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }
    }
}