'use client';
import { useEffect, useState } from 'react';
import { Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, CircularProgress, IconButton, Button, Snackbar, Alert } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import Link from 'next/link';
import api from '@/services/api';
import AddIcon from '@mui/icons-material/Add';
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
export default function VendorsPage() {
  const [vendors, setVendors] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [snackbar, setSnackbar] = useState<{open: boolean, msg: string, type: 'success'|'error'}>({open: false, msg: '', type: 'success'});
  const user = useAuthStore.getState().user;
  const isAdmin = user?.role === 'ADMIN';

  useEffect(() => {
    api.get('/vendors')
      .then(res => {
        setVendors(res.data)
      })
      .catch(() => setError('Tedarikçiler yüklenemedi'))
      .finally(() => setLoading(false));
  }, []);

  const handleDelete = async (id: number) => {
    if (confirm('Tedarikçi silinsin mi?')) {
      try {
        await api.delete(`/vendors/${id}`);
        setVendors(vendors => vendors.filter(v => v.id !== id));
        setSnackbar({open:true, msg:'Tedarikçi başarıyla silindi!', type:'success'});
      } catch {
        setSnackbar({open:true, msg:'Tedarikçi silinemedi! İlgili tedarikçinin Harcamalarını siliniz', type:'error'});
      }
    }
  };

  return (
    <Box p={4}>

      <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
        <Typography variant="h5">Tedarikçiler</Typography>
        {isAdmin && (
          <Button component={Link} href="/dashboard/vendors/new" variant="contained" startIcon={<AddIcon />}>Yeni Tedarikçi</Button>
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
          <Table >
            <TableHead>
              <TableRow>
                <StyledTableCell>Ad</StyledTableCell>
                <StyledTableCell>Kullanıcı Adı</StyledTableCell>
                <StyledTableCell>Şirket Adı</StyledTableCell>
                {isAdmin && <StyledTableCell>İşlemler</StyledTableCell>}
              </TableRow>
            </TableHead>
            <TableBody>
              {vendors.map((v) => (
                <TableRow key={v.id}>
                  <StyledTableCell>{v.name}</StyledTableCell>
                  <StyledTableCell>{v.username}</StyledTableCell>
                  <StyledTableCell>{v.companyName}</StyledTableCell>
                  {isAdmin && (
                    <StyledTableCell>
                    <Button variant="outlined" color="primary" component={Link}  href={`/dashboard/vendors/${v.id}/edit`}  sx={{ gap: 1, mr:1}} size="small">
                      <EditIcon fontSize="small" /> Düzenle
                    </Button>
                    <Button variant="outlined" color="error" onClick={() => handleDelete(v.id)} size="small" sx={{ gap: 1}}>
                      <DeleteIcon fontSize="small" sx={{display: 'flex', alignItems: 'center'}}/> Sil
                    </Button>
                    </StyledTableCell>
                  )}
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
  );
}