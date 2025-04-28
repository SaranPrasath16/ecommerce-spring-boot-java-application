package com.ecommerce.exceptionhandler;

@SuppressWarnings("serial")
public class EntityPushException extends RuntimeException{
    public EntityPushException(String message) {
        super(message);
    }
}
