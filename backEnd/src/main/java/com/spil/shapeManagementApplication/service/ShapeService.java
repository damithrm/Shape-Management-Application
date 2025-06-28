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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShapeService {

    private final ShapeRepository shapeRepository;
    private final CircleDetailsRepository circleRepository;
    private final VertexRepository vertexRepository;

    /**
     * Creates and persists a new shape along with its corresponding properties based on the provided input.
     * Supports creation of circles with center and radius, or polygonal shapes with vertices.
     *
     * @param dto the data transfer object containing the details required to create the shape,
     *            such as its name, type, and relevant properties (e.g., vertices or circle details)
     * @return the newly created Shape entity
     * @throws ShapeNameAlreadyExistsException if a shape with the same name already exists in the repository
     * @throws HandleGeneralException         if an unexpected error occurs during shape creation
     */
    @Transactional(rollbackOn = Exception.class)
    public Shape createShape(ShapeRequestDTO dto) {
        try {
            if (shapeRepository.existsByName(dto.getName())) {
                throw new ShapeNameAlreadyExistsException("Shape name already exists");
            }

//            Shape shape = new Shape();
//            shape.setName(dto.getName());
//            shape.setType(dto.getType());
            Shape shape = Shape.builder()
                    .name(dto.getName())
                    .type(dto.getType())
                    .build();
            //shape.setCreatedAt(System.currentTimeMillis());
            shape = shapeRepository.save(shape);

            if (dto.getType() == ShapeType.CIRCLE) {
//                CircleDetails details = new CircleDetails();
//                details.setShape(shape);
//                details.setCenterX(dto.getCenterX());
//                details.setCenterY(dto.getCenterY());
//                details.setRadius(dto.getRadius());
                CircleDetails details = CircleDetails.builder()
                        .shape(shape)
                        .centerX(dto.getCenterX())
                        .centerY(dto.getCenterY())
                        .radius(dto.getRadius())
                        .build();
                // Save circle details
                circleRepository.save(details);
            } else {
                List<Vertex> vertexList = new ArrayList<>();
                int pos = 1;
                for (PointDTO point : dto.getVertices()) {
//                    Vertex v = new Vertex();
//                    v.setShape(shape);
//                    v.setX(point.getX());
//                    v.setY(point.getY());
//                    v.setPosition(pos++);
                Vertex v =Vertex.builder()
                        .shape(shape)
                        .x(point.getX())
                        .y(point.getY())
                        .position(pos++)
                        .build();
                    vertexList.add(v);
                }
                // Save all vertices associated with the shape
                vertexRepository.saveAll(vertexList);
            }
            return shape;
        } catch (Exception ex) {
            throw new HandleGeneralException(ex.getMessage());
        }
    }

    /**
     * Retrieves a list of all shapes stored in the repository.
     *
     * @return a list of Shape objects representing all the shapes in the repository
     * @throws HandleGeneralException if an unexpected error occurs during the retrieval process
     */
    public List<Shape> getAllShapes() {
        List<Shape> shapes = null;
        try {
            shapes = shapeRepository.findAll();
        } catch (Exception ex) {
            throw new HandleGeneralException(ex.getMessage());
        }

        return shapes;
    }
}
