'use client';
import { useEffect, useState } from 'react';
import { Box, Button, CircularProgress, Typography, Chip, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, TableSortLabel, Snackbar, Alert } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import ZoomInIcon from '@mui/icons-material/ZoomIn';
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
interface Vehicle {
  id: number;
  plateNumber: string;
  brand: string;
  model: string;
  ownership: string;
  leaseStartDate: string;
  leaseEndDate: string;
  assignments?: any[];
  readings?: any[];
  expenses?: any[];
}

export default function VehiclesPage() {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [sortBy, setSortBy] = useState<'plateNumber'|'brand'|'model'|'ownership'>('plateNumber');
  const [sortDir, setSortDir] = useState<'asc'|'desc'>('asc');
  const [snackbar, setSnackbar] = useState<{open: boolean, msg: string, type: 'success'|'error'}>({open: false, msg: '', type: 'success'});
  const sortedVehicles = [...vehicles].sort((a, b) => {
    const valA = a[sortBy]?.toString().toLowerCase() || '';
    const valB = b[sortBy]?.toString().toLowerCase() || '';
    if (valA < valB) return sortDir === 'asc' ? -1 : 1;
    if (valA > valB) return sortDir === 'asc' ? 1 : -1;
    return 0;
  });

  useEffect(() => {
    api.get('/vehicles')
      .then(res => {
        setVehicles(Array.isArray(res.data) ? res.data : []);
      })
      .catch(() => setError('Araçlar yüklenemedi'))
      .finally(() => setLoading(false));
  }, []);

  const handleDelete = async (id: number) => {
    if (confirm('Bu aracı silmek istediğinize emin misiniz?')) {
      try {
        await api.delete(`/vehicles/${id}`);
        setVehicles(vehicles => vehicles.filter(v => v.id !== id));
        setSnackbar({open:true, msg:'Araç başarıyla silindi!', type:'success'});
      } catch {
        setSnackbar({open:true, msg:'Araç silinemedi!', type:'error'});
      }
    }
  };

  const user = useAuthStore.getState().user;
  const isAdmin = user?.role === 'ADMIN';
  const isVendor = user?.role === 'VENDOR';
  const isEmployee = user?.role === 'EMPLOYEE';

  if (loading) return <Box display="flex" justifyContent="center" alignItems="center" minHeight={200}><CircularProgress /></Box>;
  if (error) return <Box display="flex" justifyContent="center" alignItems="center" minHeight={200}><Typography color="error">{error}</Typography></Box>;

  return (
    <ProtectedRoute>
      <Box p={4}>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography variant="h5">Araçlar</Typography>
          {(isAdmin || isVendor) && (
            <Button component={Link} href="/dashboard/vehicles/new" variant="contained" startIcon={<AddIcon />}>Yeni Araç</Button>
          )}
        </Box>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <StyledTableCell>
                  <TableSortLabel active={sortBy==='plateNumber'} direction={sortDir} onClick={()=>{setSortBy('plateNumber');setSortDir(sortDir==='asc'?'desc':'asc')}}>
                    Plaka
                  </TableSortLabel>
                </StyledTableCell>
                <StyledTableCell>
                  <TableSortLabel active={sortBy==='brand'} direction={sortDir} onClick={()=>{setSortBy('brand');setSortDir(sortDir==='asc'?'desc':'asc')}}>
                    Marka
                  </TableSortLabel>
                </StyledTableCell>
                <StyledTableCell>
                  <TableSortLabel active={sortBy==='model'} direction={sortDir} onClick={()=>{setSortBy('model');setSortDir(sortDir==='asc'?'desc':'asc')}}>
                    Model
                  </TableSortLabel>
                </StyledTableCell>
                <StyledTableCell>
                  <TableSortLabel active={sortBy==='ownership'} direction={sortDir} onClick={()=>{setSortBy('ownership');setSortDir(sortDir==='asc'?'desc':'asc')}}>
                    Sahiplik
                  </TableSortLabel>
                </StyledTableCell>
                <StyledTableCell>Kiralama</StyledTableCell>
                <StyledTableCell>Atama</StyledTableCell>
                <StyledTableCell>Kilometre</StyledTableCell>
                <StyledTableCell>Harcama</StyledTableCell>
                <StyledTableCell align="center">İşlemler</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {sortedVehicles.map(vehicle => (
                <TableRow key={vehicle.id}>
                  <StyledTableCell>{vehicle.plateNumber || '-'}</StyledTableCell>
                  <StyledTableCell>{vehicle.brand || '-'}</StyledTableCell>
                  <StyledTableCell>{vehicle.model || '-'}</StyledTableCell>
                  <StyledTableCell>{vehicle.ownership || '-'}</StyledTableCell>
                  <StyledTableCell>{vehicle.leaseStartDate} - {vehicle.leaseEndDate}</StyledTableCell>
                  <StyledTableCell>
                    <Chip label={vehicle.assignments?.length ?? 0} color={vehicle.assignments?.length ? 'primary' : 'default'} />
                  </StyledTableCell>
                  <StyledTableCell>
                    <Chip label={vehicle.readings?.length ?? 0} color={vehicle.readings?.length ? 'primary' : 'default'} />
                  </StyledTableCell>
                  <StyledTableCell>
                    <Chip label={vehicle.expenses?.length ?? 0} color={vehicle.expenses?.length ? 'primary' : 'default'} />
                  </StyledTableCell>
                  <StyledTableCell align="center">
                    <Button component={Link} href={`/dashboard/vehicles/${vehicle.id}`} variant="outlined" size="small" sx={{ gap: 1, mr:1}} ><ZoomInIcon fontSize='small'/>Detay</Button>
                    {(isAdmin || isVendor) && (
                      <>
                      <Button variant="outlined" color="warning" component={Link} href={`/dashboard/vehicles/${vehicle.id}/edit`}  sx={{ gap: 1, mr:1}} size="small">
                      <EditIcon fontSize="small" /> Düzenle
                    </Button>
                    <Button variant="outlined" color="error" onClick={() => handleDelete(vehicle.id)} size="small" sx={{ gap: 1}}>
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
        <Snackbar open={snackbar.open} autoHideDuration={2000} onClose={()=>setSnackbar(s=>({...s,open:false}))} anchorOrigin={{vertical:'bottom',horizontal:'right'}}>
          <Alert severity={snackbar.type} sx={{ width: '100%' }}>{snackbar.msg}</Alert>
        </Snackbar>
      </Box>
    </ProtectedRoute>
  );
}