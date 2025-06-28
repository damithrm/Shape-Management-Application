package com.spil.shapeManagementApplication.exception;

import com.spil.shapeManagementApplication.dto.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(
                new ResponseBean("01", "Validation failed", errors)
        );
    }

    @ExceptionHandler(ShapeNameAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(ShapeNameAlreadyExistsException ex) {
        // Log the exception message
        log.warn("Shape name already Exists: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("massage", ex.getMessage());
        return ResponseEntity.badRequest().body(
                new ResponseBean("01", ex.getMessage(), null)
        );
    }

    @ExceptionHandler(NoSuchShapeException.class)
    public ResponseEntity<?> handleNoSuchShapeException(ShapeNameAlreadyExistsException ex) {
        // Log the exception message
        log.warn("Shape Id not exists: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("massage", ex.getMessage());
        return ResponseEntity.badRequest().body(
                new ResponseBean("01", ex.getMessage(), null)
        );
    }

    @ExceptionHandler(HandleGeneralException.class)
    public ResponseEntity<ResponseBean> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        Map<String, String> error = new HashMap<>();
        error.put("massage", ex.getMessage());
        return ResponseEntity.internalServerError().body(
                new ResponseBean("01", ex.getMessage(), null)
        );
    }
}
