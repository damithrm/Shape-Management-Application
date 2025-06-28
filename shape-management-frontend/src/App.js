import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import ShapeManagement from "./pages/ShapeManagement";
import ShapeDrawing from "./pages/ShapeDrawing";
import Navbar from "./components/Navbar";
import { NotificationWrapper } from './context/NotificationProvider';

export default function App() {
  return (
    <NotificationWrapper>
        <Router>
          <Navbar />
          <Routes>
            <Route path="/" element={<ShapeManagement />} />
            <Route path="/drawing" element={<ShapeDrawing />} />
          </Routes>
        </Router>
    </NotificationWrapper>
  );
}
