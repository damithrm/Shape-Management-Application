package com.spil.shapeManagementApplication.dto;

import com.spil.shapeManagementApplication.model.ShapeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShapeResponseDTO {
    private Long shapeId;
    private String name;
    private ShapeType type;

    // For circle
    private Double centerX;
    private Double centerY;
    private Double radius;

    // For polygon shapes
    private List<PointDTO> vertices;
}
