package com.ecommerce.exceptionhandler;

@SuppressWarnings("serial")
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

}
