package com.cloudgov.pizzashop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "pizzas")
public class Pizza {
    @Id
    private String id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Toppings are required")
    @Size(min = 1, message = "At least one topping is required")
    private List<String> toppings;
    
    @NotNull(message = "Size options are required")
    @Size(min = 1, message = "At least one size option is required")
    private List<String> sizeOptions;
    
    @NotNull(message = "Price is required")
    private Double price;
}