package com.spil.shapeManagementApplication.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Base64;

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
      "name": "TestCircle5",
      "type": "CIRCLE",
      "centerX": 100,
      "centerY": 100,
      "radius": 20
    }
    """;

        String username = "admin";
        String password = "admin";
        String basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        mockMvc.perform(post("/api/shapes")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeJson))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value("00"));
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
        String username = "admin";
        String password = "admin";
        String basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        // Create a shape first
        mockMvc.perform(post("/api/shapes")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value("00"));

        mockMvc.perform(get("/api/shapes")
                        .header("Authorization", basicAuthHeader)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("00"))
                .andExpect(jsonPath("$.content[0].name").value("TestShape"))
                .andExpect(jsonPath("$.content[0].centerX").value(50.0))
                .andExpect(jsonPath("$.content[0].centerY").value(50.0))
                .andExpect(jsonPath("$.content[0].radius").value(15.0));
    }

    @Test
    void shouldUpdateShapeSuccessfully() throws Exception {
        String postJson = """
    {
      "name": "ToBeUpdated",
      "type": "CIRCLE",
      "centerX": 100,
      "centerY": 100,
      "radius": 20
    }
    """;
        String username = "admin";
        String password = "admin";
        String basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    // Step 1: Create a shape first
        MvcResult result = mockMvc.perform(post("/api/shapes")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Long shapeId = JsonPath.read(responseBody, "$.content.shapeId");

        String putJson = """
    {
      "name": "ToBeUpdatedModified",
      "type": "CIRCLE",
      "centerX": 120,
      "centerY": 130,
      "radius": 25
    }
    """;
        // Step 2: Update the shape
        mockMvc.perform(put("/api/shapes/{id}", shapeId)
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(putJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("00"))
                .andExpect(jsonPath("$.content.name").value("ToBeUpdatedModified"))
                .andExpect(jsonPath("$.content.type").value("CIRCLE"));
    }

    @Test
    void shouldDeleteShapeSuccessfully() throws Exception {
        // Step 1: Create a shape first
        String shapeJson = """
    {
      "name": "ToBeDeleted",
      "type": "CIRCLE",
      "centerX": 60,
      "centerY": 60,
      "radius": 10
    }
    """;
        String username = "admin";
        String password = "admin";
        String basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        MvcResult result = mockMvc.perform(post("/api/shapes")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeJson))
                .andExpect(status().isCreated())
                .andReturn();

        //  Extract the shapeId from the response JSON
        String responseBody = result.getResponse().getContentAsString();
        Long shapeId = JsonPath.read(responseBody, "$.content.shapeId");

        //  Delete the shape
        mockMvc.perform(delete("/api/shapes/{id}", shapeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("00"))
                .andExpect(jsonPath("$.responseMsg").value("Shape deleted successfully"));

        //  Ensure it's actually deleted by checking shape list
        mockMvc.perform(get("/api/shapes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.shapeId == %s)]", shapeId).doesNotExist());
    }

    @Test
    void shouldDetectOverlappingShapes() throws Exception {
        String circle1 = """
    {
      "name": "CircleA",
      "type": "CIRCLE",
      "centerX": 100,
      "centerY": 100,
      "radius": 30
    }
    """;

        String circle2 = """
    {
      "name": "CircleB",
      "type": "CIRCLE",
      "centerX": 120,
      "centerY": 100,
      "radius": 30
    }
    """;
        String username = "admin";
        String password = "admin";
        String basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        mockMvc.perform(post("/api/shapes")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circle1))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/shapes")
                        .header("Authorization", basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(circle2))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/shapes/overlaps")
                        .header("Authorization", basicAuthHeader)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value("00"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }
}