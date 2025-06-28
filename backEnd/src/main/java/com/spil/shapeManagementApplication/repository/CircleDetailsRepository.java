package com.spil.shapeManagementApplication.repository;

import com.spil.shapeManagementApplication.model.CircleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CircleDetailsRepository extends JpaRepository<CircleDetails, Long> {

    Optional<CircleDetails> findByShape_ShapeId(Long shapeId);
}
