package com.cloudgov.pizzashop.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.cloudgov.pizzashop.model.ApiResponse;

@RestController
@RequestMapping("/")
public class RootController {
    private static final String BASE_URL = "/";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<EndpointsResponse>> getEndpoints() {
        EndpointsResponse response = new EndpointsResponse(
            BASE_URL,
            new Endpoints(
                "Pizzas Management",
                BASE_URL + "pizzas",
                new String[]{
                    "GET /pizzas - Get all pizzas",
                    "GET /pizzas/{id} - Get pizza by ID",
                    "POST /pizzas - Create new pizza",
                    "PUT /pizzas/{id} - Update pizza",
                    "DELETE /pizzas/{id} - Delete pizza"
                }
            ),
            new Endpoints(
                "Orders Management",
                BASE_URL + "orders",
                new String[]{
                    "GET /orders - Get all orders",
                    "GET /orders/{status} - Get orders by status",
                    "GET /orders/{id} - Get order by ID",
                    "POST /orders - Create new order",
                    "PUT /orders/{id}/status - Update order status",
                    "DELETE /orders/{id} - Cancel order"
                }
            )
        );
        return Mono.just(ApiResponse.success(response));
    }

    private static class EndpointsResponse {
        private final String baseUrl;
        private final Endpoints pizzas;
        private final Endpoints orders;

        public EndpointsResponse(String baseUrl, Endpoints pizzas, Endpoints orders) {
            this.baseUrl = baseUrl;
            this.pizzas = pizzas;
            this.orders = orders;
        }

        // Getters
        public String getBaseUrl() { return baseUrl; }
        public Endpoints getPizzas() { return pizzas; }
        public Endpoints getOrders() { return orders; }
    }

    private static class Endpoints {
        private final String category;
        private final String baseEndpoint;
        private final String[] availableEndpoints;

        public Endpoints(String category, String baseEndpoint, String[] availableEndpoints) {
            this.category = category;
            this.baseEndpoint = baseEndpoint;
            this.availableEndpoints = availableEndpoints;
        }

        // Getters
        public String getCategory() { return category; }
        public String getBaseEndpoint() { return baseEndpoint; }
        public String[] getAvailableEndpoints() { return availableEndpoints; }
    }
}