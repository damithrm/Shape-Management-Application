package com.spil.shapeManagementApplication.controller;

import com.spil.shapeManagementApplication.dto.ResponseBean;
import com.spil.shapeManagementApplication.dto.ShapeRequestDTO;
import com.spil.shapeManagementApplication.model.Shape;
import com.spil.shapeManagementApplication.service.ShapeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/shapes")
@RequiredArgsConstructor
public class ShapeController {

    private final ShapeService shapeService;

    @PostMapping
    public ResponseEntity<?> createShape(@Valid @RequestBody ShapeRequestDTO dto) {
        log.info("Received request to create a new shape");
        Shape shape = shapeService.createShape(dto);
        log.info("Shape created successfully");
        return ResponseEntity.ok(
                new ResponseBean("00", "Shape created successfully", shape)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllShapes() {
        log.info("Received request to get all shapes");
        List<Shape> shapes = shapeService.getAllShapes();
        log.info("Shapes retrieved successfully");
        return ResponseEntity.ok(
                new ResponseBean("00", "Shape list fetched successfully", shapes)
        );
    }
}
