'use client';
import { useEffect, useState } from 'react';
import { Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, CircularProgress, Button, Chip, IconButton, Snackbar, Alert } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import Link from 'next/link';
import api from '../../../services/api';
import ProtectedRoute from '../../../components/ProtectedRoute';

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

interface Expense {
  id: number;
  amount: number;
  date: string;
  description: string;
  type: string;
  vehicle?: { id: number; plateNumber: string };
  vendor?: { id: number; name: string };
  validatedBy?: { id: number; username: string };
}

export default function ExpensesPage() {
  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [snackbar, setSnackbar] = useState<{open: boolean, msg: string, type: 'success'|'error'}>({open: false, msg: '', type: 'success'});

  useEffect(() => {
    api.get('/expenses')
      .then(res => {
        setExpenses(res.data);
        console.log(res.data);
      })
      .catch(() => setError('Harcamalar yüklenemedi'))
      .finally(() => setLoading(false));
  }, []);

  const formatDate = (date: string) => new Date(date).toLocaleDateString('tr-TR');
  const formatAmount = (amount: number) => amount.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' });
  const getTypeColor = (type: string) => {
    switch (type) {
      case 'FUEL': return 'primary';
      case 'MAINTENANCE': return 'secondary';
      case 'INSURANCE': return 'info';
      case 'REPAIR': return 'warning';
      case 'TIRE': return 'success';
      default: return 'default';
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm('Bu harcamayı silmek istediğinize emin misiniz?')) {
      try {
        await api.delete(`/expenses/${id}`);
        setExpenses(expenses => expenses.filter(e => e.id !== id));
        setSnackbar({open:true, msg:'Harcama başarıyla silindi!', type:'success'});
      } catch {
        setSnackbar({open:true, msg:'Harcama silinemedi!', type:'error'});
      }
    }
  };

  if (loading) return (
    <ProtectedRoute>
      <Box p={4}>
        <Box display="flex" justifyContent="center" alignItems="center" minHeight={200}>
          <CircularProgress />
        </Box>
      </Box>
    </ProtectedRoute>
  );
  if (error) return (
    <ProtectedRoute>
      <Box p={4}>
        <Box display="flex" justifyContent="center" alignItems="center" minHeight={200}>
          <Typography color="error">{error}</Typography>
        </Box>
      </Box>
    </ProtectedRoute>
  );
  return (
    <ProtectedRoute>
      <Box p={4}>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography variant="h5">Harcamalar</Typography>
          <Button component={Link} href="/dashboard/expenses/new" variant="contained" startIcon={<AddIcon />}>Yeni Harcama</Button>
        </Box>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <StyledTableCell>Araç</StyledTableCell>
                <StyledTableCell>Tarih</StyledTableCell>
                <StyledTableCell>Tutar</StyledTableCell>
                <StyledTableCell>Açıklama</StyledTableCell>
                <StyledTableCell>Tip</StyledTableCell>
                <StyledTableCell>Tedarikçi</StyledTableCell>
                <StyledTableCell>Onaylayan</StyledTableCell>
                <StyledTableCell>İşlemler</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {expenses.map((e) => (
                <TableRow key={e.id}>
                  <TableCell>{e.vehicle?.plateNumber || '-'}</TableCell>
                  <TableCell>{formatDate(e.date)}</TableCell>
                  <TableCell>{formatAmount(e.amount)}</TableCell>
                  <TableCell>{e.description}</TableCell>
                  <TableCell><Chip label={e.type} color={getTypeColor(e.type)} /></TableCell>
                  <TableCell>{e.vendor?.name || '-'}</TableCell>
                  <TableCell>{e.validatedBy?.username || '-'}</TableCell>
                  <TableCell>

                    
                  <Button variant="outlined" color="primary" href={`/dashboard/expenses/${e.id}/edit`} sx={{ gap: 1, mr:1}} size="small">
                      <EditIcon fontSize="small" /> Düzenle
                    </Button>
                    <Button variant="outlined" color="error" onClick={() => handleDelete(e.id)} size="small" sx={{ gap: 1}}>
                      <DeleteIcon fontSize="small" sx={{display: 'flex', alignItems: 'center'}}/> Sil
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <Snackbar open={snackbar.open} autoHideDuration={2000} onClose={()=>setSnackbar(s=>({...s,open:false}))} anchorOrigin={{vertical:'bottom',horizontal:'right'}}>
          <Alert severity={snackbar.type} sx={{ width: '100%' }}>{snackbar.msg}</Alert>
        </Snackbar>
      </Box>
    </ProtectedRoute>
  );
}
