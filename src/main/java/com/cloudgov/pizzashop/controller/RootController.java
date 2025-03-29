package com.cloudgov.pizzashop.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.cloudgov.pizzashop.model.ApiResponse;

/**
 * Controller for root endpoints, providing access to API documentation and endpoints.
 */
@RestController
@RequestMapping("/")
public class RootController {
    private static final String BASE_URL = "/";

    /**
     * Retrieves the list of available API endpoints.
     * Returns a Mono containing ApiResponse with the endpoints information.
     * 
     * @return Mono containing ApiResponse with endpoints information
     */
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

    /**
     * Response object containing API endpoints information.
     */
    private static class EndpointsResponse {
        private final String baseUrl;
        private final Endpoints pizzas;
        private final Endpoints orders;

        /**
         * Constructs an EndpointsResponse object.
         * 
         * @param baseUrl Base URL of the API
         * @param pizzas Endpoints for pizzas management
         * @param orders Endpoints for orders management
         */
        public EndpointsResponse(String baseUrl, Endpoints pizzas, Endpoints orders) {
            this.baseUrl = baseUrl;
            this.pizzas = pizzas;
            this.orders = orders;
        }

        // Getters
        /**
         * Returns the base URL of the API.
         * 
         * @return Base URL of the API
         */
        public String getBaseUrl() { return baseUrl; }

        /**
         * Returns the endpoints for pizzas management.
         * 
         * @return Endpoints for pizzas management
         */
        public Endpoints getPizzas() { return pizzas; }

        /**
         * Returns the endpoints for orders management.
         * 
         * @return Endpoints for orders management
         */
        public Endpoints getOrders() { return orders; }
    }

    /**
     * Represents a category of API endpoints.
     */
    private static class Endpoints {
        private final String category;
        private final String baseEndpoint;
        private final String[] availableEndpoints;

        /**
         * Constructs an Endpoints object.
         * 
         * @param category Category of the endpoints (e.g. "Pizzas Management")
         * @param baseEndpoint Base endpoint for the category
         * @param availableEndpoints List of available endpoints for the category
         */
        public Endpoints(String category, String baseEndpoint, String[] availableEndpoints) {
            this.category = category;
            this.baseEndpoint = baseEndpoint;
            this.availableEndpoints = availableEndpoints;
        }

        // Getters
        /**
         * Returns the category of the endpoints.
         * 
         * @return Category of the endpoints
         */
        public String getCategory() { return category; }

        /**
         * Returns the base endpoint for the category.
         * 
         * @return Base endpoint for the category
         */
        public String getBaseEndpoint() { return baseEndpoint; }

        /**
         * Returns the list of available endpoints for the category.
         * 
         * @return List of available endpoints for the category
         */
        public String[] getAvailableEndpoints() { return availableEndpoints; }
    }
}