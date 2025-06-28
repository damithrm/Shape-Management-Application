import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ShapeManagement from './pages/ShapeManagement';
import ShapeDrawing from './pages/ShapeDrawing';
import Navbar from './components/Navbar';

export default function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<ShapeManagement />} />
        <Route path="/drawing" element={<ShapeDrawing />} />
      </Routes>
    </Router>
  );
}
