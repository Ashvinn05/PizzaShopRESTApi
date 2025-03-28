package com.cloudgov.pizzashop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a configuration entity stored in the MongoDB database.
 */
@Document(collection = "config")
@Data
public class Config {

    /**
     * Unique identifier for the configuration entity.
     */
    @Id
    private String id;

    /**
     * Key or name of the configuration property.
     */
    private String key;

    /**
     * Value of the configuration property.
     */
    private boolean value;

    /**
     * Default constructor for the Config class.
     */
    public Config() {}

    /**
     * Constructs a new Config instance with the specified key and value.
     * 
     * @param key   the key or name of the configuration property
     * @param value the value of the configuration property
     */
    public Config(String key, boolean value) {
        this.key = key;
        this.value = value;
    }
}