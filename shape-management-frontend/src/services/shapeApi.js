import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8000/api/shapes'
});

export const getShapes = () => api.get('');
export const getOverlaps = () => api.get('/overlaps');

// Include auth headers manually for secure endpoints
export const createShape = (shape, config) => api.post('', shape, config);
export const updateShape = (id, shape, config) => api.put(`/${id}`, shape, config);
export const deleteShape = (id, config) => api.delete(`/${id}`, config);
