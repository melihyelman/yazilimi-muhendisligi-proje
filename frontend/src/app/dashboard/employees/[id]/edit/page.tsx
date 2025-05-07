'use client';
import { useForm } from 'react-hook-form';
import { useEffect } from 'react';
import { Box, Button, Paper, TextField, Typography } from '@mui/material';
import api from '@/services/api';
import { useRouter, useParams } from 'next/navigation';
import { useAuthStore } from '@/stores/auth';

interface EmployeeFormInputs {
  name: string;
  department: string;
  username: string;
  password?: string;
}

export default function EmployeeEditPage() {
  const { id } = useParams<{ id: string }>();
  const { register, handleSubmit, setValue, formState: { errors } } = useForm<EmployeeFormInputs>();
  const router = useRouter();

  useEffect(() => {
    api.get(`/employees/${id}`)
      .then(res => {
        setValue('name', res.data.name);
        setValue('department', res.data.department);
        setValue('username', res.data.username);
      });
  }, [id, setValue]);

  const onSubmit = async (data: EmployeeFormInputs) => {
    await api.put(`/employees/${id}`, {
      name: data.name,
      department: data.department,
      username: data.username,
      role: 'EMPLOYEE',
      ...(data.password ? { passwordHash: data.password } : {})
    });
    router.push('/dashboard/employees');
  };

  const user = useAuthStore.getState().user;
  if (user?.role !== 'ADMIN') return <Typography color="error" p={4}>Bu sayfaya erişim yetkiniz yok.</Typography>;

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Çalışan Düzenle</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField label="Ad" fullWidth margin="normal" {...register('name', { required: true })} error={!!errors.name} />
          <TextField label="Bölüm" fullWidth margin="normal" {...register('department', { required: true })} error={!!errors.department} />
          <TextField label="Kullanıcı Adı" fullWidth margin="normal" {...register('username', { required: true })} error={!!errors.username} />
          <TextField label="Şifre (değiştirmek için doldurun)" type="password" fullWidth margin="normal" {...register('password')} error={!!errors.password} />
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Kaydet</Button>
        </form>
      </Paper>
    </Box>
  );
}