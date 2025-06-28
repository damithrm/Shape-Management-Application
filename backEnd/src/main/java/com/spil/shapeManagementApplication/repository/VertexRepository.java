package com.spil.shapeManagementApplication.repository;

import com.spil.shapeManagementApplication.model.Vertex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VertexRepository extends JpaRepository<Vertex, Long> {
}
