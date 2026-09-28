package com.example.wallet_management_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ExpenditureNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleExpenditureNotFound(ExpenditureNotFoundException ex) {
    }
}