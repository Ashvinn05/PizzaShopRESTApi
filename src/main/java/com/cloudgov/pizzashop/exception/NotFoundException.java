package com.cloudgov.pizzashop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when a requested resource is not found.
 * Extends Spring's ResponseStatusException with a 404 status code.
 * Used for handling cases where requested entities (like pizzas or orders) are not found.
 */
public class NotFoundException extends ResponseStatusException {
    /**
     * Creates a new NotFoundException with the specified message.
     * Automatically sets the HTTP status code to 404 NOT_FOUND.
     * 
     * @param message the error message describing what was not found
     */
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}