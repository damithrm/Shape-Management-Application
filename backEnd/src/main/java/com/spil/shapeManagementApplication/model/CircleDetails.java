package com.spil.shapeManagementApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
@Table(name = "circle_details")
public class CircleDetails {

    @Id
    private Long shapeId;

    @Positive
    @NotNull
    private double centerX;

    @Positive
    @NotNull
    private double centerY;

    @Positive
    @NotNull
    private double radius;

    @OneToOne
    @MapsId
    @JoinColumn(name = "shape_id")
    private Shape shape;
}
