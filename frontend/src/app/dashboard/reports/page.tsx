'use client';
import { useEffect, useState } from 'react';
import { Box, Typography, Paper, CircularProgress, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, MenuItem, Select, InputLabel, FormControl, TextField, Button, Tabs, Tab } from '@mui/material';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
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

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

interface Vehicle { id: number; plateNumber: string; }
interface Report {
  assignmentCount: number;
  totalExp: number;
  expensesByType: any;
  mileageInPeriod: number;
  vehicleId: number;
  totalDistance: number;
  totalFuel: number;
  totalExpense: number;
  tripCount: number;
}

export default function FleetReportsPage() {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [reports, setReports] = useState<Record<number, Report>>({});
  const [oldReports, setOldReports] = useState<any[]>([]);
  const [selected, setSelected] = useState<number|null>(null);
  const [from, setFrom] = useState('2025-01-01');
  const [to, setTo] = useState('2025-12-31');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [odoData, setOdoData] = useState<any[]>([]);
  const [expenseData, setExpenseData] = useState<any[]>([]);
  const [tab, setTab] = useState(0);

  useEffect(() => {
    api.get('/vehicles').then(res => setVehicles(res.data));
    api.get('/reports').then(res => setOldReports(res.data));
  }, []);

  // Araç seçimi değişince rapor ve grafik verilerini sıfırla
  useEffect(() => {
    setReports({});
    setOdoData([]);
    setExpenseData([]);
    setError('');
  }, [selected, from, to]);

  const fetchReport = async (vehicleId: number) => {
    setLoading(true);
    setError('');
    try {
      if (vehicleId === 0) {
        // Tüm araçlar için rapor (query parametre ile gönder)
        const res = await api.post(`/reports?from=${from}&to=${to}`);
        const reportsObj: Record<number, Report> = {};
        let totalDistance = 0;
        let totalFuel = 0;
        let totalExpense = 0;
        let totalTrip = 0;
        res.data.forEach((r: any) => {
          reportsObj[r.vehicle.id] = {
            vehicleId: r.vehicle.id,
            totalDistance: r.mileageInPeriod,
            totalFuel: r.expensesByType?.FUEL || 0,
            totalExpense: r.totalExpense,
            tripCount: r.assignmentCount,
          };
          totalDistance += r.mileageInPeriod || 0;
          totalFuel += r.expensesByType?.FUEL || 0;
          totalExpense += r.totalExpense || 0;
          totalTrip += r.assignmentCount || 0;
        });
        setReports(reportsObj);
        // Grafik için toplamları tek bir veri olarak ata
        setOdoData([{ date: `${from} - ${to}`, km: totalDistance }]);
        setExpenseData([{ date: `${from} - ${to}`, amount: totalExpense }]);
      } else {
        // Tek araç için rapor (query parametre ile gönder)
        const res = await api.post(`/reports/${vehicleId}?from=${from}&to=${to}`);
        setReports(r => ({ ...r, [vehicleId]: res.data }));
        // Kilometre zaman serisi
        const odoRes = await api.get(`/readings/vehicle/${vehicleId}`);
        setOdoData(odoRes.data.filter((d: any) => new Date(d.date) >= new Date(from) && new Date(d.date) <= new Date(to)));
        // Harcama zaman serisi
        const expRes = await api.get('/expenses');
        setExpenseData(expRes.data.filter((e: any) => e.vehicleId === vehicleId && new Date(e.date) >= new Date(from) && new Date(e.date) <= new Date(to)));
      }
    } catch {
      setError('Rapor yüklenemedi');
    } finally {
      setLoading(false);
    }
  };

  const user = useAuthStore.getState().user;
  const isAdmin = user?.role === 'ADMIN';
  const isVendor = user?.role === 'VENDOR';
  const isEmployee = user?.role === 'EMPLOYEE';
  if (isEmployee) return <Typography color="error" p={4}>Bu sayfaya erişim yetkiniz yok.</Typography>;

  return (
    <ProtectedRoute>
      <Box p={4}>
        <Typography variant="h5" mb={2}>Filo Genel Raporları</Typography>
        <Tabs value={tab} onChange={(_, v) => setTab(v)} sx={{ mb: 2 }}>
          <Tab label="Yeni Rapor" />
          <Tab label="Eski Raporlar" />
        </Tabs>
        {tab === 0 && (
          <>
            <Box display="flex" gap={2} mb={2} width="100%">
              <FormControl sx={{ flex: 1 }}>
                <InputLabel>Araç</InputLabel>
                <Select
                  value={selected ?? ''}
                  label="Araç"
                  onChange={e => setSelected(Number(e.target.value))}
                  sx={{ minWidth: 120 }}
                >
                  <MenuItem value={0}>Tüm Araçlar</MenuItem>
                  {vehicles.map(v => (
                    <MenuItem key={v.id} value={v.id}>{v.plateNumber}</MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField label="Başlangıç" type="date" value={from} onChange={e => setFrom(e.target.value)} InputLabelProps={{ shrink: true }} sx={{ flex: 1 }} />
              <TextField label="Bitiş" type="date" value={to} onChange={e => setTo(e.target.value)} InputLabelProps={{ shrink: true }} sx={{ flex: 1 }} />
              <Button variant="contained" onClick={() => selected !== null && fetchReport(selected)} sx={{ height: 56, flex: 1 }}>Raporu Getir</Button>
            </Box>
            {loading ? (
              <Box display="flex" justifyContent="center" alignItems="center" minHeight={120}><CircularProgress /></Box>
            ) : error ? (
              <Typography color="error">{error}</Typography>
            ) : selected === 0 && Object.values(reports).length > 0 ? (
                <>
                  <TableContainer component={Paper} sx={{ width: '100%', my: 2 }}>
                    <Table size="small">
                      <TableHead>
                        <TableRow>
                          <StyledTableCell>Araç</StyledTableCell>
                          <StyledTableCell>Toplam KM</StyledTableCell>
                          <StyledTableCell>Toplam Yakıt</StyledTableCell>
                          <StyledTableCell>Toplam Harcama</StyledTableCell>
                          <StyledTableCell>Görev Sayısı</StyledTableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {Object.values(reports).map((r: any) => {
                          const vehicleId = r.vehicleId || r.vehicle?.id;
                          const v = vehicles.find(v => v.id === vehicleId);
                          return (
                            <TableRow key={vehicleId}>
                              <StyledTableCell>{v?.plateNumber || vehicleId}</StyledTableCell>
                              <StyledTableCell>{(r.totalDistance ?? r.mileageInPeriod)?.toLocaleString('tr-TR')} km</StyledTableCell>
                              <StyledTableCell>{(r.totalFuel ?? (r.expensesByType?.FUEL || 0))?.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' })}</StyledTableCell>
                              <StyledTableCell>{(r.totalExpense ?? r.totalExp)?.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' })}</StyledTableCell>
                              <StyledTableCell>{(r.tripCount ?? r.assignmentCount)}</StyledTableCell>
                            </TableRow>
                          );
                        })}
                      </TableBody>
                    </Table>
                  </TableContainer>
                  <Box display="flex" gap={2} width="100%" mt={2}>
                    <Paper sx={{ flex: 1, p: 2 }}>
                      <Typography variant="subtitle1" mb={1}>Tüm Araçlar Toplam KM Grafiği</Typography>
                      <Line
                        data={{
                          labels: Object.values(reports).map((r: any) => {
                            const v = vehicles.find(v => v.id === (r.vehicleId || r.vehicle?.id));
                            return v?.plateNumber || (r.vehicleId || r.vehicle?.id);
                          }),
                          datasets: [
                            {
                              label: 'Toplam KM',
                              data: Object.values(reports).map((r: any) => r.totalDistance ?? r.mileageInPeriod ?? 0),
                              borderColor: '#1976d2',
                              backgroundColor: 'rgba(25,118,210,0.1)',
                            },
                          ],
                        }}
                        options={{
                          responsive: true,
                          plugins: { legend: { position: 'top' }, title: { display: false } },
                          scales: {
                            y: { title: { display: true, text: 'KM' } },
                          },
                        }}
                      />
                    </Paper>
                    <Paper sx={{ flex: 1, p: 2 }}>
                      <Typography variant="subtitle1" mb={1}>Tüm Araçlar Toplam Harcama Grafiği</Typography>
                      <Line
                        data={{
                          labels: Object.values(reports).map((r: any) => {
                            const v = vehicles.find(v => v.id === (r.vehicleId || r.vehicle?.id));
                            return v?.plateNumber || (r.vehicleId || r.vehicle?.id);
                          }),
                          datasets: [
                            {
                              label: 'Toplam Harcama',
                              data: Object.values(reports).map((r: any) => r.totalExpense ?? r.totalExp ?? 0),
                              borderColor: '#d32f2f',
                              backgroundColor: 'rgba(211,47,47,0.1)',
                            },
                          ],
                        }}
                        options={{
                          responsive: true,
                          plugins: { legend: { position: 'top' }, title: { display: false } },
                          scales: {
                            y: { title: { display: true, text: 'Harcama (₺)' } },
                          },
                        }}
                      />
                    </Paper>
                  </Box>
                </>
            ) : selected && selected !== 0 && reports[selected] ? (
              <TableContainer component={Paper} sx={{ width: '100%' }}>
                <Table size="small">
                  <TableHead>
                    <TableRow>
                      <StyledTableCell>Toplam KM</StyledTableCell>
                      <StyledTableCell>Toplam Yakıt</StyledTableCell>
                      <StyledTableCell>Toplam Harcama</StyledTableCell>
                      <StyledTableCell>Görev Sayısı</StyledTableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    <TableRow>
                      <StyledTableCell>{(reports[selected].totalDistance ?? reports[selected].mileageInPeriod)?.toLocaleString('tr-TR')} km</StyledTableCell>
                      <StyledTableCell>{(reports[selected].totalFuel ?? (reports[selected].expensesByType?.FUEL || 0))?.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' })}</StyledTableCell>
                      <StyledTableCell>{(reports[selected].totalExpense ?? reports[selected].totalExp)?.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' })}</StyledTableCell>
                      <StyledTableCell>{(reports[selected].tripCount ?? reports[selected].assignmentCount)}</StyledTableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            ) : null}
            {selected && selected !== 0 && (odoData.length > 0 || expenseData.length > 0) && (
              <Box display="flex" gap={2} width="100%" mt={2}>
                <Paper sx={{ flex: 1, p: 2 }}>
                  <Typography variant="subtitle1" mb={1}>Zaman Serisi KM Grafiği</Typography>
                  <Line
                    data={{
                      labels: odoData.map(d => d.date),
                      datasets: [
                        {
                          label: 'Kilometre',
                          data: odoData.map(d => d.km),
                          borderColor: '#1976d2',
                          backgroundColor: 'rgba(25,118,210,0.1)',
                        },
                      ],
                    }}
                    options={{
                      responsive: true,
                      plugins: { legend: { position: 'top' }, title: { display: false } },
                      scales: {
                        y: { title: { display: true, text: 'KM' } },
                      },
                    }}
                  />
                </Paper>
                <Paper sx={{ flex: 1, p: 2 }}>
                  <Typography variant="subtitle1" mb={1}>Zaman Serisi Harcama Grafiği</Typography>
                  <Line
                    data={{
                      labels: expenseData.map(e => e.date),
                      datasets: [
                        {
                          label: 'Harcama',
                          data: expenseData.map(e => e.amount),
                          borderColor: '#d32f2f',
                          backgroundColor: 'rgba(211,47,47,0.1)',
                        },
                      ],
                    }}
                    options={{
                      responsive: true,
                      plugins: { legend: { position: 'top' }, title: { display: false } },
                      scales: {
                        y: { title: { display: true, text: 'Harcama (₺)' } },
                      },
                    }}
                  />
                </Paper>
              </Box>
            )}
          </>
        )}
        {tab === 1 && (
          <Box display="flex" flexWrap="wrap" gap={2}>
            {oldReports.length === 0 && <Typography>Hiç eski rapor yok.</Typography>}
            {oldReports.map((r: any, i: number) => (
              <Paper key={i} sx={{ flex: '1 1 30%', minWidth: 250, p: 2, mb: 2, background: 'rgba(56, 139, 253, 0.16)' }}>
                <pre style={{ margin: 0, fontSize: 13 }}>{r.content}</pre>
              </Paper>
            ))}
          </Box>
        )}
      </Box>
    </ProtectedRoute>
  );
}
