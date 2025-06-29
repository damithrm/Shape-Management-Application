import React, { createContext, useContext, useState } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [credentials, setCredentials] = useState({ username: "", password: "" });

  const setAuth = (username, password) => {
    setCredentials({ username, password });
  };

  const getAuthHeader = () => {
    if (!credentials.username || !credentials.password) return null;
    return "Basic " + btoa(`${credentials.username}:${credentials.password}`);
  };

  return (
    <AuthContext.Provider value={{ credentials, setAuth, getAuthHeader }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
