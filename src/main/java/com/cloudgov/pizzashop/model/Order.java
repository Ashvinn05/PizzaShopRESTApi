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
    @Id
    private String id;
    
    @NotNull(message = "List of pizzas is required")
    @Size(min = 1, message = "At least one pizza is required")
    private List<String> pizzas;
    
    @NotBlank(message = "Order status is required")
    private String status;
    
    @NotNull(message = "Timestamp is required")
    private Date timestamp;
    
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Map<String, Object> additionalAttributes;
}