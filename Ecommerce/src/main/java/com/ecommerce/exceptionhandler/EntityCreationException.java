package com.ecommerce.exceptionhandler;

@SuppressWarnings("serial")
public class EntityCreationException  extends RuntimeException{
    public EntityCreationException(String message) {
        super(message);
    }

}
