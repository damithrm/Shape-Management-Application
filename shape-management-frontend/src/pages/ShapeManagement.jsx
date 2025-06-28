import React, { useEffect, useState } from "react";
import ShapeForm from "../components/ShapeForm";
import ShapeList from "../components/ShapeList";
import {
  getShapes,
  createShape,
  updateShape,
  deleteShape,
} from "../services/shapeApi";

import Container from "@mui/material/Container";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import { useNotification } from "../context/NotificationProvider";

export default function ShapeManagement() {
  const [shapes, setShapes] = useState([]);
  const [editingShape, setEditingShape] = useState(null);
  const { showSuccess, showWarning, showInfo, showError } = useNotification();

  const fetchShapes = async () => {
    const res = await getShapes();
    setShapes(res.data.content);
  };

  useEffect(() => {
    fetchShapes();
  }, []);

  const handleSubmit = async (shape) => {
    if (editingShape) {
      const result = await updateShape(editingShape.shapeId, shape);
      if (result?.data?.responseCode === "03") {
        showWarning(result?.data?.responseMessage, {
          autoHideDuration: 2000,
        });
      } else if (result?.data?.responseCode === "00") {
        showSuccess(result?.data?.responseMessage, {
          autoHideDuration: 2000,
        });
      } else {
        showError(result?.data?.responseMessage, {
          autoHideDuration: 2000,
        });
      }
      setEditingShape(null);
    } else {
      const result = await createShape(shape);
      if (result?.data?.responseCode === "03") {
        showWarning(result?.data?.responseMessage, {
          autoHideDuration: 2000,
        });
      } else if (result?.data?.responseCode === "00") {
        showSuccess(result?.data?.responseMessage, {
          autoHideDuration: 2000,
        });
      } else {
        showError(result?.data?.responseMessage, {
          autoHideDuration: 2000,
        });
      }
    }
    fetchShapes();
  };

  const handleDelete = async (id) => {
    await deleteShape(id);
    fetchShapes();
  };

  return (
    <Container maxWidth="md" sx={{ mt: 6, mb: 6 }}>
      <Typography variant="h4" gutterBottom className="font-bold text-center">
        Shape Management
      </Typography>

      {/* <Grid
        container
        spacing={4}
        justifyContent="center"
        alignItems="stretch"
      > */}
      {/* Form Section */}
      {/* <Grid item xs={12} md={6}> */}
      <Paper elevation={4} sx={{ p: 4, height: "100%" }}>
        <Typography variant="h6" gutterBottom>
          {editingShape ? "Edit Shape" : "Create New Shape"}
        </Typography>
        <ShapeForm
          onSubmit={handleSubmit}
          editingShape={editingShape}
          onCancel={() => setEditingShape(null)}
        />
      </Paper>
      {/* </Grid> */}

      {/* List Section */}
      {/* <Grid item xs={12} md={6}> */}
      <Paper elevation={4} sx={{ p: 4, height: "100%" }}>
        <Typography variant="h6" gutterBottom>
          Shapes List
        </Typography>
        {shapes.length === 0 ? (
          <Box sx={{ color: "gray", textAlign: "center", py: 2 }}>
            No shapes yet. Add some!
          </Box>
        ) : (
          <ShapeList
            shapes={shapes}
            onEdit={setEditingShape}
            onDelete={handleDelete}
          />
        )}
      </Paper>
      {/* </Grid> */}
      {/* </Grid> */}
    </Container>
  );
}
