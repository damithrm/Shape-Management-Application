package com.spil.shapeManagementApplication.service;

import com.spil.shapeManagementApplication.dto.PointDTO;
import com.spil.shapeManagementApplication.dto.ShapeRequestDTO;
import com.spil.shapeManagementApplication.exception.HandleGeneralException;
import com.spil.shapeManagementApplication.exception.ShapeNameAlreadyExistsException;
import com.spil.shapeManagementApplication.model.CircleDetails;
import com.spil.shapeManagementApplication.model.Shape;
import com.spil.shapeManagementApplication.model.ShapeType;
import com.spil.shapeManagementApplication.model.Vertex;
import com.spil.shapeManagementApplication.repository.CircleDetailsRepository;
import com.spil.shapeManagementApplication.repository.ShapeRepository;
import com.spil.shapeManagementApplication.repository.VertexRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShapeService {

    private final ShapeRepository shapeRepository;
    private final CircleDetailsRepository circleRepository;
    private final VertexRepository vertexRepository;

    @Transactional
    public Shape createShape(ShapeRequestDTO dto) {
        try {
            if (shapeRepository.existsByName(dto.getName())) {
                throw new ShapeNameAlreadyExistsException("Shape name already exists");
            }

            Shape shape = Shape.builder()
                    .name(dto.getName())
                    .type(dto.getType())
                    .build();
            // Save the shape first to generate an ID
            shape = shapeRepository.save(shape);

            if (dto.getType() == ShapeType.CIRCLE) {
                CircleDetails details = CircleDetails.builder()
                        .shape(shape)
                        .centerX(dto.getCenterX())
                        .centerX(dto.getCenterX())
                        .radius(dto.getRadius())
                        .build();
                // Save the circle details
                circleRepository.save(details);
            } else {
                List<Vertex> vertexList = new ArrayList<>();
                int pos = 1;
                for (PointDTO point : dto.getVertices()) {
                    Vertex v = Vertex.builder()
                            .shape(shape)
                            .x(point.getX())
                            .y(point.getY())
                            .position(pos++)
                            .build();
                    vertexList.add(v);
                }
                // Save all vertices
                vertexRepository.saveAll(vertexList);
            }
            return shape;
        }catch (Exception ex){
            throw new HandleGeneralException(ex.getMessage());
        }
    }
}
