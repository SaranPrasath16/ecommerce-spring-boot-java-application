package com.ecommerce.exceptionhandler;

@SuppressWarnings("serial")
public class EntityDeletionException extends RuntimeException {
    public EntityDeletionException(String message) {
        super(message);
    }

}
