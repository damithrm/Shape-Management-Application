package com.spil.shapeManagementApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vertices")
public class Vertex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vertexId;

    @NotNull
    private Double x;

    @NotNull
    private Double y;

    private int position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shape_id", nullable = false)
    private Shape shape;
}
