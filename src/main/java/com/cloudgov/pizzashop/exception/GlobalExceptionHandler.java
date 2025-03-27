package com.cloudgov.pizzashop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import java.util.stream.Collectors;
import com.cloudgov.pizzashop.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ServerWebExchange;

/**
 * Global exception handler for the application.
 * Handles various types of exceptions and provides consistent error responses.
 * Uses reactive Mono responses to maintain non-blocking behavior.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles NotFoundException exceptions.
     * Logs the error and returns a 404 Not Found response.
     * 
     * @param ex the NotFoundException that occurred
     * @param exchange the current ServerWebExchange
     * @return a Mono containing an ErrorResponse with 404 status
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleNotFound(NotFoundException ex, ServerWebExchange exchange) {
        log.error("[ERROR] {} - {}", exchange.getRequest().getPath(), ex.getMessage());
        log.debug("Stack trace:", ex);
        return Mono.just(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    /**
     * Handles IllegalArgumentException exceptions.
     * Logs the error and returns a 400 Bad Request response.
     * 
     * @param ex the IllegalArgumentException that occurred
     * @param exchange the current ServerWebExchange
     * @return a Mono containing an ErrorResponse with 400 status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleBadRequest(IllegalArgumentException ex, ServerWebExchange exchange) {
        log.error("[ERROR] {} - {}", exchange.getRequest().getPath(), ex.getMessage());
        log.debug("Stack trace:", ex);
        return Mono.just(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    /**
     * Handles validation errors (WebExchangeBindException).
     * Collects all field errors and returns a 400 Bad Request response.
     * 
     * @param ex the WebExchangeBindException that occurred
     * @param exchange the current ServerWebExchange
     * @return a Mono containing an ErrorResponse with 400 status and validation errors
     */
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleValidation(WebExchangeBindException ex, ServerWebExchange exchange) {
        String message = ex.getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        log.error("[ERROR] {} - Validation error: {}", exchange.getRequest().getPath(), message);
        log.debug("Stack trace:", ex);
        return Mono.just(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message));
    }

    /**
     * Handles all other exceptions.
     * Logs the error with full stack trace and returns a 500 Internal Server Error response.
     * 
     * @param ex the Exception that occurred
     * @param exchange the current ServerWebExchange
     * @return a Mono containing an ErrorResponse with 500 status
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleGeneric(Exception ex, ServerWebExchange exchange) {
        log.error("[ERROR] {} - Internal server error", exchange.getRequest().getPath(), ex);
        log.debug("Stack trace:", ex);
        
        String errorMessage = "An unexpected error occurred. Please try again later.";
        return Mono.just(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage));
    }
}