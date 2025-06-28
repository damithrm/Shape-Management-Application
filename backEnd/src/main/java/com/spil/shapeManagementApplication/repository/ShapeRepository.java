package com.spil.shapeManagementApplication.repository;

import com.spil.shapeManagementApplication.model.Shape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShapeRepository extends JpaRepository<Shape, Long> {

    boolean existsByName(String name);

    Optional<Shape> findByName(String name);
}
