/**
 * Represents a customer order in the pizza shop system.
 * Each order contains a list of pizzas, their status, and customer information.
 * Orders are stored in MongoDB with the collection name "orders".
 * 
 * Validation constraints:
 * - pizzas: Must contain at least one pizza (handled by GlobalExceptionHandler)
 * - status: Must not be blank (handled by GlobalExceptionHandler)
 * - timestamp: Must not be null (handled by GlobalExceptionHandler)
 * @see com.cloudgov.pizzashop.exception.GlobalExceptionHandler
 */
package com.cloudgov.pizzashop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Data
@Document(collection = "orders")
public class Order {
    /**
     * Unique identifier for the order, automatically generated by MongoDB.
     */
    @Id
    private String id;
    
    /**
     * List of pizza IDs included in this order.
     * Must contain at least one pizza.
     * Validation error: "At least one pizza is required"
     */
    @NotNull(message = "List of pizzas is required")
    @Size(min = 1, message = "At least one pizza is required")
    private List<String> pizzas;
    
    /**
     * Current status of the order (e.g., "pending", "preparing", "ready", "delivered").
     * Must not be blank.
     * Validation error: "Order status is required"
     */
    @NotBlank(message = "Order status is required")
    private String status;
    
    /**
     * Timestamp when the order was created.
     * Must not be null.
     * Validation error: "Timestamp is required"
     */
    @NotNull(message = "Timestamp is required")
    private Date timestamp;
    
    /**
     * Name of the customer placing the order.
     * Optional field.
     */
    private String customerName;
    
    /**
     * Email address of the customer.
     * Optional field.
     */
    private String customerEmail;
    
    /**
     * Phone number of the customer.
     * Optional field.
     */
    private String customerPhone;
    
    /**
     * Additional attributes that can be stored with the order.
     * Can be used for custom metadata or future extensions.
     */
    private Map<String, Object> additionalAttributes;
}