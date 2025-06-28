package com.spil.shapeManagementApplication.controller;

import com.spil.shapeManagementApplication.dto.ResponseBean;
import com.spil.shapeManagementApplication.dto.ShapeRequestDTO;
import com.spil.shapeManagementApplication.dto.ShapeResponseDTO;
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
    public ResponseEntity<ResponseBean> updateShape(
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
    public ResponseEntity<ResponseBean> deleteShape(@PathVariable Long id) {
        log.info("Received request to delete shape with ID: {}", id);
        shapeService.deleteShape(id);
        log.info("Shape with ID: {} deleted successfully", id);
        return ResponseEntity.ok(
                new ResponseBean("00", "Shape deleted successfully", null)
        );
    }


}
