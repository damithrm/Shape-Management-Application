package com.spil.shapeManagementApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "shapes")
public class Shape {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shapeId;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShapeType type;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "shape", cascade = CascadeType.ALL)
    private CircleDetails circleDetails;

    @OneToMany(mappedBy = "shape", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vertex> vertices = new ArrayList<>();
}
