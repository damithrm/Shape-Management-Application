package com.spil.shapeManagementApplication.dto;

import com.spil.shapeManagementApplication.model.ShapeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShapeRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private ShapeType type;

    // For circle
    private Double centerX;
    private Double centerY;

    @Positive
    private Double radius;

    @Size(min = 3, message = "At least 3 vertices required")
    // For other shapes
    private List<PointDTO> vertices;
}
