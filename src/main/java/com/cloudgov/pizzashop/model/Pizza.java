package com.cloudgov.pizzashop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * Represents a pizza in the pizza shop inventory.
 * Contains information about the pizza's characteristics, ingredients, and pricing.
 */
@Data
@Document(collection = "pizzas")
public class Pizza {
    /**
     * Unique identifier for the pizza.
     */
    @Id
    private String id;

    /**
     * Name of the pizza.
     * Maximum length: 100 characters.
     */
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    /**
     * Description of the pizza.
     * Maximum length: 500 characters.
     */
    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 500, message = "Description must be between 1 and 500 characters")
    private String description;

    /**
     * List of toppings available for this pizza.
     * Each topping must be between 1 and 50 characters.
     */
    @NotNull(message = "Toppings are required")
    @Size(min = 1, message = "At least one topping is required")
    private List<@Size(max = 50, message = "Topping must not exceed 50 characters") String> toppings;

    /**
     * List of available size options for this pizza.
     * Valid sizes are: small, medium, or large.
     */
    @NotNull(message = "Size options are required")
    @Size(min = 1, message = "At least one size option is required")
    private List<@Pattern(regexp = "small|medium|large", message = "Size must be small, medium, or large") String> sizeOptions;

    /**
     * Price of the pizza.
     * Must be at least 0.01.
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    private Double price;
}