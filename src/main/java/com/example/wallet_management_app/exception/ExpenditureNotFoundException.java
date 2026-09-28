package com.example.wallet_management_app.exception;

public class ExpenditureNotFoundException extends RuntimeException {

    public ExpenditureNotFoundException(String message) {
        super(message);
    }
}