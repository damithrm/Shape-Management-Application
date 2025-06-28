package com.spil.shapeManagementApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "circle_details")
public class CircleDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shape_id")
    private Shape shape;
}
