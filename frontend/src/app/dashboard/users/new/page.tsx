'use client';
import { useForm, Controller } from 'react-hook-form';
import { Box, Button, Paper, TextField, Typography, MenuItem, Select, InputLabel, FormControl } from '@mui/material';
import api from '../../../../services/api';
import { useRouter } from 'next/navigation';

interface UserFormInputs {
  username: string;
  password: string;
  role: string;
}

export default function UserCreatePage() {
  const { register, handleSubmit, control, formState: { errors } } = useForm<UserFormInputs>();
  const router = useRouter();

  const onSubmit = async (data: UserFormInputs) => {
    await api.post('/users', data);
    router.push('/dashboard/users');
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Kullanıcı Ekle</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField label="Kullanıcı Adı" fullWidth margin="normal" {...register('username', { required: true })} error={!!errors.username} />
          <TextField label="Şifre" type="password" fullWidth margin="normal" {...register('password', { required: true })} error={!!errors.password} />
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
