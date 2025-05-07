'use client';
import React from 'react';
import { useForm } from 'react-hook-form';
import { TextField, Button, Box, Typography, Alert, Paper, InputAdornment, IconButton } from '@mui/material';
import { useAuthStore } from '../../stores/auth';
import { useRouter } from 'next/navigation';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';

interface LoginFormInputs {
  username: string;
  password: string;
}

export default function LoginPage() {
  const { register, handleSubmit, formState: { errors } } = useForm<LoginFormInputs>();
  const login = useAuthStore((s: any) => s.login);
  const router = useRouter();
  const [loading, setLoading] = React.useState(false);
  const [apiError, setApiError] = React.useState('');
  const [showPassword, setShowPassword] = React.useState(false);

  const onSubmit = async (data: LoginFormInputs) => {
    setLoading(true);
    setApiError('');
    try {
      await login(data.username, data.password);
      router.push('/dashboard');
    } catch (err: any) {
      setApiError(err?.response?.data?.message || err?.message || 'Giriş başarısız');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box minHeight="100vh" display="flex" alignItems="center" justifyContent="center" >
      <Paper sx={{ p: 4, minWidth: 350, borderRadius: 3, boxShadow: 3 }}>
        <Typography variant="h5" mb={2} fontWeight={700} align="center">Giriş Yap</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField
            label="Kullanıcı Adı"
            fullWidth
            margin="normal"
            {...register('username', { required: 'Kullanıcı adı zorunlu' })}
            error={!!errors.username}
            helperText={errors.username?.message}
            autoFocus
          />
          <TextField
            label="Şifre"
            type={showPassword ? 'text' : 'password'}
            fullWidth
            margin="normal"
            {...register('password', { required: 'Şifre zorunlu' })}
            error={!!errors.password}
            helperText={errors.password?.message}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton onClick={() => setShowPassword((v) => !v)} edge="end">
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              )
            }}
          />
          {apiError && <Alert severity="error" sx={{ mt: 2 }}>{apiError}</Alert>}
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            disabled={loading}
            sx={{ mt: 3 }}
            size="large"
          >
            {loading ? 'Giriş Yapılıyor...' : 'Giriş Yap'}
          </Button>
        </form>
      </Paper>
    </Box>
  );
}