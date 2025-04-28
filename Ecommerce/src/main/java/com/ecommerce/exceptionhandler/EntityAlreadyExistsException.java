package com.ecommerce.exceptionhandler;

@SuppressWarnings("serial")
public class EntityAlreadyExistsException extends RuntimeException{
    public EntityAlreadyExistsException(String message) {
        super(message);
    }

}
