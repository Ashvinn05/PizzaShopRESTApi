package com.cloudgov.pizzashop.service;

import com.cloudgov.pizzashop.model.Order;
import com.cloudgov.pizzashop.repository.OrderRepository;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId("1");
        testOrder.setStatus("pending");
        testOrder.setPizzas(new ArrayList<>());
        testOrder.setTimestamp(new Date());
    }

    @Test
    void shouldReturnAllOrdersWhenAvailable() {
        // Given
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findAll()).thenReturn(Flux.fromIterable(orders));

        // When
        Flux<Order> result = orderService.getAllOrders();

        // Then
        StepVerifier.create(result)
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyFluxWhenNoOrdersAvailable() {
        // Given
        when(orderRepository.findAll()).thenReturn(Flux.empty());

        // When
        Flux<Order> result = orderService.getAllOrders();

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenErrorFetchingOrders() {
        // Given
        when(orderRepository.findAll()).thenReturn(Flux.error(new RuntimeException("Test error")));

        // When
        Flux<Order> result = orderService.getAllOrders();

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof RuntimeException);
                assertEquals("Failed to fetch orders", e.getMessage());
            });
    }

    @Test
    void shouldReturnOrdersByStatusWhenAvailable() {
        // Given
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findByStatus(testOrder.getStatus())).thenReturn(Flux.fromIterable(orders));

        // When
        Flux<Order> result = orderService.getOrdersByStatus(testOrder.getStatus());

        // Then
        StepVerifier.create(result)
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyFluxWhenNoOrdersByStatus() {
        // Given
        when(orderRepository.findByStatus("completed")).thenReturn(Flux.empty());

        // When
        Flux<Order> result = orderService.getOrdersByStatus("completed");

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void shouldReturnOrderWhenIdExists() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Mono.just(testOrder));

        // When
        Mono<Order> result = orderService.getOrderById(testOrder.getId());

        // Then
        StepVerifier.create(result)
            .expectNext(testOrder)
            .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        // Given
        when(orderRepository.findById("nonexistent")).thenReturn(Mono.empty());

        // When
        Mono<Order> result = orderService.getOrderById("nonexistent");

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof NotFoundException);
                assertEquals("Order not found", e.getMessage());
            });
    }

    @Test
    void shouldCreateNewOrder() {
        // Given
        Order newOrder = new Order();
        newOrder.setStatus("pending");
        newOrder.setPizzas(List.of("1", "2"));
        
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(newOrder));

        // When
        Mono<Order> result = orderService.createOrder(newOrder);

        // Then
        StepVerifier.create(result)
            .expectNext(newOrder)
            .verifyComplete();
    }

    @Test
    void shouldThrowExceptionWhenCreatingOrderWithNoPizzas() {
        // Given
        Order invalidOrder = new Order();
        invalidOrder.setStatus("pending");
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(invalidOrder).block();
        }, "At least one pizza is required");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInvalidIdFormat() {
        // Given
        String invalidId = "invalid-id-format";
        when(orderRepository.findById(invalidId)).thenReturn(Mono.error(new IllegalArgumentException("Invalid ID format")));

        // When
        Mono<Order> result = orderService.getOrderById(invalidId);

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof NotFoundException);
                assertEquals("Invalid order ID format: " + invalidId, e.getMessage());
            });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingOrderWithEmptyStatus() {
        // Given
        Order invalidOrder = new Order();
        invalidOrder.setPizzas(List.of("1", "2"));
        invalidOrder.setStatus("");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(invalidOrder).block();
        }, "Status is required");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingOrderWithNullStatus() {
        // Given
        Order invalidOrder = new Order();
        invalidOrder.setPizzas(List.of("1", "2"));
        invalidOrder.setStatus(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(invalidOrder).block();
        }, "Status is required");
    }

    @Test
    void shouldUpdateOrderStatus() {
        // Given
        String newStatus = "preparing";
        when(orderRepository.findById(testOrder.getId())).thenReturn(Mono.just(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(testOrder));

        // When
        Mono<Order> result = orderService.updateOrderStatus(testOrder.getId(), newStatus);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(order -> order.getStatus().equals(newStatus))
            .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistingOrder() {
        // Given
        when(orderRepository.findById("nonexistent")).thenReturn(Mono.empty());

        // When
        Mono<Order> result = orderService.updateOrderStatus("nonexistent", "preparing");

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof NotFoundException);
                assertEquals("Order not found", e.getMessage());
            });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingOrderWithEmptyStatus() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Mono.just(testOrder));
        String emptyStatus = "";

        // When
        Mono<Order> result = orderService.updateOrderStatus(testOrder.getId(), emptyStatus);

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof IllegalArgumentException);
                assertEquals("Status is required", e.getMessage());
            });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingOrderWithNullStatus() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Mono.just(testOrder));
        String nullStatus = null;

        // When
        Mono<Order> result = orderService.updateOrderStatus(testOrder.getId(), nullStatus);

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof IllegalArgumentException);
                assertEquals("Status is required", e.getMessage());
            });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInvalidStatusFormat() {
        // Given
        String invalidStatus = "invalid-status-format";
        when(orderRepository.findByStatus(invalidStatus)).thenReturn(Flux.error(new IllegalArgumentException("Invalid status format")));

        // When
        Flux<Order> result = orderService.getOrdersByStatus(invalidStatus);

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof NotFoundException);
                assertEquals("Invalid status format", e.getMessage());
            });
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDatabaseErrorFetchingOrders() {
        // Given
        when(orderRepository.findAll()).thenReturn(Flux.error(new RuntimeException("Database error")));

        // When
        Flux<Order> result = orderService.getAllOrders();

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof RuntimeException);
                assertEquals("Failed to fetch orders", e.getMessage());
            });
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDatabaseErrorCreatingOrder() {
        // Given
        Order newOrder = new Order();
        newOrder.setPizzas(List.of("1", "2"));
        newOrder.setStatus("pending");
        
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<Order> result = orderService.createOrder(newOrder);

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof RuntimeException);
                assertEquals("Failed to create order", e.getMessage());
            });
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDatabaseErrorUpdatingOrder() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Mono.just(testOrder));
        String newStatus = "preparing";
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<Order> result = orderService.updateOrderStatus(testOrder.getId(), newStatus);

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof RuntimeException);
                assertEquals("Failed to update order status", e.getMessage());
            });
    }

    @Test
    void shouldCancelExistingOrder() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Mono.just(testOrder));
        when(orderRepository.delete(any(Order.class))).thenReturn(Mono.empty());

        // When
        Mono<Void> result = orderService.cancelOrder(testOrder.getId());

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCancelingNonExistingOrder() {
        // Given
        when(orderRepository.findById("nonexistent")).thenReturn(Mono.empty());

        // When
        Mono<Void> result = orderService.cancelOrder("nonexistent");

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof NotFoundException);
                assertEquals("Order not found", e.getMessage());
            });
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCancelingOrderWithInvalidId() {
        // Given
        String invalidId = "invalid-id-format";
        when(orderRepository.findById(invalidId)).thenReturn(Mono.error(new IllegalArgumentException("Invalid ID format")));

        // When
        Mono<Void> result = orderService.cancelOrder(invalidId);

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof NotFoundException);
                assertEquals("Invalid order ID format: " + invalidId, e.getMessage());
            });
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDatabaseErrorCancelingOrder() {
        // Given
        when(orderRepository.findById(testOrder.getId())).thenReturn(Mono.just(testOrder));
        when(orderRepository.delete(any(Order.class))).thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<Void> result = orderService.cancelOrder(testOrder.getId());

        // Then
        StepVerifier.create(result)
            .verifyErrorSatisfies(e -> {
                assertTrue(e instanceof RuntimeException);
                assertEquals("Failed to cancel order", e.getMessage());
            });
    }
}