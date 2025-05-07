'use client';
import { useForm } from 'react-hook-form';
import { useEffect } from 'react';
import { Box, Button, Paper, TextField, Typography } from '@mui/material';
import api from '@/services/api';
import { useRouter, useParams } from 'next/navigation';
import { useAuthStore } from '@/stores/auth';

interface VendorFormInputs {
  name: string;
  username: string;
  companyName: string;
  department: string;
  password?: string;
}

export default function VendorEditPage() {
  const { id } = useParams<{ id: string }>();
  const { register, handleSubmit, setValue, formState: { errors } } = useForm<VendorFormInputs>();
  const router = useRouter();

  useEffect(() => {
    api.get(`/vendors/${id}`)
      .then(res => {
        setValue('name', res.data.name);
        setValue('username', res.data.username);
        setValue('companyName', res.data.companyName);
        setValue('department', res.data.department || '');
      });
  }, [id, setValue]);

  const onSubmit = async (data: VendorFormInputs) => {
    await api.put(`/vendors/${id}`, {
      name: data.name,
      username: data.username,
      companyName: data.companyName,
      department: data.department,
      role: 'VENDOR',
      ...(data.password ? { passwordHash: data.password } : {})
    });
    router.push('/dashboard/vendors');
  };

  const user = useAuthStore.getState().user;
  if (user?.role !== 'ADMIN') return <Typography color="error" p={4}>Bu sayfaya erişim yetkiniz yok.</Typography>;

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Tedarikçi Düzenle</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField label="Ad" fullWidth margin="normal" {...register('name', { required: true })} error={!!errors.name} />
          <TextField label="Kullanıcı Adı" fullWidth margin="normal" {...register('username', { required: true })} error={!!errors.username} />
          <TextField label="Şirket Adı" fullWidth margin="normal" {...register('companyName', { required: true })} error={!!errors.companyName} />
          <TextField label="Departman" fullWidth margin="normal" {...register('department', { required: true })} error={!!errors.department} />
          <TextField label="Şifre (değiştirmek için doldurun)" type="password" fullWidth margin="normal" {...register('password')} error={!!errors.password} />
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Kaydet</Button>
        </form>
      </Paper>
    </Box>
  );
}