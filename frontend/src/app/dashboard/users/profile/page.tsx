'use client';
import { useAuthStore } from '@/stores/auth';
import { useForm } from 'react-hook-form';
import { Box, Paper, Typography, TextField, Button, Alert } from '@mui/material';
import { useState } from 'react';
import api from '@/services/api';

interface PasswordForm {
  password: string;
}

export default function UserProfilePage() {
  const user = useAuthStore(s => s.user);
  const { register, handleSubmit, reset, formState: { errors } } = useForm<PasswordForm>();
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  const onSubmit = async (data: PasswordForm) => {
    setSuccess(''); setError('');
    try {
      await api.put(`/users/${user?.id}`, { password: data.password });
      setSuccess('Şifre başarıyla güncellendi.');
      reset();
    } catch {
      setError('Şifre güncellenemedi!');
    }
  };

  if (!user) return <Box p={4}><Alert severity="error">Kullanıcı bilgisi bulunamadı.</Alert></Box>;

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Profil Bilgilerim</Typography>
        <Typography><b>Kullanıcı Adı:</b> {user.username}</Typography>
        <Typography mb={2}><b>Rol:</b> {user.role}</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Typography variant="subtitle1" mt={3} mb={1}>Şifre Değiştir</Typography>
          <TextField label="Yeni Şifre" type="password" fullWidth margin="normal" {...register('password', { required: true, minLength: 4 })} error={!!errors.password} helperText={errors.password && 'En az 4 karakter'} />
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 1 }}>Şifreyi Güncelle</Button>
        </form>
        {success && <Alert severity="success" sx={{ mt: 2 }}>{success}</Alert>}
        {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
      </Paper>
    </Box>
  );
}
