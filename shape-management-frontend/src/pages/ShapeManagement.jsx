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
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import AddIcon from "@mui/icons-material/Add";

import { useNotification } from "../context/NotificationProvider";
import { useAuth } from "../context/AuthContext"; // ✅ authentication context

export default function ShapeManagement() {
  const [shapes, setShapes] = useState([]);
  const [editingShape, setEditingShape] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const { showSuccess, showWarning, showError } = useNotification();
  const { setAuth, getAuthHeader } = useAuth();

  // Prompt user for credentials once
  useEffect(() => {
    const username = window.prompt("Enter username:");
    const password = window.prompt("Enter password:");
    setAuth(username, password);
    fetchShapes();
  }, []);

  const fetchShapes = async () => {
    try {
      const res = await getShapes();
      setShapes(res.data.content || []);
    } catch (e) {
      showError("Failed to fetch shapes");
    }
  };

  const handleOpenModal = (shape = null) => {
    setEditingShape(shape);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setEditingShape(null);
    setIsModalOpen(false);
  };

  const handleSubmit = async (shape) => {
    const auth = { headers: { Authorization: getAuthHeader() } };
    try {
      let result;
      if (editingShape) {
        result = await updateShape(editingShape.shapeId, shape, auth);
      } else {
        result = await createShape(shape, auth);
      }

      if (result?.data?.responseCode === "00") {
        showSuccess(result.data.responseMessage || "Success!");
        handleCloseModal();
        fetchShapes();
      } else {
        showWarning(result?.data?.responseMessage || "Failed to process shape");
      }
    } catch (err) {
      showError("Error submitting shape. Please check your credentials or data.");
    }
  };

  const handleDelete = async (id) => {
    const auth = { headers: { Authorization: getAuthHeader() } };
    try {
      const result = await deleteShape(id, auth);
      if (result?.data?.responseCode === "00") {
        showSuccess("Shape deleted successfully");
        fetchShapes();
      } else {
        showWarning(result?.data?.responseMessage || "Delete failed");
      }
    } catch (err) {
      showError("Error deleting shape. Please check your credentials.");
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 6, mb: 6 }}>
      <Typography variant="h4" gutterBottom className="font-bold text-center">
        Shape Management
      </Typography>

      <Box sx={{ mb: 3, display: "flex", justifyContent: "flex-end" }}>
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={() => handleOpenModal()}
        >
          Add New Shape
        </Button>
      </Box>

      <Paper elevation={4} sx={{ p: 4 }}>
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
            onEdit={handleOpenModal}
            onDelete={handleDelete}
          />
        )}
      </Paper>

      {/* Shape Form Modal */}
      <Dialog open={isModalOpen} onClose={handleCloseModal} maxWidth="sm" fullWidth>
        <DialogTitle>{editingShape ? "Edit Shape" : "Create New Shape"}</DialogTitle>
        <DialogContent dividers>
          <ShapeForm
            onSubmit={handleSubmit}
            editingShape={editingShape}
            onCancel={handleCloseModal}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseModal} color="secondary">
            Cancel
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}
