'use client';
import { useEffect, useState } from 'react';
import { Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, CircularProgress, Button, IconButton, Snackbar, Alert } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import Link from 'next/link';
import api from '../../../services/api';
import ProtectedRoute from '../../../components/ProtectedRoute';
import { useAuthStore } from '@/stores/auth';

import { styled } from '@mui/material/styles';
import { tableCellClasses } from '@mui/material/TableCell';

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: '#52595d',
    color: theme.palette.common.white,
    fontWeight: 'bold',
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));


interface OdometerReading {
  id: number;
  date: string;
  km: number;
  vehicle?: { id: number; plateNumber?: string };
}


export default function OdometersPage() {
  const [readings, setReadings] = useState<OdometerReading[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [snackbar, setSnackbar] = useState<{open: boolean, msg: string, type: 'success'|'error'}>({open: false, msg: '', type: 'success'});

  const user = useAuthStore.getState().user;
  const isAdmin = user?.role === 'ADMIN';
  const isEmployee = user?.role === 'EMPLOYEE';
  const isVendor = user?.role === 'VENDOR';

  useEffect(() => {
    api.get('/readings')
      .then(res => setReadings(res.data))
      .catch(() => setError('Veriler yüklenemedi'))
      .finally(() => setLoading(false));
  }, []);

  if (isVendor) return <Typography color="error" p={4}>Bu sayfaya erişim yetkiniz yok.</Typography>;
  
  const handleDelete = async (id: number) => {
    if (confirm('Bu kilometre kaydını silmek istediğinize emin misiniz?')) {
      try {
        await api.delete(`/readings/${id}`);
        setReadings(readings => readings.filter(r => r.id !== id));
        setSnackbar({open:true, msg:'Kayıt başarıyla silindi!', type:'success'});
      } catch {
        setSnackbar({open:true, msg:'Kayıt silinemedi!', type:'error'});
      }
    }
  };

  const formatDate = (date: string) => new Date(date).toLocaleDateString('tr-TR');

  return (
    <ProtectedRoute>
      <Box p={4}>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography variant="h5">Kilometre Kayıtları</Typography>
          {(isAdmin || isEmployee) && (
            <Button component={Link} href="/dashboard/odometers/new" variant="contained" startIcon={<AddIcon />}>Yeni Kayıt</Button>
          )}
        </Box>
        {loading ? (
          <Box display="flex" justifyContent="center" alignItems="center" minHeight={200}>
            <CircularProgress />
          </Box>
        ) : error ? (
          <Typography color="error">{error}</Typography>
        ) : (
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <StyledTableCell>Plaka</StyledTableCell>
                  <StyledTableCell>Tarih</StyledTableCell>
                  <StyledTableCell>Kilometre</StyledTableCell>
                  <StyledTableCell>İşlemler</StyledTableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {readings.map((reading) => (
                  <TableRow key={reading.id}>
                    <StyledTableCell>{reading.vehicle?.plateNumber || '-'}</StyledTableCell>
                    <StyledTableCell>{formatDate(reading.date)}</StyledTableCell>
                    <StyledTableCell>{reading.km}</StyledTableCell>
                    <StyledTableCell>
                      {(isAdmin || isEmployee) && (
                        <>
                        <Button variant="outlined" color="primary" component={Link}  href={`/dashboard/odometers/${reading.id}/edit`}  sx={{ gap: 1, mr:1}} size="small">
                      <EditIcon fontSize="small" /> Düzenle
                    </Button>
                    <Button variant="outlined" color="error" onClick={() => handleDelete(reading.id)} size="small" sx={{ gap: 1}}>
                      <DeleteIcon fontSize="small" sx={{display: 'flex', alignItems: 'center'}}/> Sil
                    </Button>
                        </>
                      )}
                    </StyledTableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
        <Snackbar open={snackbar.open} autoHideDuration={2000} onClose={()=>setSnackbar(s=>({...s,open:false}))} anchorOrigin={{vertical:'bottom',horizontal:'right'}}>
          <Alert severity={snackbar.type} sx={{ width: '100%' }}>{snackbar.msg}</Alert>
        </Snackbar>
      </Box>
    </ProtectedRoute>
  );
}