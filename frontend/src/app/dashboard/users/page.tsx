'use client';
import { useEffect, useState } from 'react';
import { Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, CircularProgress, IconButton } from '@mui/material';
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
interface User {
  id: number;
  username: string;
  role: string;
}

export default function UsersPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/users')
      .then(res => setUsers(res.data))
      .catch(() => setError('Kullanıcılar yüklenemedi'))
      .finally(() => setLoading(false));
  }, []);

  const handleDelete = async (id: number) => {
    if (confirm('Kullanıcı silinsin mi?')) {
      await api.delete(`/users/${id}`);
      setUsers(users => users.filter(u => u.id !== id));
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
        <Typography color="error">{error}</Typography>
      </Box>
    </ProtectedRoute>
  );
  return (
    <ProtectedRoute>
      <Box p={4}>
        <Typography variant="h5" mb={2}>Kullanıcılar</Typography>
        <TableContainer component={Paper}>
          <Table size="small">
            <TableHead>
              <TableRow>
                <StyledTableCell>Kullanıcı Adı</StyledTableCell>
                <StyledTableCell>Rol</StyledTableCell>
                <StyledTableCell>İşlemler</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((u) => (
                <TableRow key={u.id}>
                  <StyledTableCell>{u.username}</StyledTableCell>
                  <StyledTableCell>{u.role}</StyledTableCell>
                  <StyledTableCell>
                    <IconButton color="primary" component={Link} href={`/dashboard/users/${u.id}/edit`} size="small">
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton color="error" onClick={() => handleDelete(u.id)} size="small">
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </StyledTableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </ProtectedRoute>
  );
}
