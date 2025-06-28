import React, { createContext, useContext } from 'react';
import { SnackbarProvider, useSnackbar } from 'notistack';

const NotificationContext = createContext();

export function NotificationProvider({ children }) {
  const { enqueueSnackbar, closeSnackbar } = useSnackbar();

  const showNotification = (message, options = {}) => {
    enqueueSnackbar(message, {
      variant: options.type || 'default',
      autoHideDuration: options.duration || 3000,
      ...options
    });
  };

  const showSuccess = (message, options = {}) => {
    showNotification(message, { ...options, type: 'success' });
  };

  const showError = (message, options = {}) => {
    showNotification(message, { ...options, type: 'error' });
  };

  const showWarning = (message, options = {}) => {
    showNotification(message, { ...options, type: 'warning' });
  };

  const showInfo = (message, options = {}) => {
    showNotification(message, { ...options, type: 'info' });
  };

  return (
    <NotificationContext.Provider
      value={{
        showNotification,
        showSuccess,
        showError,
        showWarning,
        showInfo,
        closeSnackbar
      }}
    >
      {children}
    </NotificationContext.Provider>
  );
}

export const useNotification = () => useContext(NotificationContext);

export function NotificationWrapper({ children }) {
  return (
    <SnackbarProvider 
      maxSnack={3} 
      anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
    >
      <NotificationProvider>
        {children}
      </NotificationProvider>
    </SnackbarProvider>
  );
}