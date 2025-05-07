'use client';
import { useForm, Controller } from 'react-hook-form';
import { useEffect, useState } from 'react';
import { Box, Button, Paper, TextField, Typography, MenuItem, Select, InputLabel, FormControl, CircularProgress } from '@mui/material';
import api from '../../../../../services/api';
import { useRouter, useParams } from 'next/navigation';

interface UserFormInputs {
  username: string;
  password?: string;
  role: string;
}

export default function UserEditPage() {
  const { id } = useParams<{ id: string }>();
  const { register, handleSubmit, control, setValue, formState: { errors } } = useForm<UserFormInputs>();
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get(`/users/${id}`)
      .then(res => {
        setValue('username', res.data.username);
        setValue('role', res.data.role);
      })
      .catch(() => setError('Kullanıcı bulunamadı'))
      .finally(() => setLoading(false));
  }, [id, setValue]);

  const onSubmit = async (data: UserFormInputs) => {
    try {
      await api.put(`/users/${id}`, data);
      router.push('/dashboard/users');
    } catch (err: any) {
      if (err?.response?.status === 403) {
        alert('Yetkiniz yok veya bu işlemi yapamazsınız!');
      } else {
        alert('Kullanıcı güncellenemedi!');
      }
    }
  };

  if (loading) return <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh"><CircularProgress /></Box>;
  if (error) return <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh"><Typography color="error">{error}</Typography></Box>;

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Kullanıcıyı Düzenle</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField label="Kullanıcı Adı" fullWidth margin="normal" {...register('username', { required: true })} error={!!errors.username} />
          <TextField label="Yeni Şifre (opsiyonel)" type="password" fullWidth margin="normal" {...register('password')} />
          <FormControl fullWidth margin="normal">
            <InputLabel>Rol</InputLabel>
            <Controller
              name="role"
              control={control}
              rules={{ required: true }}
              render={({ field }) => (
                <Select
                  {...field}
                  label="Rol"
                  error={!!errors.role}
                  value={field.value || ''}
                >
                  <MenuItem value="ADMIN">Admin</MenuItem>
                  <MenuItem value="VENDOR">Tedarikçi</MenuItem>
                  <MenuItem value="EMPLOYEE">Çalışan</MenuItem>
                </Select>
              )}
            />
          </FormControl>
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Kaydet</Button>
        </form>
      </Paper>
    </Box>
  );
}
