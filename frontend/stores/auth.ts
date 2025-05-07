import { create } from 'zustand';
import api from '../services/api';

interface User {
  id: number;
  username: string;
  role: string;
}

interface AuthState {
  user: User | null;
  token: string | null;
  loading: boolean;
  login: (username: string, password: string) => Promise<void>;
  logout: () => void;
  fetchUser: () => Promise<void>;
}

export const useAuthStore = create<AuthState>((set, get) => ({
  user: null,
  token: null,
  loading: false,
  login: async (username, password) => {
    set({ loading: true });
    const res = await api.post('/auth/login', { username, password });
    const token = res.data.token;
    set({ token });
    localStorage.setItem('token', token);
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    const usersRes = await api.get('/users');
    const user = usersRes.data.find((u: any) => u.username === username);
    if (!user) {
      set({ loading: false });
      throw new Error('Kullanıcı bilgisi alınamadı!');
    }
    set({ user, loading: false });
    localStorage.setItem('user', JSON.stringify(user));
  },
  logout: () => {
    set({ user: null, token: null });
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    delete api.defaults.headers.common['Authorization'];
  },
  fetchUser: async () => {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');
    if (!token || !userStr) return;
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    const userObj = JSON.parse(userStr);
    const usersRes = await api.get('/users');
    const user = usersRes.data.find((u: any) => u.username === userObj.username);
    set({ user, token });
    localStorage.setItem('user', JSON.stringify(user));
  },
}));
export function getToken() {
  if (typeof window !== 'undefined') {
    return localStorage.getItem('token');
  }
  return null;
}