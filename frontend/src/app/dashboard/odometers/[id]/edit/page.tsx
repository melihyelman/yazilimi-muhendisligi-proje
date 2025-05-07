"use client";
import { useForm, Controller } from 'react-hook-form';
import { useEffect, useState } from 'react';
import { Box, Button, Paper, TextField, Typography, MenuItem, Select, InputLabel, FormControl, CircularProgress } from '@mui/material';
import api from '@/services/api';
import { useRouter, useParams } from 'next/navigation';

interface OdometerFormInputs {
  vehicleId: string;
  date: string;
  km: number;
}

interface Vehicle { id: number; plateNumber: string; }

export default function OdometerEditPage() {
  const { id } = useParams<{ id: string }>();
  const { register, handleSubmit, control, setValue, formState: { errors } } = useForm<OdometerFormInputs>();
  const router = useRouter();
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.get('/vehicles'),
      api.get(`/readings/${id}`)
    ]).then(([vehRes, odoRes]) => {
      setVehicles(vehRes.data);
      const reading = odoRes.data;
      setValue('vehicleId', reading.vehicle?.id?.toString() || '');
      setValue('date', reading.date);
      setValue('km', reading.km);
    }).finally(() => setLoading(false));
  }, [id, setValue]);

  const handleVehicleChange = (value: string) => setValue('vehicleId', value);

  const onSubmit = async (data: OdometerFormInputs) => {
    await api.put(`/readings/${id}`, {
      date: data.date,
      km: Number(data.km),
      vehicle_id: Number(data.vehicleId)
    });
    router.push('/dashboard/odometers');
  };

  if (loading) return <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh"><CircularProgress /></Box>;

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Kilometre Kaydını Düzenle</Typography>
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
                    <MenuItem key={v.id} value={v.id}>{v.plateNumber}</MenuItem>
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
