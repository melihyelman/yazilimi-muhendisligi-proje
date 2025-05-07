"use client";
import { useForm, Controller } from 'react-hook-form';
import { useEffect, useState } from 'react';
import { Box, Button, Paper, TextField, Typography, MenuItem, Select, InputLabel, FormControl, CircularProgress } from '@mui/material';
import api from '../../../../../services/api';
import { useRouter, useParams } from 'next/navigation';

interface ExpenseFormInputs {
  vehicleId: string;
  vendorId: string;
  validatedById: string;
  amount: number;
  date: string;
  description: string;
  type: string;
}

interface Vehicle { id: number; plateNumber: string; }
interface Vendor { id: number; name: string; }
interface User { id: number; username: string; role: string; }

export default function ExpenseEditPage() {
  const { id } = useParams<{ id: string }>();
  const { register, handleSubmit, control, setValue, formState: { errors } } = useForm<ExpenseFormInputs>();
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [expense, setExpense] = useState<any>(null);
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [vendors, setVendors] = useState<Vendor[]>([]);
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    Promise.all([
      api.get('/vehicles'),
      api.get('/vendors'),
      api.get('/users'),
      api.get(`/expenses/${id}`)
    ]).then(([v, ven, usr, exp]) => {
      setVehicles(v.data);
      setVendors(ven.data);
      console.log(usr.data);
      setExpense(exp.data);
      setValue('vehicleId', exp.data.vehicle?.id?.toString() || '');
      setValue('vendorId', exp.data.vendor?.id?.toString() || '');
      setValue('validatedById', exp.data.validatedBy?.id?.toString() || '');
      setValue('amount', exp.data.amount);
      setValue('date', exp.data.date);
      setValue('description', exp.data.description);
      setValue('type', exp.data.type);
    }).finally(() => setLoading(false));
  }, [id, setValue]);

  const onSubmit = async (data: ExpenseFormInputs) => {
    await api.put(`/expenses/${id}?vehicleId=${Number(data.vehicleId)}&vendorId=${Number(data.vendorId)}&validatedBy=${Number(data.validatedById)}`, {
      amount: Number(data.amount),
      date: data.date,
      description: data.description,
      type: data.type
    });
    router.push('/dashboard/expenses');
  };

  if (loading) return <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh"><CircularProgress /></Box>;
  if (!expense) return null;

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400 }}>
        <Typography variant="h6" mb={2}>Harcamayı Düzenle</Typography>
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
                  onChange={e => field.onChange(e.target.value)}
                >
                  {vehicles.map((v) => (
                    <MenuItem key={v.id} value={v.id}>{v.plateNumber}</MenuItem>
                  ))}
                </Select>
              )}
            />
          </FormControl>
          <FormControl fullWidth margin="normal">
            <InputLabel>Tedarikçi</InputLabel>
            <Controller
              name="vendorId"
              control={control}
              rules={{ required: true }}
              render={({ field }) => (
                <Select
                  {...field}
                  label="Tedarikçi"
                  error={!!errors.vendorId}
                  value={field.value || ''}
                  onChange={e => field.onChange(e.target.value)}
                >
                  {vendors.map((v) => (
                    <MenuItem key={v.id} value={v.id}>{v.name}</MenuItem>
                  ))}
                </Select>
              )}
            />
          </FormControl>
          <FormControl fullWidth margin="normal">
            <InputLabel>Onaylayan</InputLabel>
            <Controller
              name="validatedById"
              control={control}
              rules={{ required: true }}
              render={({ field }) => (
                <Select
                  {...field}
                  label="Onaylayan"
                  error={!!errors.validatedById}
                  value={field.value || ''}
                  onChange={e => field.onChange(e.target.value)}
                >
                  {users.map((u) => (
                    <MenuItem key={u.id} value={u.id}>{u.username}</MenuItem>
                  ))}
                </Select>
              )}
            />
          </FormControl>
          <TextField label="Tutar" type="number" fullWidth margin="normal" {...register('amount', { required: true, min: 0 })} error={!!errors.amount} />
          <TextField label="Tarih" type="date" fullWidth margin="normal" {...register('date', { required: true })} InputLabelProps={{ shrink: true }} error={!!errors.date} />
          <TextField label="Açıklama" fullWidth margin="normal" {...register('description', { required: true })} error={!!errors.description} />
          <FormControl fullWidth margin="normal">
            <InputLabel>Tip</InputLabel>
            <Controller
              name="type"
              control={control}
              rules={{ required: true }}
              render={({ field }) => (
                <Select
                  {...field}
                  label="Tip"
                  error={!!errors.type}
                  value={field.value || ''}
                >
                  <MenuItem value="MAINTENANCE">Bakım</MenuItem>
                  <MenuItem value="INSURANCE">Sigorta</MenuItem>
                  <MenuItem value="FUEL">Yakıt</MenuItem>
                  <MenuItem value="REPAIR">Onarım</MenuItem>
                  <MenuItem value="TIRE">Lastik</MenuItem>
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