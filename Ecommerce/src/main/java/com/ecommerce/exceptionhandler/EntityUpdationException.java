package com.ecommerce.exceptionhandler;

@SuppressWarnings("serial")
public class EntityUpdationException extends RuntimeException{
    public EntityUpdationException(String message) {
        super(message);
    }

}
