'use client';
import { useForm, Controller } from 'react-hook-form';
import { Box, Button, Paper, TextField, Typography, CircularProgress, MenuItem, Select, InputLabel, FormControl, Snackbar, Alert } from '@mui/material';
import api from '../../../../../services/api';
import { useRouter, useParams } from 'next/navigation';
import { useEffect, useState } from 'react';

interface VehicleFormInputs {
  plateNumber: string;
  brand: string;
  model: string;
  ownership: string;
  leaseStartDate: string;
  leaseEndDate: string;
}

export default function VehicleEditPage() {
  const { id } = useParams<{ id: string }>();
  const { register, handleSubmit, setValue, control, formState: { errors }, watch } = useForm<VehicleFormInputs>({
    defaultValues: {
      plateNumber: '',
      brand: '',
      model: '',
      ownership: '',
      leaseStartDate: '',
      leaseEndDate: ''
    }
  });
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [snackbar, setSnackbar] = useState<{open: boolean, msg: string, type: 'success'|'error'}>({open: false, msg: '', type: 'success'});

  const ownership = watch('ownership');

  useEffect(() => {
    api.get(`/vehicles/${id}`)
      .then(res => {
        const v = res.data;
        setValue('plateNumber', v.plateNumber);
        setValue('brand', v.brand);
        setValue('model', v.model);
        setValue('ownership', v.ownership);
        setValue('leaseStartDate', v.leaseStartDate);
        setValue('leaseEndDate', v.leaseEndDate);
      })
      .catch(() => setError('Araç bulunamadı'))
      .finally(() => setLoading(false));
  }, [id, setValue]);

  const onSubmit = async (data: VehicleFormInputs) => {
    const payload: any = {
      plateNumber: data.plateNumber,
      brand: data.brand,
      model: data.model,
      ownership: data.ownership
    };
    if (data.ownership === 'LEASED') {
      payload.leaseStartDate = data.leaseStartDate;
      payload.leaseEndDate = data.leaseEndDate;
    }
    try {
      await api.put(`/vehicles/${id}`, payload);
      setSnackbar({open:true, msg:'Araç başarıyla güncellendi!', type:'success'});
      setTimeout(()=>router.push('/dashboard/vehicles'), 1200);
    } catch {
      setSnackbar({open:true, msg:'Araç güncellenemedi!', type:'error'});
    }
  };

  if (loading) return <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh"><CircularProgress /></Box>;
  if (error) return <Typography color="error">{error}</Typography>;

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
      <Paper sx={{ p: 4, minWidth: 400, borderRadius: 3, boxShadow: 3 }}>
        <Typography variant="h5" mb={2} fontWeight={700}>Araç Düzenle</Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <TextField label="Plaka" fullWidth margin="normal" {...register('plateNumber', { required: true })} error={!!errors.plateNumber} />
          <TextField label="Marka" fullWidth margin="normal" {...register('brand', { required: true })} error={!!errors.brand} />
          <TextField label="Model" fullWidth margin="normal" {...register('model', { required: true })} error={!!errors.model} />
          <FormControl fullWidth margin="normal">
            <InputLabel>Sahiplik</InputLabel>
            <Controller
              name="ownership"
              control={control}
              rules={{ required: true }}
              render={({ field }) => (
                <Select {...field} label="Sahiplik" error={!!errors.ownership}>
                  <MenuItem value="OWNED">Sahipli</MenuItem>
                  <MenuItem value="LEASED">Kiralık</MenuItem>
                </Select>
              )}
            />
          </FormControl>
          {ownership === 'LEASED' && (
            <>
              <TextField label="Kiralama Başlangıç" type="date" fullWidth margin="normal" {...register('leaseStartDate', { required: true })} InputLabelProps={{ shrink: true }} error={!!errors.leaseStartDate} />
              <TextField label="Kiralama Bitiş" type="date" fullWidth margin="normal" {...register('leaseEndDate', { required: true })} InputLabelProps={{ shrink: true }} error={!!errors.leaseEndDate} />
            </>
          )}
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }} size="large">Kaydet</Button>
        </form>
        <Snackbar open={snackbar.open} autoHideDuration={2000} onClose={()=>setSnackbar(s=>({...s,open:false}))} anchorOrigin={{vertical:'bottom',horizontal:'right'}}>
          <Alert severity={snackbar.type} sx={{ width: '100%' }}>{snackbar.msg}</Alert>
        </Snackbar>
      </Paper>
    </Box>
  );
}