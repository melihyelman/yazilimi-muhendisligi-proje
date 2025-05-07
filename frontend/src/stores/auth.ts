import { create } from 'zustand';
import api from '../services/api';

interface User {
  name: string;
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
  setAuthFromStorage: () => void;
}

// login fonksiyonu store dışında tanımlanıyor
async function loginImpl(username: string, password: string) {
  useAuthStore.setState({ loading: true });
  const res = await api.post('/auth/login', { username, password });
  const token = res.data.token;
  useAuthStore.setState({ token });
  localStorage.setItem('token', token);
  api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  const usersRes = await api.get('/users');
  const user = usersRes.data.find((u: any) => u.username === username);
  if (!user) {
    useAuthStore.setState({ loading: false });
    throw new Error('Kullanıcı bilgisi alınamadı!');
  }
  useAuthStore.setState({ user, loading: false });
  localStorage.setItem('user', JSON.stringify(user));
}

export const useAuthStore = create<AuthState>((set, get) => ({
  user: null,
  token: null,
  loading: false,
  login: loginImpl,
  logout: () => {
    set({ user: null, token: null });
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
    delete api.defaults.headers.common['Authorization'];
  },
  fetchUser: async () => {
    if (typeof window === 'undefined') return;
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
  setAuthFromStorage: () => {
    if (typeof window === 'undefined') return;
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    if (token && userStr) {
      try {
        const user = JSON.parse(userStr);
        set({ token, user });
      } catch {
        set({ token, user: null });
      }
    }
  },
}));