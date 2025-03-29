package com.cloudgov.pizzashop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents a customer order in the pizza shop system.
 * Stores information about the ordered pizzas, customer details, and order status.
 */
@Data
@Document(collection = "orders")
public class Order {
    /**
     * Unique identifier for the order.
     */
    @Id
    private String id;

    /**
     * List of pizza IDs that are part of this order.
     * Each ID must be a valid MongoDB ObjectId.
     */
    @NotNull(message = "List of pizzas is required")
    @Size(min = 1, message = "At least one pizza is required")
    private List<@Pattern(regexp = "^[0-9a-fA-F]{24}$", message = "Pizza ID must be a valid MongoDB ObjectId") String> pizzas;

    /**
     * Current status of the order.
     * Valid statuses are: pending, preparing, ready, delivered, cancelled.
     */
    @NotBlank(message = "Order status is required")
    @Pattern(regexp = "pending|preparing|ready|delivered|cancelled", message = "Status must be one of: pending, preparing, ready, delivered, cancelled")
    private String status;

    /**
     * Timestamp when the order was created.
     */
    @NotNull(message = "Timestamp is required")
    private Date timestamp;

    /**
     * Name of the customer who placed the order.
     * Maximum length: 100 characters.
     */
    @Size(max = 100, message = "Customer name must not exceed 100 characters")
    private String customerName;

    /**
     * Email address of the customer.
     */
    @Email(message = "Invalid email format")
    private String customerEmail;

    /**
     * Phone number of the customer.
     * Must be 10-15 digits, optionally starting with +.
     */
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be 10-15 digits, optionally starting with +")
    private String customerPhone;

    /**
     * Additional attributes associated with the order.
     * Values can be any text.
     */
    private Map<String, String> additionalAttributes;
}