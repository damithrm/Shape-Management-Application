import React, { useEffect, useState } from 'react';
import ShapeCanvas from '../components/ShapeCanvas';
import { getShapes, getOverlaps } from '../services/shapeApi';

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
    <div>
      <h3>Shape Drawing</h3>
      <ShapeCanvas shapes={shapes} overlaps={overlaps} />
    </div>
  );
}
