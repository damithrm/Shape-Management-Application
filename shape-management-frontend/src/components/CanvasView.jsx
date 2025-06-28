import React, { useEffect, useRef, useState } from 'react';
import { fetchShapes, getOverlaps } from '../api/shapeApi';

const CanvasView = () => {
  const canvasRef = useRef(null);
  const [shapes, setShapes] = useState([]);
  const [overlaps, setOverlaps] = useState(new Set());

  useEffect(() => {
    const loadShapes = async () => {
      const res = await fetchShapes();
      const overlapRes = await getOverlaps();
      setShapes(res.data.content);
      setOverlaps(new Set(overlapRes.data.content));
    };
    loadShapes();
  }, []);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    shapes.forEach(shape => {
      ctx.beginPath();
      const isOverlap = overlaps.has(shape.shapeId);

      if (shape.type === 'CIRCLE') {
        ctx.arc(shape.centerX, shape.centerY, shape.radius, 0, 2 * Math.PI);
      } else {
        shape.vertices?.forEach((v, i) => {
          if (i === 0) ctx.moveTo(v.x, v.y);
          else ctx.lineTo(v.x, v.y);
        });
        ctx.closePath();
      }

      ctx.fillStyle = isOverlap ? 'rgba(255, 0, 0, 0.3)' : 'rgba(0, 128, 255, 0.3)';
      ctx.strokeStyle = isOverlap ? 'red' : 'black';
      ctx.lineWidth = isOverlap ? 2 : 1;
      ctx.fill();
      ctx.stroke();
    });
  }, [shapes, overlaps]);

  return <canvas ref={canvasRef} width={800} height={600} className="border" />;
};

export default CanvasView;
