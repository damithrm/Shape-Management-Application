import React, { useRef, useEffect, useState } from 'react';

export default function ShapeCanvas({ shapes, overlaps }) {
  const canvasRef = useRef();
  const [dimensions, setDimensions] = useState({
    width: window.innerWidth - 50,
    height: window.innerHeight - 150
  });

  useEffect(() => {
    const handleResize = () => {
      setDimensions({
        width: window.innerWidth - 50,
        height: window.innerHeight - 150
      });
    };

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  useEffect(() => {
    if (!shapes.length) return;
    
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');

    // Calculate bounds of all shapes
    let minX = Infinity;
    let minY = Infinity;
    let maxX = -Infinity;
    let maxY = -Infinity;

    shapes.forEach(shape => {
      if (shape.type === 'CIRCLE') {
        // For circles, consider the bounding box
        minX = Math.min(minX, shape.centerX - shape.radius);
        minY = Math.min(minY, shape.centerY - shape.radius);
        maxX = Math.max(maxX, shape.centerX + shape.radius);
        maxY = Math.max(maxY, shape.centerY + shape.radius);
      } else if (shape.vertices?.length) {
        // For polygons, check all vertices
        shape.vertices.forEach(vertex => {
          minX = Math.min(minX, vertex.x);
          minY = Math.min(minY, vertex.y);
          maxX = Math.max(maxX, vertex.x);
          maxY = Math.max(maxY, vertex.y);
        });
      }
    });

    // Add padding (10% on each side)
    const width = maxX - minX;
    const height = maxY - minY;
    const padding = Math.max(width, height) * 0.1;
    
    minX -= padding;
    minY -= padding;
    maxX += padding;
    maxY += padding;

    // Calculate scale to fit in canvas
    const contentWidth = maxX - minX;
    const contentHeight = maxY - minY;
    const scaleX = canvas.width / contentWidth;
    const scaleY = canvas.height / contentHeight;
    const scale = Math.min(scaleX, scaleY);

    // Clear the canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // Apply transformations
    ctx.save();
    
    // Center content in canvas
    const centerX = (canvas.width - contentWidth * scale) / 2;
    const centerY = (canvas.height - contentHeight * scale) / 2;
    
    ctx.translate(centerX, centerY);
    ctx.scale(scale, scale);
    ctx.translate(-minX, -minY);

    // Draw each shape
    shapes.forEach((shape) => {
      const isOverlap = overlaps.includes(shape.shapeId);
      ctx.strokeStyle = isOverlap ? 'red' : 'green';
      ctx.lineWidth = isOverlap ? 3 / scale : 1 / scale; // Adjust line width for scaling

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

    // Restore the canvas state
    ctx.restore();
    
  }, [shapes, overlaps, dimensions]);

  return (
    <canvas
      ref={canvasRef}
      width={dimensions.width}
      height={dimensions.height}
      style={{
        maxWidth: '100%',
        maxHeight: '100%',
        border: '1px solid #ccc',
      }}
    />
  );
}