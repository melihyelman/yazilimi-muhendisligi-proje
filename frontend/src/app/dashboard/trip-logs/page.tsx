"use client";
import { useEffect, useState } from 'react';
import { Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, CircularProgress, TextField, MenuItem, Button, Snackbar, Alert } from '@mui/material';
import api from '@/services/api';

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
interface TripLog {
  id: number;
  km: number;
  assignment: {
    id: number;
    startDate: string;
    endDate: string;
    employeeId: number;
    vehicleId: number;
    approved: boolean;
  };
}
interface Vehicle { id: number; plateNumber: string; }
interface Employee { id: number; name: string; }

export default function TripLogsPage() {
  const [tripLogs, setTripLogs] = useState<TripLog[]>([]);
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [assignments, setAssignments] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({ assignmentId: '', vehicleId: '', employeeId: '' });
  const [dateRange, setDateRange] = useState<{from: string, to: string}>({from: '', to: ''});
  const [snackbar, setSnackbar] = useState<{open: boolean, msg: string, type: 'success'|'error'}>({open: false, msg: '', type: 'success'});

  useEffect(() => {
    Promise.all([
      api.get('/trip-logs'),
      api.get('/vehicles'),
      api.get('/employees'),
      api.get('/employee-assignments')
    ]).then(([logsRes, vehiclesRes, employeesRes, assignmentsRes]) => {
      setTripLogs(logsRes.data);
      setVehicles(vehiclesRes.data);
      setEmployees(employeesRes.data);
      setAssignments(assignmentsRes.data);
    }).finally(() => setLoading(false));
  }, []);

  const filteredLogs = tripLogs.filter(log => {
    const { assignmentId, vehicleId, employeeId } = filters;
    const assignment = assignments.find(a => a.id === log.assignment.id);
    const assignmentLabel = assignment ? `${assignment.employee?.name || '-'} / ${assignment.vehicle?.plateNumber || '-'}` : '';
    const logDate = log.assignment.startDate;
    const inDateRange = (!dateRange.from || logDate >= dateRange.from) && (!dateRange.to || logDate <= dateRange.to);
    return (
      (!assignmentId || log.assignment.id === Number(assignmentId)) &&
      (!vehicleId || log.assignment.vehicleId === Number(vehicleId)) &&
      (!employeeId || log.assignment.employeeId === Number(employeeId)) &&
      inDateRange
    );
  });

  // Trip log silme örneği (ekle/düzenle yoksa sadece silme için):
  const handleDelete = async (id: number) => {
    if (confirm('Bu seyahat kaydını silmek istediğinize emin misiniz?')) {
      try {
        await api.delete(`/trip-logs/${id}`);
        setTripLogs(logs => logs.filter(l => l.id !== id));
        setSnackbar({open:true, msg:'Seyahat kaydı başarıyla silindi!', type:'success'});
      } catch {
        setSnackbar({open:true, msg:'Seyahat kaydı silinemedi!', type:'error'});
      }
    }
  };

  return (
    <Box p={4}>
      <Typography variant="h5" mb={2}>Seyahat Kayıtları</Typography>
      <Paper sx={{ p: 2, mb: 2 }}>
        <Box display="flex" gap={2} flexWrap="wrap">
          <TextField
            select
            label="Atama"
            value={filters.assignmentId}
            onChange={e => setFilters(f => ({ ...f, assignmentId: e.target.value }))}
            size="small"
            sx={{ minWidth: 200 }}
          >
            <MenuItem value="">Tümü</MenuItem>
            {assignments.map(a => (
              <MenuItem key={a.id} value={a.id}>
                {a.employee?.name || '-'} / {a.vehicle?.plateNumber || '-'}
              </MenuItem>
            ))}
          </TextField>
          <TextField
            select
            label="Araç"
            value={filters.vehicleId}
            onChange={e => setFilters(f => ({ ...f, vehicleId: e.target.value }))}
            size="small"
            sx={{ minWidth: 120 }}
          >
            <MenuItem value="">Tümü</MenuItem>
            {vehicles.map(v => (
              <MenuItem key={v.id} value={v.id}>{v.plateNumber}</MenuItem>
            ))}
          </TextField>
          <TextField
            select
            label="Çalışan"
            value={filters.employeeId}
            onChange={e => setFilters(f => ({ ...f, employeeId: e.target.value }))}
            size="small"
            sx={{ minWidth: 120 }}
          >
            <MenuItem value="">Tümü</MenuItem>
            {employees.map(e => (
              <MenuItem key={e.id} value={e.id}>{e.name}</MenuItem>
            ))}
          </TextField>
          <TextField
            label="Başlangıç Tarihi"
            type="date"
            size="small"
            value={dateRange.from}
            onChange={e => setDateRange(r => ({ ...r, from: e.target.value }))}
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            label="Bitiş Tarihi"
            type="date"
            size="small"
            value={dateRange.to}
            onChange={e => setDateRange(r => ({ ...r, to: e.target.value }))}
            InputLabelProps={{ shrink: true }}
          />
          <Button variant="outlined" onClick={() => { setFilters({ assignmentId: '', vehicleId: '', employeeId: '' }); setDateRange({from: '', to: ''}); }}>Filtreleri Temizle</Button>
        </Box>
      </Paper>
      {loading ? (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight={200}>
          <CircularProgress />
        </Box>
      ) : (
        <TableContainer component={Paper}>
          <Table size="small">
            <TableHead>
              <TableRow>
                <StyledTableCell>Log ID</StyledTableCell>
                <StyledTableCell>KM</StyledTableCell>
                <StyledTableCell>Atama ID</StyledTableCell>
                <StyledTableCell>Araç</StyledTableCell>
                <StyledTableCell>Çalışan</StyledTableCell>
                <StyledTableCell>Başlangıç</StyledTableCell>
                <StyledTableCell>Bitiş</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredLogs.map(log => {
                const vehicle = vehicles.find(v => v.id === log.assignment.vehicleId);
                const employee = employees.find(e => e.id === log.assignment.employeeId);
                return (
                  <TableRow key={log.id}>
                    <StyledTableCell>{log.id}</StyledTableCell>
                    <StyledTableCell>{log.km}</StyledTableCell>
                    <StyledTableCell>{log.assignment.id}</StyledTableCell>
                    <StyledTableCell>{vehicle?.plateNumber || '-'}</StyledTableCell>
                    <StyledTableCell>{employee?.name || '-'}</StyledTableCell>
                    <StyledTableCell>{log.assignment.startDate}</StyledTableCell>
                    <StyledTableCell>{log.assignment.endDate}</StyledTableCell>
                  </TableRow>
                );
              })}
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
