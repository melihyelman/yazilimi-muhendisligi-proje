'use client';
import { useForm, Controller } from 'react-hook-form';
import { useEffect, useState } from 'react';
import { Box, Button, Paper, TextField, Typography, MenuItem, Select, InputLabel, FormControl, CircularProgress } from '@mui/material';
import api from '../../../../services/api';
import { useRouter } from 'next/navigation';

interface AssignmentFormInputs {
  type: 'employee' | 'pool';
  vehicle_id: string;
  employee_id?: string;
  mission?: string;
  startDate: string;
  endDate: string;
}

interface Vehicle { id: number; plateNumber: string; }
interface Employee { id: number; name: string; }

export default function AssignmentCreatePage() {
  const { register, handleSubmit, control, watch, formState: { errors }, setValue } = useForm<AssignmentFormInputs>({ defaultValues: { type: 'employee' } });
  const router = useRouter();
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [loading, setLoading] = useState(true);

  const type = watch('type');

  useEffect(() => {
    Promise.all([
      api.get('/vehicles'),
      api.get('/employees')
    ]).then(([v, e]) => {
      setVehicles(v.data);
      setEmployees(e.data);
    }).finally(() => setLoading(false));
  }, []);

  const handleVehicleChange = (value: string) => setValue('vehicle_id', value);
  const handleEmployeeChange = (value: string) => setValue('employee_id', value);

  const onSubmit = async (data: AssignmentFormInputs) => {
    if (data.type === 'employee') {
      await api.post('/employee-assignments', {
        startDate: data.startDate,
        endDate: data.endDate,
        approved: false,
        vehicle_id: Number(data.vehicle_id),
        employee_id: Number(data.employee_id),
        type: 'employee',
      });
    } else {
      await api.post('/pool-assignments', {
        startDate: data.startDate,
        endDate: data.endDate,
        approved: false,
        vehicle_id: Number(data.vehicle_id),
        mission: data.mission,
        type: 'pool',
      });
    }
    router.push('/dashboard/assignments');
  };

  if (loading) return <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh"><CircularProgress /></Box>;

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Yeni Atama</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <FormControl fullWidth margin="normal">
            <InputLabel>Atama Tipi</InputLabel>
            <Controller
              name="type"
              control={control}
              render={({ field }) => (
                <Select {...field} label="Atama Tipi">
                  <MenuItem value="employee">Çalışana Atama</MenuItem>
                  <MenuItem value="pool">Havuza Atama</MenuItem>
                </Select>
              )}
            />
          </FormControl>
          <FormControl fullWidth margin="normal">
            <InputLabel>Araç</InputLabel>
            <Controller
              name="vehicle_id"
              control={control}
              rules={{ required: true }}
              render={({ field }) => (
                <Select
                  {...field}
                  label="Araç"
                  error={!!errors.vehicle_id}
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
          {type === 'employee' && (
            <FormControl fullWidth margin="normal">
              <InputLabel>Çalışan</InputLabel>
              <Controller
                name="employee_id"
                control={control}
                rules={{ required: true }}
                render={({ field }) => (
                  <Select
                    {...field}
                    label="Çalışan"
                    error={!!errors.employee_id}
                    value={field.value || ''}
                    onChange={e => handleEmployeeChange(e.target.value)}
                  >
                    {employees.map((e) => (
                      <MenuItem key={e.id} value={String(e.id)}>{e.name}</MenuItem>
                    ))}
                  </Select>
                )}
              />
            </FormControl>
          )}
          {type === 'pool' && (
            <TextField label="Görev (Mission)" fullWidth margin="normal" {...register('mission', { required: true })} error={!!errors.mission} />
          )}
          <TextField label="Başlangıç Tarihi" type="date" fullWidth margin="normal" {...register('startDate', { required: true })} InputLabelProps={{ shrink: true }} error={!!errors.startDate} />
          <TextField label="Bitiş Tarihi" type="date" fullWidth margin="normal" {...register('endDate', { required: true })} InputLabelProps={{ shrink: true }} error={!!errors.endDate} />
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Kaydet</Button>
        </form>
      </Paper>
    </Box>
  );
}