import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import ShapeManagement from "./pages/ShapeManagement";
import ShapeDrawing from "./pages/ShapeDrawing";
import Navbar from "./components/Navbar";
import { NotificationWrapper } from "./context/NotificationProvider";
import { AuthProvider } from "./context/AuthContext";

export default function App() {
  return (
    <NotificationWrapper>
      <AuthProvider>
        <Router>
          <Navbar />
          <Routes>
            <Route path="/manage-shapes" element={<ShapeManagement />} />
            <Route path="/" element={<ShapeDrawing />} />
          </Routes>
        </Router>
      </AuthProvider>
    </NotificationWrapper>
  );
}
