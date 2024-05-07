package com.realestate.exceptions;

public class CannotDeleteResourceException extends RuntimeException{
    public CannotDeleteResourceException(String message) {
        super(message);
    }
}
