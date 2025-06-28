import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8000/api/shapes'
});

export const getShapes = () => api.get('');
export const createShape = (shape) => api.post('', shape);
export const updateShape = (id, shape) => api.put(`/${id}`, shape);
export const deleteShape = (id) => api.delete(`/${id}`);
export const getOverlaps = () => api.get('/overlaps');
