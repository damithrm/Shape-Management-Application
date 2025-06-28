package com.spil.shapeManagementApplication.repository;

import com.spil.shapeManagementApplication.model.Shape;
import com.spil.shapeManagementApplication.model.Vertex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VertexRepository extends JpaRepository<Vertex, Long> {


    List<Vertex> findByShape_ShapeId(Long shape);

    void deleteAllByShape_ShapeId(Long id);
}
