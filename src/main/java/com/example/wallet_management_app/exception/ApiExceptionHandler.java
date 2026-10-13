package com.example.wallet_management_app.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestControllerAdvice(
    basePackages = "com.example.wallet_management_app.controller.api"
)
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidException(
        MethodArgumentNotValidException exception
    ) {
        BindingResult result = exception.getBindingResult();

        Map<String, String> errors = new HashMap<>();

        List<FieldError> errorMessages = result.getFieldErrors();

        for (FieldError fieldError : errorMessages) {

            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();

            errors.put(field, message);
        }

        return ResponseEntity.badRequest().body(errors);
    }
}