'use client';
import { useForm, Controller } from 'react-hook-form';
import { useEffect, useState } from 'react';
import { Box, Button, Paper, TextField, Typography, MenuItem, Select, InputLabel, FormControl, CircularProgress } from '@mui/material';
import api from '@/services/api';
import { useRouter, useParams, useSearchParams } from 'next/navigation';

interface AssignmentFormInputs {
  type: 'employee' | 'pool';
  vehicleId: number;
  employeeId?: number;
  mission?: string;
  startDate: string;
  endDate: string;
}

interface Vehicle { id: number; plateNumber: string; }
interface Employee { id: number; name: string; }

export default function AssignmentEditPage() {
  const { id } = useParams<{ id: string }>();
  const searchParams = useSearchParams();
  const type = searchParams.get('type');
  const { register, handleSubmit, control, setValue, formState: { errors } } = useForm<AssignmentFormInputs>({ defaultValues: { type: 'employee' } });
  const router = useRouter();
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (type === 'employee') {
      Promise.all([
        api.get('/vehicles'),
        api.get('/employees'),
        api.get(`/employee-assignments/${id}`)
      ]).then(([v, e, a]) => {
        setVehicles(v.data);
        setEmployees(e.data);
        const assignment = a.data;
        setValue('type', 'employee');
        setValue('vehicleId', assignment.vehicle?.id);
        setValue('employeeId', assignment.employee?.id);
        setValue('startDate', assignment.startDate);
        setValue('endDate', assignment.endDate);
      }).catch(() => setError('Atama bulunamadı')).finally(() => setLoading(false));
    } else if (type === 'pool') {
      api.get('/vehicles').then(res => setVehicles(res.data));
      api.get(`/pool-assignments/${id}`)
        .then(a => {
          const assignment = a.data;
          setValue('type', 'pool');
          setValue('vehicleId', assignment.vehicle?.id);
          setValue('startDate', assignment.startDate);
          setValue('endDate', assignment.endDate);
          setValue('mission', assignment.mission);
        })
        .catch(() => setError('Atama bulunamadı'))
        .finally(() => setLoading(false));
    }
  }, [id, setValue, type]);

  const onSubmit = async (data: AssignmentFormInputs) => {
    if (type === 'employee') {
      await api.put(`/employee-assignments/${id}`, {
        startDate: data.startDate,
        endDate: data.endDate,
        vehicle_id: data.vehicleId,
        employee_id: data.employeeId
      });
    } else if (type === 'pool') {
      await api.put(`/pool-assignments/${id}`, {
        startDate: data.startDate,
        endDate: data.endDate,
        vehicle_id: data.vehicleId,
        ...(data.mission ? { mission: data.mission } : {})
      });
    }
    router.push('/dashboard/assignments');
  };

  if (loading) return <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh"><CircularProgress /></Box>;
  if (error) return <Typography color="error">{error}</Typography>;

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Atama Düzenle</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <FormControl fullWidth margin="normal">
            <InputLabel>Araç</InputLabel>
            <Controller
              name="vehicleId"
              control={control}
              rules={{ required: true }}
              render={({ field }) => (
                <Select {...field} label="Araç" error={!!errors.vehicleId}>
                  {vehicles.map((v) => (
                    <MenuItem key={v.id} value={v.id}>{v.plateNumber}</MenuItem>
                  ))}
                </Select>
              )}
            />
          </FormControl>
          {type === 'employee' && (
            <FormControl fullWidth margin="normal">
              <InputLabel>Çalışan</InputLabel>
              <Controller
                name="employeeId"
                control={control}
                rules={{ required: true }}
                render={({ field }) => (
                  <Select {...field} label="Çalışan" error={!!errors.employeeId}>
                    {employees.map((e) => (
                      <MenuItem key={e.id} value={e.id}>{e.name}</MenuItem>
                    ))}
                  </Select>
                )}
              />
            </FormControl>
          )}
          {type === 'pool' && (
            <TextField label="Görev (Mission)" fullWidth margin="normal" {...register('mission')} />
          )}
          <TextField label="Başlangıç Tarihi" type="date" fullWidth margin="normal" {...register('startDate', { required: true })} InputLabelProps={{ shrink: true }} error={!!errors.startDate} />
          <TextField label="Bitiş Tarihi" type="date" fullWidth margin="normal" {...register('endDate', { required: true })} InputLabelProps={{ shrink: true }} error={!!errors.endDate} />
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Kaydet</Button>
        </form>
      </Paper>
    </Box>
  );
}