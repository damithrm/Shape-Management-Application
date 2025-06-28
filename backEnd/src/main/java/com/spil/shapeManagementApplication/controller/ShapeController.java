package com.spil.shapeManagementApplication.controller;

import com.spil.shapeManagementApplication.dto.ResponseBean;
import com.spil.shapeManagementApplication.dto.ShapeRequestDTO;
import com.spil.shapeManagementApplication.dto.ShapeResponseDTO;
import com.spil.shapeManagementApplication.model.Shape;
import com.spil.shapeManagementApplication.service.ShapeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@Tag(name = "Shape", description = "APIs for managing shapes")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/shapes")
@RequiredArgsConstructor
public class ShapeController {

    private final ShapeService shapeService;

    @PostMapping
    @Operation(summary = "Create a new shape", description = "Creates a new shape with the provided details")
    public ResponseEntity<?> createShape(@Valid @RequestBody ShapeRequestDTO dto) {
        log.info("Received request to create a new shape");
        Shape shape = shapeService.createShape(dto);
        log.info("Shape created successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseBean.builder()
                                .responseCode("00")
                                .responseMessage("Shape created successfully")
                                .content(shape)
                                .build()
                );
    }

    @GetMapping
    @Operation(summary = "Get all shapes", description = "Fetches a list of all shapes")
    public ResponseEntity<?> getAllShapes() {
        log.info("Received request to get all shapes");
        List<ShapeResponseDTO> dtoList = shapeService.getAllShapes();
        log.info("Shapes retrieved successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseBean.builder()
                                .responseCode("00")
                                .responseMessage("Shape list fetched successfully")
                                .content(dtoList)
                                .build()
                );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing shape", description = "Updates the shape with the given ID using the provided details")
    public ResponseEntity<?> updateShape(
            @PathVariable Long id,
            @Valid @RequestBody ShapeRequestDTO dto
    ) {
        log.info("Received request to update shape with ID: {}", id);
        Shape updatedShape = shapeService.updateShape(id, dto);
        log.info("Shape with ID: {} updated successfully", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseBean.builder()
                                .responseCode("00")
                                .responseMessage("Shape updated successfully")
                                .content(updatedShape)
                                .build()
                );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a shape", description = "Deletes the shape with the given ID")
    public ResponseEntity<?> deleteShape(@PathVariable Long id) {
        log.info("Received request to delete shape with ID: {}", id);
        shapeService.deleteShape(id);
        log.info("Shape with ID: {} deleted successfully", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseBean.builder()
                                .responseCode("00")
                                .responseMessage("Shape updated successfully")
                                .content(null)
                                .build()
                );
    }

    @GetMapping("/overlaps")
    @Operation(summary = "Get overlapping shapes", description = "Fetches IDs of shapes that overlap with each other")
    public ResponseEntity<?> getOverlappingShapes() {
        log.info("Received request to get overlapping shapes");
        Set<Long> overlaps = shapeService.findOverlappingShapeIds();
        log.info("Shapes overlaps retrieved successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseBean.builder()
                                .responseCode("00")
                                .responseMessage("Overlaps Checked Successfully")
                                .content(overlaps)
                                .build()
                );
    }



}
