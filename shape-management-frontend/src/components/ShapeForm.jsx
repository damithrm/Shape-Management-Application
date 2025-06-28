import React, { useState, useEffect } from 'react';
import {
  TextField,
  Button,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  Stack,
  Box
} from '@mui/material';
import { useNotification } from '../context/NotificationProvider';

export default function ShapeForm({ onSubmit, editingShape, onCancel }) {
  const [name, setName] = useState('');
  const [type, setType] = useState('CIRCLE');
  const [centerX, setCenterX] = useState('');
  const [centerY, setCenterY] = useState('');
  const [radius, setRadius] = useState('');
  const [vertices, setVertices] = useState('');
  const [errors, setErrors] = useState({
    name: '',
    centerX: '',
    centerY: '',
    radius: '',
    vertices: ''
  });
  
  const { showError } = useNotification?.() || { showError: () => {} };

  useEffect(() => {
    // Reset errors when shape changes
    setErrors({
      name: '',
      centerX: '',
      centerY: '',
      radius: '',
      vertices: ''
    });
    
    if (editingShape) {
      setName(editingShape.name);
      setType(editingShape.type);
      if (editingShape.type === 'CIRCLE') {
        setCenterX(editingShape.centerX || '');
        setCenterY(editingShape.centerY || '');
        setRadius(editingShape.radius || '');
      } else {
        setVertices(editingShape.vertices?.map(v => `${v.x},${v.y}`).join(';') || '');
      }
    } else {
      setName('');
      setType('CIRCLE');
      setCenterX('');
      setCenterY('');
      setRadius('');
      setVertices('');
    }
  }, [editingShape]);

  // Validation functions
  const validateName = (value) => {
    if (!value.trim()) {
      return 'Shape name cannot be empty';
    }
    return '';
  };

  const validatePositiveNumber = (value, fieldName) => {
    if (!value || isNaN(parseFloat(value)) || parseFloat(value) <= 0) {
      return `${fieldName} must be greater than 0`;
    }
    return '';
  };

  const validateVertices = (value, shapeType) => {
    if (!value.trim()) {
      return 'Vertices cannot be empty';
    }

    const vertexCount = value.split(';').filter(v => v.trim()).length;
    const currentType = shapeType || type;

    if (currentType === 'RECTANGLE' && vertexCount !== 4) {
      return 'Rectangle must have exactly 4 vertices';
    } else if (currentType === 'TRIANGLE' && vertexCount !== 3) {
      return 'Triangle must have exactly 3 vertices';
    } else if (currentType === 'POLYGON' && vertexCount < 5) {
      return 'Polygon must have 5 or more vertices';
    }

    // Validate format of each vertex
    const invalidVertex = value.split(';').some(v => {
      const parts = v.split(',');
      return parts.length !== 2 || isNaN(parts[0]) || isNaN(parts[1]);
    });

    if (invalidVertex) {
      return 'All vertices must be in format "x,y"';
    }

    return '';
  };

  // Handle input changes with validation
  const handleNameChange = (e) => {
    const value = e.target.value;
    setName(value);
    setErrors(prev => ({ ...prev, name: validateName(value) }));
  };

  const handleTypeChange = (e) => {
    const newType = e.target.value;
    setType(newType);
    if (newType !== 'CIRCLE' && vertices) {
      setErrors(prev => ({ ...prev, vertices: validateVertices(vertices, newType) }));
    }
  };

  const handleCenterXChange = (e) => {
    const value = e.target.value;
    setCenterX(value);
    setErrors(prev => ({ ...prev, centerX: validatePositiveNumber(value, 'Center X') }));
  };

  const handleCenterYChange = (e) => {
    const value = e.target.value;
    setCenterY(value);
    setErrors(prev => ({ ...prev, centerY: validatePositiveNumber(value, 'Center Y') }));
  };

  const handleRadiusChange = (e) => {
    const value = e.target.value;
    setRadius(value);
    setErrors(prev => ({ ...prev, radius: validatePositiveNumber(value, 'Radius') }));
  };

  const handleVerticesChange = (e) => {
    const value = e.target.value;
    setVertices(value);
    setErrors(prev => ({ ...prev, vertices: validateVertices(value) }));
  };

  const validateForm = () => {
    const newErrors = {
      name: validateName(name)
    };
    
    if (type === 'CIRCLE') {
      newErrors.centerX = validatePositiveNumber(centerX, 'Center X');
      newErrors.centerY = validatePositiveNumber(centerY, 'Center Y');
      newErrors.radius = validatePositiveNumber(radius, 'Radius');
    } else {
      newErrors.vertices = validateVertices(vertices);
    }
    
    setErrors(newErrors);
    
    // Check if there are any errors
    const hasErrors = Object.values(newErrors).some(error => error !== '');
    if (hasErrors) {
      showError('Please correct the errors in the form');
    }
    
    return !hasErrors;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return; // Stop if validation fails
    }
    
    const shape = { name, type };

    if (type === 'CIRCLE') {
      shape.centerX = parseFloat(centerX);
      shape.centerY = parseFloat(centerY);
      shape.radius = parseFloat(radius);
    } else {
      shape.vertices = vertices.split(';').map(p => {
        const [x, y] = p.split(',').map(Number);
        return { x, y };
      });
    }

    onSubmit(shape);
  };

  return (
    <Box component="form" onSubmit={handleSubmit} noValidate autoComplete="off">
      <Stack spacing={2}>
        <TextField
          id="shape-name"
          label="Shape Name"
          variant="outlined"
          value={name}
          onChange={handleNameChange}
          required
          fullWidth
          error={!!errors.name}
          helperText={errors.name || ' '}
        />

        <FormControl fullWidth>
          <InputLabel id="shape-type-label">Shape Type</InputLabel>
          <Select
            labelId="shape-type-label"
            id="shape-type"
            value={type}
            label="Shape Type"
            onChange={handleTypeChange}
          >
            <MenuItem value="CIRCLE">Circle</MenuItem>
            <MenuItem value="RECTANGLE">Rectangle</MenuItem>
            <MenuItem value="TRIANGLE">Triangle</MenuItem>
            <MenuItem value="POLYGON">Polygon</MenuItem>
          </Select>
        </FormControl>

        {type === 'CIRCLE' ? (
          <>
            <TextField
              id="center-x"
              label="Center X"
              type="number"
              variant="outlined"
              value={centerX}
              onChange={handleCenterXChange}
              required
              fullWidth
              error={!!errors.centerX}
              helperText={errors.centerX || ' '}
            />
            <TextField
              id="center-y"
              label="Center Y"
              type="number"
              variant="outlined"
              value={centerY}
              onChange={handleCenterYChange}
              required
              fullWidth
              error={!!errors.centerY}
              helperText={errors.centerY || ' '}
            />
            <TextField
              id="radius"
              label="Radius"
              type="number"
              variant="outlined"
              value={radius}
              onChange={handleRadiusChange}
              required
              fullWidth
              error={!!errors.radius}
              helperText={errors.radius || ' '}
            />
          </>
        ) : (
          <TextField
            id="vertices"
            label="Vertices (x,y;x,y;...)"
            variant="outlined"
            value={vertices}
            onChange={handleVerticesChange}
            required
            fullWidth
            error={!!errors.vertices}
            helperText={errors.vertices || `Example: 0,0;10,0;10,10${type === 'TRIANGLE' ? ' (3 vertices)' : 
                                           type === 'RECTANGLE' ? ' (4 vertices)' : 
                                           type === 'POLYGON' ? ' (5+ vertices)' : ''}`}
          />
        )}

        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button variant="contained" type="submit">
            {editingShape ? 'Update' : 'Create'}
          </Button>
          {editingShape && (
            <Button variant="outlined" color="secondary" onClick={onCancel}>
              Cancel
            </Button>
          )}
        </Box>
      </Stack>
    </Box>
  );
}