package com.ecommerce.exceptionhandler;

@SuppressWarnings("serial")
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }

}

