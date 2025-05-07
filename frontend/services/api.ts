import axios from 'axios';
import { getToken } from '../stores/auth';

const api = axios.create({
  baseURL: '/api',
});

api.interceptors.request.use((config) => {
  // Sadece /auth endpointleri hariç tüm isteklerde token ekle
  if (!config.url?.startsWith('/auth')) {
    const token = getToken?.();
    if (token) {
      config.headers = config.headers || {};
      config.headers['Authorization'] = `Bearer ${token}`;
    }
  }
  return config;
});

export default api;