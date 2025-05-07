'use client';
import { useForm, Controller } from 'react-hook-form';
import { useEffect, useState } from 'react';
import { Box, Button, Paper, TextField, Typography, MenuItem, Select, InputLabel, FormControl, CircularProgress } from '@mui/material';
import api from '../../../../services/api';
import { useRouter } from 'next/navigation';

interface OdometerFormInputs {
  vehicleId: string;
  date: string;
  km: number;
}

interface Vehicle { id: number; plateNumber: string; }

export default function OdometerCreatePage() {
  const { register, handleSubmit, control, formState: { errors }, setValue } = useForm<OdometerFormInputs>();
  const router = useRouter();
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/vehicles').then(res => setVehicles(res.data)).finally(() => setLoading(false));
  }, []);

  const handleVehicleChange = (value: string) => setValue('vehicleId', value);

  const onSubmit = async (data: OdometerFormInputs) => {
    try {
      await api.post('/readings', {
        date: data.date,
        km: Number(data.km),
        vehicle_id: Number(data.vehicleId)
      });
      router.push('/dashboard/odometers');
    } catch (err) {
      alert('Kayıt eklenemedi! (Yetki veya veri hatası)');
    }
  };

  if (loading) return <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh"><CircularProgress /></Box>;

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Yeni Kilometre Kaydı</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <FormControl fullWidth margin="normal">
            <InputLabel>Araç</InputLabel>
            <Controller
              name="vehicleId"
              control={control}
              rules={{ required: true }}
              render={({ field }) => (
                <Select
                  {...field}
                  label="Araç"
                  error={!!errors.vehicleId}
                  value={field.value || ''}
                  onChange={e => handleVehicleChange(e.target.value)}
                >
                  {vehicles.map((v) => (
                    <MenuItem key={v.id} value={String(v.id)}>{v.plateNumber}</MenuItem>
                  ))}
                </Select>
              )}
            />
          </FormControl>
          <TextField label="Tarih" type="date" fullWidth margin="normal" {...register('date', { required: true })} InputLabelProps={{ shrink: true }} error={!!errors.date} />
          <TextField label="Kilometre" type="number" fullWidth margin="normal" {...register('km', { required: true, min: 0 })} error={!!errors.km} />
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Kaydet</Button>
        </form>
      </Paper>
    </Box>
  );
}
