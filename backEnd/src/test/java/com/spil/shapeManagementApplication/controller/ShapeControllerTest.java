package com.spil.shapeManagementApplication.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ShapeControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}