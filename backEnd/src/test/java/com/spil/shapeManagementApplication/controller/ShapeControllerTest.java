package com.spil.shapeManagementApplication.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ShapeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String circleJson = """
        {
          "name": "TestShape",
          "type": "CIRCLE",
          "centerX": 50,
          "centerY": 50,
          "radius": 15
        }
        """;

    @Test
    void shouldCreateCircleShapeSuccessfully() throws Exception {
        String shapeJson = """
        {
          "name": "TestCircle",
          "type": "CIRCLE",
          "centerX": 100,
          "centerY": 100,
          "radius": 20
        }
        """;

        mockMvc.perform(post("/api/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestCircle"))
                .andExpect(jsonPath("$.type").value("CIRCLE"));
    }

    @Test
    void shouldReturnListOfShapes() throws Exception {
        String shapeJson = """
    {
      "name": "TestShape",
      "type": "CIRCLE",
      "centerX": 50,
      "centerY": 50,
      "radius": 15
    }
    """;

        mockMvc.perform(post("/api/shapes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value("00"));

        mockMvc.perform(get("/api/shapes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("00"))
                .andExpect(jsonPath("$.content[0].name").value("TestShape"))
                .andExpect(jsonPath("$.content[0].centerX").value(50.0))
                .andExpect(jsonPath("$.content[0].centerY").value(50.0))
                .andExpect(jsonPath("$.content[0].radius").value(15.0));
    }
}