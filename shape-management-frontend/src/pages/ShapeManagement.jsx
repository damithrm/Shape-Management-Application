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
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import AddIcon from "@mui/icons-material/Add";
import { useNotification } from "../context/NotificationProvider";

export default function ShapeManagement() {
  const [shapes, setShapes] = useState([]);
  const [editingShape, setEditingShape] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { showSuccess, showWarning, showInfo, showError } = useNotification();

  const fetchShapes = async () => {
    const res = await getShapes();
    setShapes(res.data.content);
  };

  useEffect(() => {
    fetchShapes();
  }, []);

  const handleOpenModal = (shape = null) => {
    setEditingShape(shape);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setEditingShape(null);
    setIsModalOpen(false);
  };

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
        handleCloseModal();
      } else {
        showError(result?.data?.responseMessage, {
          autoHideDuration: 2000,
        });
      }
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
        handleCloseModal();
      } else {
        showError(result?.data?.responseMessage, {
          autoHideDuration: 2000,
        });
      }
    }
    fetchShapes();
  };

  const handleDelete = async (id) => {
    const result = await deleteShape(id);
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
    fetchShapes();
  };

  return (
    <Container maxWidth="md" sx={{ mt: 6, mb: 6 }}>
      <Typography variant="h4" gutterBottom className="font-bold text-center">
        Shape Management
      </Typography>

      <Box sx={{ mb: 3, display: 'flex', justifyContent: 'flex-end' }}>
        <Button 
          variant="contained" 
          color="primary" 
          startIcon={<AddIcon />}
          onClick={() => handleOpenModal()}
        >
          Add New Shape
        </Button>
      </Box>

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
            onEdit={handleOpenModal}
            onDelete={handleDelete}
          />
        )}
      </Paper>

      {/* Shape Form Modal */}
      <Dialog 
        open={isModalOpen} 
        onClose={handleCloseModal}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          {editingShape ? "Edit Shape" : "Create New Shape"}
        </DialogTitle>
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