package com.spil.shapeManagementApplication.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointDTO {
    @NotNull
    private Double x;
    @NotNull
    private Double y;

    private Integer position;
}
