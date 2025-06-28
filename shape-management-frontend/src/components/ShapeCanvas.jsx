import React, { useRef, useEffect } from 'react';

export default function ShapeCanvas({ shapes, overlaps }) {
  const canvasRef = useRef();

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');

    // Clear
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Draw each shape
    shapes.forEach((shape) => {
      const isOverlap = overlaps.includes(shape.shapeId);
      ctx.strokeStyle = isOverlap ? 'red' : 'green';
      ctx.lineWidth = isOverlap ? 3 : 1;

      if (shape.type === 'CIRCLE') {
        ctx.beginPath();
        ctx.arc(shape.centerX, shape.centerY, shape.radius, 0, 2 * Math.PI);
        ctx.stroke();
      } else if (shape.vertices?.length) {
        ctx.beginPath();
        ctx.moveTo(shape.vertices[0].x, shape.vertices[0].y);
        shape.vertices.forEach((v) => ctx.lineTo(v.x, v.y));
        ctx.closePath();
        ctx.stroke();
      }
    });
  }, [shapes, overlaps]);

  return (
    <canvas
      ref={canvasRef}
      width={window.innerWidth - 50}
      height={window.innerHeight - 150}
      style={{
        maxWidth: '100%',
        maxHeight: '100%',
        border: '1px solid #ccc',
      }}
    />
  );
}
