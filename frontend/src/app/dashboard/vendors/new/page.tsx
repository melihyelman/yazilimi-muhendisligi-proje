'use client';
import { useForm } from 'react-hook-form';
import { Box, Button, Paper, TextField, Typography } from '@mui/material';
import api from '@/services/api';
import { useRouter } from 'next/navigation';
import { useAuthStore } from '@/stores/auth';

interface VendorFormInputs {
  name: string;
  username: string;
  companyName: string;
  password: string;
  department: string;
}

export default function VendorCreatePage() {
  const { register, handleSubmit, formState: { errors } } = useForm<VendorFormInputs>();
  const router = useRouter();

  const user = useAuthStore.getState().user;
  if (user?.role !== 'ADMIN') return <Typography color="error" p={4}>Bu sayfaya erişim yetkiniz yok.</Typography>;

  const onSubmit = async (data: VendorFormInputs) => {
    await api.post('/vendors', {
      name: data.name,
      username: data.username,
      companyName: data.companyName,
      department: data.department,
      role: 'VENDOR',
      passwordHash: data.password
    });
    router.push('/dashboard/vendors');
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Yeni Tedarikçi</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField label="Ad" fullWidth margin="normal" {...register('name', { required: true })} error={!!errors.name} />
          <TextField label="Kullanıcı Adı" fullWidth margin="normal" {...register('username', { required: true })} error={!!errors.username} />
          <TextField label="Şirket Adı" fullWidth margin="normal" {...register('companyName', { required: true })} error={!!errors.companyName} />
          <TextField label="Departman" fullWidth margin="normal" {...register('department', { required: true })} error={!!errors.department} />
          <TextField label="Şifre" type="password" fullWidth margin="normal" {...register('password', { required: true })} error={!!errors.password} />
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Kaydet</Button>
        </form>
      </Paper>
    </Box>
  );
}