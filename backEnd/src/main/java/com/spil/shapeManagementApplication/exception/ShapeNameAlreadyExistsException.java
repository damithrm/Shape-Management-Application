package com.spil.shapeManagementApplication.exception;

public class ShapeNameAlreadyExistsException extends RuntimeException {
    public ShapeNameAlreadyExistsException(String message) {
        super(message);
    }
}
