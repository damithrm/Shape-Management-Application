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
// import { useNotification } from '../context/NotificationProvider';

export default function ShapeForm({ onSubmit, editingShape, onCancel }) {
  const [name, setName] = useState('');
  const [type, setType] = useState('CIRCLE');
  const [centerX, setCenterX] = useState('');
  const [centerY, setCenterY] = useState('');
  const [radius, setRadius] = useState('');
  const [vertices, setVertices] = useState('');

  useEffect(() => {
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

  const handleSubmit = (e) => {
    e.preventDefault();
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
          onChange={e => setName(e.target.value)}
          required
          fullWidth
        />

        <FormControl fullWidth>
          <InputLabel id="shape-type-label">Shape Type</InputLabel>
          <Select
            labelId="shape-type-label"
            id="shape-type"
            value={type}
            label="Shape Type"
            onChange={e => setType(e.target.value)}
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
              onChange={e => setCenterX(e.target.value)}
              required
              fullWidth
            />
            <TextField
              id="center-y"
              label="Center Y"
              type="number"
              variant="outlined"
              value={centerY}
              onChange={e => setCenterY(e.target.value)}
              required
              fullWidth
            />
            <TextField
              id="radius"
              label="Radius"
              type="number"
              variant="outlined"
              value={radius}
              onChange={e => setRadius(e.target.value)}
              required
              fullWidth
            />
          </>
        ) : (
          <TextField
            id="vertices"
            label="Vertices (x,y;x,y;...)"
            variant="outlined"
            value={vertices}
            onChange={e => setVertices(e.target.value)}
            required
            helperText="Example: 0,0; 10,0; 10,10"
            fullWidth
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
