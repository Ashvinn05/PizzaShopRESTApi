package com.cloudgov.pizzashop.service;

import com.cloudgov.pizzashop.model.Pizza;
import com.cloudgov.pizzashop.repository.PizzaRepository;
import com.cloudgov.pizzashop.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PizzaServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;

    @InjectMocks
    private PizzaService pizzaService;

    private Pizza testPizza;

    @BeforeEach
    void setUp() {
        testPizza = new Pizza();
        testPizza.setId("1");
        testPizza.setName("Test Pizza");
        testPizza.setDescription("A test pizza");
    }

    @Test
    void shouldReturnAllPizzasWhenAvailable() {
        // Given
        List<Pizza> pizzas = List.of(testPizza);
        when(pizzaRepository.findAll()).thenReturn(Flux.fromIterable(pizzas));

        // When
        Flux<Pizza> result = pizzaService.getAllPizzas();

        // Then
        StepVerifier.create(result)
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyFluxWhenNoPizzasAvailable() {
        // Given
        when(pizzaRepository.findAll()).thenReturn(Flux.empty());

        // When
        Flux<Pizza> result = pizzaService.getAllPizzas();

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenErrorFetchingPizzas() {
        // Given
        when(pizzaRepository.findAll()).thenReturn(Flux.error(new RuntimeException("Test error")));

        // When
        Flux<Pizza> result = pizzaService.getAllPizzas();

        // Then
        StepVerifier.create(result)
            .verifyError(RuntimeException.class);
    }

    @Test
    void shouldReturnPizzaWhenIdExists() {
        // Given
        when(pizzaRepository.findById(testPizza.getId())).thenReturn(Mono.just(testPizza));

        // When
        Mono<Pizza> result = pizzaService.getPizzaById(testPizza.getId());

        // Then
        StepVerifier.create(result)
            .expectNext(testPizza)
            .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        // Given
        when(pizzaRepository.findById("nonexistent")).thenReturn(Mono.empty());

        // When
        Mono<Pizza> result = pizzaService.getPizzaById("nonexistent");

        // Then
        StepVerifier.create(result)
            .verifyError(NotFoundException.class);
    }

    @Test
    void shouldCreateNewPizza() {
        // Given
        Pizza newPizza = new Pizza();
        newPizza.setName("New Pizza");
        newPizza.setDescription("A new pizza");
        
        when(pizzaRepository.findByName(newPizza.getName())).thenReturn(Mono.empty());
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(Mono.just(newPizza));

        // When
        Mono<Pizza> result = pizzaService.createPizza(newPizza);

        // Then
        StepVerifier.create(result)
            .expectNext(newPizza)
            .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicatePizza() {
        // Given
        Pizza duplicatePizza = new Pizza();
        duplicatePizza.setName("Duplicate Pizza");
        
        when(pizzaRepository.findByName(duplicatePizza.getName())).thenReturn(Mono.just(duplicatePizza));

        // When
        Mono<Pizza> result = pizzaService.createPizza(duplicatePizza);

        // Then
        StepVerifier.create(result)
            .verifyError(IllegalArgumentException.class);
    }

    @Test
    void shouldUpdateExistingPizza() {
        // Given
        Pizza updatedPizza = new Pizza();
        updatedPizza.setId(testPizza.getId());
        updatedPizza.setName("Updated Pizza");
        updatedPizza.setDescription("An updated pizza");
        
        when(pizzaRepository.findById(testPizza.getId())).thenReturn(Mono.just(testPizza));
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(Mono.just(updatedPizza));

        // When
        Mono<Pizza> result = pizzaService.updatePizza(testPizza.getId(), updatedPizza);

        // Then
        StepVerifier.create(result)
            .expectNext(updatedPizza)
            .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistingPizza() {
        // Given
        Pizza nonExistingPizza = new Pizza();
        nonExistingPizza.setId("nonexistent");
        
        when(pizzaRepository.findById(nonExistingPizza.getId())).thenReturn(Mono.empty());

        // When
        Mono<Pizza> result = pizzaService.updatePizza(nonExistingPizza.getId(), nonExistingPizza);

        // Then
        StepVerifier.create(result)
            .verifyError(NotFoundException.class);
    }

    @Test
    void shouldDeleteExistingPizza() {
        // Given
        when(pizzaRepository.findById(testPizza.getId())).thenReturn(Mono.just(testPizza));
        when(pizzaRepository.delete(testPizza)).thenReturn(Mono.empty());

        // When
        Mono<Void> result = pizzaService.deletePizza(testPizza.getId());

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistingPizza() {
        // Given
        when(pizzaRepository.findById("nonexistent")).thenReturn(Mono.empty());

        // When
        Mono<Void> result = pizzaService.deletePizza("nonexistent");

        // Then
        StepVerifier.create(result)
            .verifyError(NotFoundException.class);
    }
}