package com.spil.shapeManagementApplication.service;

import com.spil.shapeManagementApplication.dto.PointDTO;
import com.spil.shapeManagementApplication.dto.ShapeRequestDTO;
import com.spil.shapeManagementApplication.dto.ShapeResponseDTO;
import com.spil.shapeManagementApplication.exception.HandleGeneralException;
import com.spil.shapeManagementApplication.exception.NoSuchShapeException;
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
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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
//    @Transactional(rollbackOn = Exception.class)
    public Shape createShape(ShapeRequestDTO dto) {
        try {
            if (shapeRepository.existsByName(dto.getName())) {
                throw new ShapeNameAlreadyExistsException("Shape name already exists");
            }

            Shape shape = Shape.builder()
                    .name(dto.getName())
                    .type(dto.getType())
                    .build();
            shape = shapeRepository.save(shape);

            if (dto.getType() == ShapeType.CIRCLE) {

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
    public List<ShapeResponseDTO> getAllShapes() {
        try {
            List<Shape> shapes = shapeRepository.findAll();
            List<ShapeResponseDTO> result = new ArrayList<>();

            for (Shape shape : shapes) {
                ShapeResponseDTO dto = new ShapeResponseDTO();
                dto.setShapeId(shape.getShapeId());
                dto.setName(shape.getName());
                dto.setType(shape.getType());

                // Now fetch details manually
                if (shape.getType() == ShapeType.CIRCLE) {
                    Optional<CircleDetails> circleDetails = circleRepository.findByShape_ShapeId(shape.getShapeId());
                    if (circleDetails.isPresent()) {
                        CircleDetails cd = circleDetails.get();
                        dto.setCenterX(cd.getCenterX());
                        dto.setCenterY(cd.getCenterY());
                        dto.setRadius(cd.getRadius());
                    }
                } else {
                    List<Vertex> vertices = vertexRepository.findByShape_ShapeId(shape.getShapeId());
                    dto.setVertices(vertices.stream()
                            .map(v -> new PointDTO(v.getX(), v.getY() , v.getPosition()))
                            .toList());
                }

                result.add(dto);
            }

            return result;
        } catch (Exception ex) {
            throw new HandleGeneralException(ex.getMessage());
        }
    }

    /**
     * Updates an existing shape using the provided details. The method ensures that the shape
     * is valid, updates its properties, and manages associated details based on its type (circle or polygon).
     * If the shape type changes, old details are removed and new ones are added accordingly.
     *
     * @param id the unique identifier of the shape to update
     * @param dto the data transfer object containing the updated properties of the shape, such as its name, type,
     *            and additional attributes (e.g., center and radius for circles, or vertices for polygons)
     * @return the updated Shape entity
     * @throws NoSuchShapeException if the shape with the given ID does not exist
     * @throws ShapeNameAlreadyExistsException if the new name provided in the dto already exists for another shape
     * @throws HandleGeneralException if any unexpected error occurs during the update process
     */
    @Transactional
    public Shape updateShape(Long id, ShapeRequestDTO dto) {
        try {
            Shape shape = shapeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchShapeException("Shape not found with ID: " + id));

            // Validate name uniqueness (only if changing)
            if (!shape.getName().equals(dto.getName()) && shapeRepository.existsByName(dto.getName())) {
                throw new ShapeNameAlreadyExistsException("Shape name already exists: " + dto.getName());
            }

            shape.setName(dto.getName());
            shape.setType(dto.getType());
            shape = shapeRepository.save(shape);

            // Remove old shape details
            circleRepository.deleteById(id);
            vertexRepository.deleteAllByShape_ShapeId(id);

            // Add new shape details
            if (dto.getType() == ShapeType.CIRCLE) {
                CircleDetails cd = new CircleDetails();
                cd.setShape(shape);
                cd.setCenterX(dto.getCenterX());
                cd.setCenterY(dto.getCenterY());
                cd.setRadius(dto.getRadius());
                circleRepository.save(cd);
            } else {
                int pos = 1;
                List<Vertex> vertices = new ArrayList<>();
                for (PointDTO p : dto.getVertices()) {
                    Vertex vertex = new Vertex();
                    vertex.setX(p.getX());
                    vertex.setY(p.getY());
                    vertex.setPosition(pos++);
                    vertex.setShape(shape);
                    vertices.add(vertex);
                }
                vertexRepository.saveAll(vertices);
            }

            return shape;
        } catch (NoSuchShapeException ex) {
            throw new HandleGeneralException(ex.getMessage());
        }
    }

    /**
     * Deletes a shape and its associated details (e.g., circle details or vertices) from the database
     * using the provided shape ID. If the shape does not exist, an exception is thrown.
     *
     * @param id the unique identifier of the shape to be deleted
     * @throws NoSuchShapeException if the shape with the given ID does not exist in the repository
     */
    @Transactional
    public void deleteShape(Long id) {
        Optional<Shape> shape = shapeRepository.findById(id);
        if (!shapeRepository.existsById(id)) {
            throw new NullPointerException("Shape not found with ID: " + id);
        }

        // Delete related data first
        circleRepository.deleteAllByShape_ShapeId(id);
        vertexRepository.deleteAllByShape_ShapeId(id);

        // Then delete the shape itself
        shapeRepository.deleteById(id);
    }


    /**
     * Finds and returns the IDs of shapes that are overlapping.
     * The method iterates through all the shapes fetched from the repository
     * and determines if any two shapes overlap. If an overlap is detected,
     * the IDs of the overlapping shapes are added to a set to ensure no duplicates.
     *
     * @return a set of IDs of the shapes that are overlapping
     */
    public Set<Long> findOverlappingShapeIds() {
        List<Shape> shapes = shapeRepository.findAll();
        Set<Long> overlappingIds = new HashSet<>();

        for (int i = 0; i < shapes.size(); i++) {
            for (int j = i + 1; j < shapes.size(); j++) {
                Shape s1 = shapes.get(i);
                Shape s2 = shapes.get(j);

                if (isOverlapping(s1, s2)) {
                    overlappingIds.add(s1.getShapeId());
                    overlappingIds.add(s2.getShapeId());
                }
            }
        }

        return overlappingIds;
    }


    private boolean isOverlapping(Shape s1, Shape s2) {
        if (s1.getType() == ShapeType.CIRCLE && s2.getType() == ShapeType.CIRCLE) {
            return circleOverlapsCircle(s1, s2);
        }

        if (s1.getType() == ShapeType.CIRCLE) {
            return circleOverlapsPolygon(s1, s2);
        }

        if (s2.getType() == ShapeType.CIRCLE) {
            return circleOverlapsPolygon(s2, s1);
        }

        return polygonOverlapsPolygon(s1, s2);
    }

    private boolean circleOverlapsCircle(Shape s1, Shape s2) {
        CircleDetails c1 = circleRepository.findById(s1.getShapeId()).orElse(null);
        CircleDetails c2 = circleRepository.findById(s2.getShapeId()).orElse(null);
        if (c1 == null || c2 == null) return false;

        double dx = c1.getCenterX() - c2.getCenterX();
        double dy = c1.getCenterY() - c2.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < (c1.getRadius() + c2.getRadius());
    }

    private boolean circleOverlapsPolygon(Shape circleShape, Shape polygonShape) {
        CircleDetails c = circleRepository.findById(circleShape.getShapeId()).orElse(null);
        List<Vertex> vertices = vertexRepository.findByShape_ShapeId(polygonShape.getShapeId());
        if (c == null || vertices.isEmpty()) return false;

        for (Vertex v : vertices) {
            double dx = c.getCenterX() - v.getX();
            double dy = c.getCenterY() - v.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < c.getRadius()) return true;
        }

        return false;
    }

    private boolean polygonOverlapsPolygon(Shape s1, Shape s2) {
        List<Vertex> v1 = vertexRepository.findByShape_ShapeId(s1.getShapeId());
        List<Vertex> v2 = vertexRepository.findByShape_ShapeId(s2.getShapeId());

        if (v1.isEmpty() || v2.isEmpty()) return false;

        double[] bb1 = getBoundingBox(v1);
        double[] bb2 = getBoundingBox(v2);

        return !(bb1[2] < bb2[0] || bb2[2] < bb1[0] ||
                bb1[3] < bb2[1] || bb2[3] < bb1[1]);
    }

    private double[] getBoundingBox(List<Vertex> vertices) {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (Vertex v : vertices) {
            minX = Math.min(minX, v.getX());
            minY = Math.min(minY, v.getY());
            maxX = Math.max(maxX, v.getX());
            maxY = Math.max(maxY, v.getY());
        }

        return new double[]{minX, minY, maxX, maxY};
    }

}
