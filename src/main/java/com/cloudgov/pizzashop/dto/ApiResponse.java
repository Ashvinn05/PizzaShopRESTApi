package com.cloudgov.pizzashop.model;

import lombok.Data;

/**
 * Represents a generic API response structure that can be used across the application.
 * This class encapsulates the response data along with success status and optional message.
 *
 * @param <T> The type of data being returned in the response
 */
@Data
public class ApiResponse<T> {
    /** The actual data being returned in the response */
    private T data;
    
    /** Indicates whether the request was successful or not */
    private boolean success;
    
    /** Optional message to provide additional context about the response */
    private String message;

    /**
     * Creates a successful response with the given data and default success message
     *
     * @param data The response data
     */
    public ApiResponse(T data) {
        this.data = data;
        this.success = true;
        this.message = "Request processed successfully";
    }

    /**
     * Creates a successful response with the given data and custom message
     *
     * @param data The response data
     * @param message Custom success message
     */
    public ApiResponse(T data, String message) {
        this.data = data;
        this.success = true;
        this.message = message;
    }

    /**
     * Creates an error response with the given error message
     *
     * @param errorMessage The error message to be included in the response
     */
    public ApiResponse(String errorMessage) {
        this.data = null;
        this.success = false;
        this.message = errorMessage;
    }

    /**
     * Creates a successful response with the given data and default success message
     *
     * @param data The response data
     * @return A new ApiResponse instance with success status
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    /**
     * Creates a successful response with the given data and custom message
     *
     * @param data The response data
     * @param message Custom success message
     * @return A new ApiResponse instance with success status
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message);
    }

    /**
     * Creates an error response with the given error message
     *
     * @param errorMessage The error message to be included in the response
     * @return A new ApiResponse instance with error status
     */
    public static <T> ApiResponse<T> error(String errorMessage) {
        return new ApiResponse<>(errorMessage);
    }
}