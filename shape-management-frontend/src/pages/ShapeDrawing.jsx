import React, { useEffect, useState } from 'react';
import ShapeCanvas from '../components/ShapeCanvas';
import { getShapes, getOverlaps } from '../services/shapeApi';

import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

export default function ShapeDrawing() {
  const [shapes, setShapes] = useState([]);
  const [overlaps, setOverlaps] = useState([]);

  useEffect(() => {
    const fetchShapesAndOverlaps = async () => {
      const shapesRes = await getShapes();
      const overlapsRes = await getOverlaps();
      setShapes(shapesRes.data.content);
      setOverlaps(overlapsRes.data.content);
    };

    fetchShapesAndOverlaps();
  }, []);

  return (
    <Box
      sx={{
        height: 'calc(100vh - 64px)', 
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      <Container maxWidth="xl" sx={{ py: 2 }}>
        <Typography variant="h4" gutterBottom>
          Shape Drawing
        </Typography>
      </Container>
      <Box
        sx={{
          flex: 1,
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          px: 2,
          pb: 2,
        }}
      >
        <ShapeCanvas shapes={shapes} overlaps={overlaps} />
      </Box>
    </Box>
  );
}
