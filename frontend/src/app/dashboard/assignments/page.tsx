'use client';
import { ReactNode, useEffect, useState } from 'react';
import { Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, CircularProgress, Button, Snackbar, Alert } from '@mui/material';
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
interface Assignment {
  mission: ReactNode;
  id: number;
  vehicle_id?: number;
  employee_id?: number;
  type: string;
  startDate: string;
  endDate: string;
  vehiclePlate?: string;
  employeeName?: string;
  vehicle?: Vehicle;
  employee?: Employee;
  status?: string;
}
interface Vehicle { id: number; plateNumber: string; }
interface Employee { id: number; name: string; }

export default function AssignmentsPage() {
  const [assignments, setAssignments] = useState<Assignment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [actionLoadingId, setActionLoadingId] = useState<number | null>(null);
  const [actionError, setActionError] = useState<string | null>(null);
  const [snackbar, setSnackbar] = useState<{open: boolean, msg: string, type: 'success'|'error'}>({open: false, msg: '', type: 'success'});

  const user = useAuthStore.getState().user;
  const isAdmin = user?.role === 'ADMIN';

  const formatDate = (date: string) => new Date(date).toLocaleDateString('tr-TR');

  const handleDelete = async (assignment: Assignment) => {
    if (!confirm('Bu atamayı silmek istediğinize emin misiniz?')) return;
    try {
      if (assignment.type === 'employee') {
        await api.delete(`/employee-assignments/${assignment.id}`);
      } else {
        await api.delete(`/pool-assignments/${assignment.id}`);
      }
      setAssignments(prev => prev.filter(a => a.id !== assignment.id));
      setSnackbar({open:true, msg:'Atama başarıyla silindi!', type:'success'});
    } catch (err) {
      setSnackbar({open:true, msg:'Silme işlemi başarısız!' + err, type:'error'});
    }
  };

  useEffect(() => {
    Promise.all([
      api.get('/employee-assignments'),
      api.get('/pool-assignments')
    ]).then(([ea, pa]) => {
      // Employee assignments: doğrudan objeden oku
      const employeeAssignments = ea.data.map((a: any) => ({
        ...a,
        vehiclePlate: a.vehicle?.plateNumber || '-',
        employeeName: a.employee?.name || '-',
        type: 'employee',
        status: a.approved === true ? 'APPROVED' : a.approved === false ? 'REJECTED' : 'PENDING',
      }));
      // Pool assignments: doğrudan objeden oku
      const poolAssignments = pa.data.map((a: any) => ({
        ...a,
        vehiclePlate: a.vehicle?.plateNumber || '-',
        employeeName: '-',
        mission: a.mission || '-',
        type: 'pool',
      }));
      setAssignments([...employeeAssignments, ...poolAssignments]);
    }).catch(() => setError('Atamalar yüklenemedi'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <ProtectedRoute>
      <Box p={4}>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography variant="h5">Atamalar</Typography>
          <Button component={Link} href="/dashboard/assignments/new" variant="contained" startIcon={<AddIcon />}>Yeni Atama</Button>
        </Box>
        {loading ? (
          <Box display="flex" justifyContent="center" alignItems="center" minHeight={200}>
            <CircularProgress />
          </Box>
        ) : error ? (
          <Box display="flex" justifyContent="center" alignItems="center" minHeight={200}><Typography color="error">{error}</Typography></Box>
        ) : (
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <StyledTableCell>Araç</StyledTableCell>
                  <StyledTableCell>Çalışan/Görev</StyledTableCell>
                  <StyledTableCell>Başlangıç</StyledTableCell>
                  <StyledTableCell>Bitiş</StyledTableCell>
                  <StyledTableCell>Tip</StyledTableCell>
                  <StyledTableCell>Durum</StyledTableCell>
                  <StyledTableCell>Aksiyon</StyledTableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {assignments.map((a) => (
                  <TableRow key={a.id}>
                    <StyledTableCell>{a.vehiclePlate}</StyledTableCell>
                    <StyledTableCell>{a.type === 'employee' ? a.employeeName : a.mission}</StyledTableCell>
                    <StyledTableCell>{formatDate(a.startDate)}</StyledTableCell>
                    <StyledTableCell>{formatDate(a.endDate)}</StyledTableCell>
                    <StyledTableCell>{a.type === 'employee' ? 'Çalışan' : 'Havuz'}</StyledTableCell>
                    <StyledTableCell>{a.status ? a.status : '-'}</StyledTableCell>
                    <StyledTableCell>
                      {a.type === 'employee' && isAdmin && (
                        <>
                          {a.status !== 'APPROVED' && (
                            <Button
                              size="small"
                              color="success"
                              variant="outlined"
                              sx={{ mr: 1 }}
                              disabled={actionLoadingId === a.id}
                              onClick={async () => {
                                setActionLoadingId(a.id);
                                setActionError(null);
                                try {
                                  const km = prompt('Onaylamak için km bilgisini girin:');
                                  if (!km || isNaN(Number(km))) throw new Error('Geçerli bir km girilmelidir.');
                                  const resp = await api.post(`/employee-assignments/${a.id}/approve?km=${km}`);
                                  setAssignments(prev => prev.map(item => item.id === a.id ? { ...item, status: resp.data.approved ? 'APPROVED' : 'PENDING' } : item));
                                } catch (err: any) {
                                  setActionError('Onaylama işlemi başarısız: ' + (err?.response?.data?.message || err.message || 'Bilinmeyen hata'));
                                } finally {
                                  setActionLoadingId(null);
                                }
                              }}
                            >{actionLoadingId === a.id ? '...' : 'Onayla'}</Button>
                          )}
                          {a.status !== 'REJECTED' && (
                            <Button
                              size="small"
                              color="error"
                              variant="outlined"
                              sx={{ mr: 1 }}
                              disabled={actionLoadingId === a.id}
                              onClick={async () => {
                                setActionLoadingId(a.id);
                                setActionError(null);
                                try {
                                  const resp = await api.post(`/employee-assignments/${a.id}/reject`);
                                  setAssignments(prev => prev.map(item => item.id === a.id ? { ...item, status: resp.data.approved === false ? 'REJECTED' : 'PENDING' } : item));
                                } catch (err: any) {
                                  setActionError('Reddetme işlemi başarısız: ' + (err?.response?.data?.message || err.message || 'Bilinmeyen hata'));
                                } finally {
                                  setActionLoadingId(null);
                                }
                              }}
                            >{actionLoadingId === a.id ? '...' : 'Reddet'}</Button>
                          )}
                        </>
                      )}

                    <Button variant="outlined" color="primary" component={Link} href={`/dashboard/assignments/${a.id}/edit?type=${a.type}`} sx={{ gap: 1, mr:1}} size="small">
                      <EditIcon fontSize="small" /> Düzenle
                    </Button>
                    <Button variant="outlined" color="error" onClick={() => handleDelete(a)} size="small" sx={{ gap: 1}}>
                      <DeleteIcon fontSize="small" sx={{display: 'flex', alignItems: 'center'}}/> Sil
                    </Button>
                      
                    </StyledTableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
        {actionError && (
          <Box mt={2} color="error.main">{actionError}</Box>
        )}
        <Snackbar open={snackbar.open} autoHideDuration={2000} onClose={()=>setSnackbar(s=>({...s,open:false}))} anchorOrigin={{vertical:'bottom',horizontal:'right'}}>
          <Alert severity={snackbar.type} sx={{ width: '100%' }}>{snackbar.msg}</Alert>
        </Snackbar>
      </Box>
    </ProtectedRoute>
  );
}