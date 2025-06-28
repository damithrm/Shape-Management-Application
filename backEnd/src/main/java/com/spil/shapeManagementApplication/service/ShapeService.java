package com.spil.shapeManagementApplication.service;

import com.spil.shapeManagementApplication.dto.PointDTO;
import com.spil.shapeManagementApplication.dto.ShapeRequestDTO;
import com.spil.shapeManagementApplication.exception.shapeNameAlreadyExistsException;
import com.spil.shapeManagementApplication.model.CircleDetails;
import com.spil.shapeManagementApplication.model.Shape;
import com.spil.shapeManagementApplication.model.ShapeType;
import com.spil.shapeManagementApplication.model.Vertex;
import com.spil.shapeManagementApplication.repository.CircleDetailsRepository;
import com.spil.shapeManagementApplication.repository.ShapeRepository;
import com.spil.shapeManagementApplication.repository.VertexRepository;
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

    public Shape createShape(ShapeRequestDTO dto) {

        if (shapeRepository.existsByName(dto.getName())) {
            throw new shapeNameAlreadyExistsException("Shape name already exists");
        }

        Shape shape = new Shape();
        shape.setName(dto.getName());
        shape.setType(dto.getType());

        shape = shapeRepository.save(shape);

        if (dto.getType() == ShapeType.CIRCLE) {
            CircleDetails details = new CircleDetails();
            details.setShape(shape);
            details.setCenterX(dto.getCenterX());
            details.setCenterY(dto.getCenterY());
            details.setRadius(dto.getRadius());
            circleRepository.save(details);
        } else {
            List<Vertex> vertexList = new ArrayList<>();
            int pos = 1;
            for (PointDTO point : dto.getVertices()) {
                Vertex v = new Vertex();
                v.setShape(shape);
                v.setX(point.getX());
                v.setY(point.getY());
                v.setPosition(pos++);
                vertexList.add(v);
            }
            vertexRepository.saveAll(vertexList);
        }

        return shape;
    }
}
