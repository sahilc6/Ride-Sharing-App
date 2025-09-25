package com.ridesharing.ride_sharing_backend.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ridesharing.ride_sharing_backend.dto.MatchingResponseDTO;

/**
 * Global exception handler for the application
 * Handles various types of exceptions and returns appropriate HTTP responses
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handle validation errors from request body validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MatchingResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn("Validation error occurred: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        String errorMessage = "Validation failed: " + errors.toString();
        MatchingResponseDTO response = new MatchingResponseDTO(
            new ArrayList<>(), 0, "VALIDATION_ERROR", errorMessage
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MatchingResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Invalid argument provided: {}", ex.getMessage());
        
        MatchingResponseDTO response = new MatchingResponseDTO(
            new ArrayList<>(), 0, "INVALID_INPUT", ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handle generic exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MatchingResponseDTO> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        
        MatchingResponseDTO response = new MatchingResponseDTO(
            new ArrayList<>(), 0, "INTERNAL_ERROR", 
            "An unexpected error occurred: " + ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

