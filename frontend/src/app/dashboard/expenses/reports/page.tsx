'use client';
import { useEffect, useState } from 'react';
import { Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, CircularProgress, Button, MenuItem, Select, FormControl, InputLabel } from '@mui/material';
import { Grid } from '@mui/material';
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
import api from '@/services/api';
import ProtectedRoute from '../../../../components/ProtectedRoute';
import { useTheme } from '@mui/material/styles';

const PERIODS = [1, 3, 6, 9, 12];
const EXPENSE_TYPES = ['FUEL', 'MAINTENANCE', 'INSURANCE', 'REPAIR', 'TIRE'];
const CHART_COLORS = [
  '#1976d2', // blue
  '#d32f2f', // red
  '#388e3c', // green
  '#fbc02d', // yellow
  '#7b1fa2', // purple
  '#ff9800', // orange
];

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

export default function ExpenseReportsPage() {
  const [expenses, setExpenses] = useState<any[]>([]);
  const [forecast, setForecast] = useState<Record<string, number>>({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [datePeriod, setDatePeriod] = useState(3); 
  const theme = useTheme();

  useEffect(() => {
    setLoading(true);
    Promise.all([
      api.get('/expenses'),
      api.get(`/forecast?period=${datePeriod}`)
    ]).then(([expRes, forecastRes]) => {
      setExpenses(expRes.data);
      setForecast(forecastRes.data || {});
    }).catch(() => setError('Veriler yüklenemedi')).finally(() => setLoading(false));
  }, [datePeriod]);

  const now = new Date();
  const filteredExpenses = expenses.filter((e: any) => {
    if (!e.date) return false;
    const expDate = new Date(e.date);
    const diffMonth = (now.getFullYear() - expDate.getFullYear()) * 12 + (now.getMonth() - expDate.getMonth());
    return diffMonth < datePeriod;
  });
  const groupedByType: Record<string, number> = {};
  const groupedByMonth: Record<string, Record<string, number>> = {};
  filteredExpenses.forEach((e: any) => {
    if (!groupedByType[e.type]) groupedByType[e.type] = 0;
    groupedByType[e.type] += e.amount || 0;
    const month = e.date ? e.date.slice(0, 7) : '';
    if (month) {
      if (!groupedByMonth[month]) groupedByMonth[month] = {};
      if (!groupedByMonth[month][e.type]) groupedByMonth[month][e.type] = 0;
      groupedByMonth[month][e.type] += e.amount || 0;
    }
  });
  const total = filteredExpenses.reduce((sum, e) => sum + (e.amount || 0), 0);
  const months = Object.keys(groupedByMonth).sort();

  return (
    <ProtectedRoute>
      <Box p={4}>
        <Typography variant="h5" mb={2}>Harcama Raporları</Typography>
        <Paper sx={{ p: 2, mb: 3 }}>
          <Grid container spacing={2} alignItems="center">
            <Grid >
              <FormControl sx={{ minWidth: 180 }}>
                <InputLabel>Rapor Dönemi</InputLabel>
                <Select value={datePeriod} label="Rapor Dönemi" onChange={e => setDatePeriod(Number(e.target.value))}>
                  {PERIODS.map(p => (
                    <MenuItem key={p} value={p}>{p} Ay</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
          </Grid>
        </Paper>
        {loading ? <CircularProgress /> : error ? <Typography color="error">{error}</Typography> : (
          <>
            <Paper sx={{ p: 2, mb: 3 }}>
              <Typography variant="subtitle1" fontWeight={600}>Harcamalar Tablosu</Typography>
              <TableContainer>
                <Table size="small">
                  <TableHead>
                    <TableRow>
                      <TableCell>Tip</TableCell>
                      <TableCell>Toplam</TableCell>
                      <TableCell>Hareketli Ortalama (Gelecek Ay)</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {EXPENSE_TYPES.map((type, idx) => (
                      <TableRow key={type}>
                        <TableCell>{type}</TableCell>
                        <TableCell>{(groupedByType[type] || 0).toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' })}</TableCell>
                        <TableCell>{forecast[type] !== undefined ? forecast[type].toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' }) : '-'}</TableCell>
                      </TableRow>
                    ))}
                    <TableRow>
                      <TableCell><b>Genel Toplam</b></TableCell>
                      <TableCell><b>{total.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' })}</b></TableCell>
                      <TableCell><b>{Object.values(forecast).length > 0 ? Object.values(forecast).reduce((a, b) => a + b, 0).toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' }) : '-'}</b></TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </Paper>
            <Paper sx={{ p: 2, mb: 3 }}>
              <Typography variant="subtitle1" fontWeight={600}>Aylara Göre Harcama Dağılımı</Typography>
              <Line
                data={{
                  labels: months,
                  datasets: EXPENSE_TYPES.map((type, idx) => ({
                    label: type,
                    data: months.map(m => groupedByMonth[m]?.[type] || 0),
                    borderColor: CHART_COLORS[idx % CHART_COLORS.length],
                    backgroundColor: CHART_COLORS[idx % CHART_COLORS.length] + '33',
                    pointBackgroundColor: CHART_COLORS[idx % CHART_COLORS.length],
                    pointBorderColor: CHART_COLORS[idx % CHART_COLORS.length],
                    pointRadius: 6,
                    pointHoverRadius: 10,
                    pointBorderWidth: 2,
                    tension: 0.3,
                  }))
                }}
                options={{
                  responsive: true,
                  plugins: {
                    legend: {
                      position: 'top',
                      labels: {
                        color: '#fff',
                      }
                    },
                    title: {
                      display: false
                    },
                  },
                  scales: {
                    x: {
                      ticks: { color: '#fff' },
                      grid: { color: '#52595d' }
                    },
                    y: {
                      ticks: { color: '#fff' },
                      grid: { color: '#52595d' }
                    }
                  }
                }}
              />
            </Paper>
          </>
        )}
      </Box>
    </ProtectedRoute>
  );
}
