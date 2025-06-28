package com.spil.shapeManagementApplication.exception;

public class shapeNameAlreadyExistsException extends RuntimeException {
    public shapeNameAlreadyExistsException(String message) {
        super(message);
    }
}
