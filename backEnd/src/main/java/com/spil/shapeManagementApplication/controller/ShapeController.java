package com.spil.shapeManagementApplication.controller;

import com.spil.shapeManagementApplication.dto.ShapeRequestDTO;
import com.spil.shapeManagementApplication.model.Shape;
import com.spil.shapeManagementApplication.service.ShapeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/shapes")
@RequiredArgsConstructor
public class ShapeController {

    private final ShapeService shapeService;

    @PostMapping
    public ResponseEntity<Shape> createShape(@Valid @RequestBody ShapeRequestDTO dto) {
        log.info("Received request to create a new shape");
        Shape shape = shapeService.createShape(dto);
        log.info("Shape created successfully");
        return ResponseEntity.status(201).body(shape);
    }
}
