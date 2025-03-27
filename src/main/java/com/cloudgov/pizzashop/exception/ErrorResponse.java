package com.cloudgov.pizzashop.exception;

/**
 * Response object for error conditions.
 * Contains status code and error message for API error responses.
 * Used by the GlobalExceptionHandler to provide consistent error responses.
 */
public class ErrorResponse {
    /**
     * HTTP status code of the error.
     */
    private int status;

    /**
     * Error message describing the issue.
     */
    private String message;

    /**
     * Creates a new ErrorResponse with the specified status and message.
     * 
     * @param status the HTTP status code
     * @param message the error message
     */
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Gets the HTTP status code.
     * 
     * @return the status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Gets the error message.
     * 
     * @return the error message
     */
    public String getMessage() {
        return message;
    }
}