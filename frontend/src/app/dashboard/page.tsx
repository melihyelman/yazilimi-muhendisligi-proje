'use client';
import { useEffect, useState } from 'react';
import { Box, Typography, Paper, CircularProgress } from '@mui/material';
import api from '../../services/api';
import Grid from '@mui/material/Grid';
import ProtectedRoute from '../../components/ProtectedRoute';
import { TrendingUp, LocalGasStation, Build, DirectionsCar, Assignment, Speed, MonetizationOn, People, ReceiptLong } from '@mui/icons-material';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { styled } from '@mui/material/styles';
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

// Kartlar için daha modern bir görünüm
const StatCard = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(3),
  textAlign: 'center',
  borderRadius: 18,
  boxShadow: '0 4px 24px 0 rgba(0,0,0,0.07)',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  gap: theme.spacing(1),
  minHeight: 140,
}));
const StatIconBox = styled('div')(({ color }) => ({
  background: color,
  borderRadius: '50%',
  width: 56,
  height: 56,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  marginBottom: 8,
}));
const ListPaper = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(2),
  borderRadius: 16,
  minHeight: 180,
  boxShadow: '0 2px 12px 0 rgba(0,0,0,0.04)',
}));
const ListUl = styled('ul')(() => ({
  margin: 0,
  paddingLeft: 0,
  listStyle: 'none',
}));
const ListLi = styled('li')(() => ({
  padding: '8px 0',
  borderBottom: '1px solid #52595d',
  fontSize: 15,
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
}));

export default function DashboardPage() {
  const [stats, setStats] = useState({
    vehicles: 0,
    assignments: 0,
    odometers: 0,
    expenses: 0,
    totalKm: 0,
    totalExpense: 0,
  });
  const [loading, setLoading] = useState(true);
  const [lastExpenses, setLastExpenses] = useState<any[]>([]);
  const [lastOdometers, setLastOdometers] = useState<any[]>([]);
  const [lastAssignments, setLastAssignments] = useState<any[]>([]);
  const [expenseTypeStats, setExpenseTypeStats] = useState<Record<string, number>>({});
  const [monthlyExpense, setMonthlyExpense] = useState<{month: string, total: number}[]>([]);
  const [employeeCount, setEmployeeCount] = useState(0);
  const [vendorCount, setVendorCount] = useState(0);

  useEffect(() => {
    Promise.all([
      api.get('/vehicles'),
      api.get('/employee-assignments'),
      api.get('/pool-assignments'),
      api.get('/readings'),
      api.get('/expenses'),
      api.get('/employees'),
      api.get('/vendors')
    ]).then(([veh, ea, pa, odo, exp, emp, ven]) => {
      const totalKm = odo.data.reduce((sum: number, r: any) => sum + (r.km || 0), 0);
      const totalExpense = exp.data.reduce((sum: number, e: any) => sum + (e.amount || 0), 0);
      setStats({
        vehicles: veh.data.length,
        assignments: ea.data.length + pa.data.length,
        odometers: odo.data.length,
        expenses: exp.data.length,
        totalKm,
        totalExpense,
      });
      setEmployeeCount(emp.data.length);
      setVendorCount(ven.data.length);
      setLastExpenses([...exp.data].sort((a, b) => b.id - a.id).slice(0, 5));
      setLastOdometers([...odo.data].sort((a, b) => b.id - a.id).slice(0, 5));
      setLastAssignments([...ea.data, ...pa.data].sort((a, b) => b.id - a.id).slice(0, 5));
      // Expense type stats
      console.log([...ea.data, ...pa.data])
      const typeStats: Record<string, number> = {};
      exp.data.forEach((e: any) => {
        if (!typeStats[e.type]) typeStats[e.type] = 0;
        typeStats[e.type] += e.amount || 0;
      });
      setExpenseTypeStats(typeStats);
      // Monthly expense
      const months: Record<string, number> = {};
      exp.data.forEach((e: any) => {
        if (!e.date) return;
        const m = e.date.slice(0, 7);
        if (!months[m]) months[m] = 0;
        months[m] += e.amount || 0;
      });
      setMonthlyExpense(Object.entries(months).sort(([a], [b]) => a.localeCompare(b)).map(([month, total]) => ({ month, total })));
    }).finally(() => setLoading(false));
  }, []);

  if (loading) return <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh"><CircularProgress /></Box>;

  return (
    <ProtectedRoute>
      <Box p={{ xs: 1, md: 4 }}>
        <Typography variant="h4" mb={4} fontWeight={700} color="#e3e3e3">Filo Genel Durum</Typography>
        {/* 1. Satır: Stat kartları */}
        <Grid container spacing={3} mb={2}>
          {[
            {
              label: 'Toplam Araç',
              value: stats.vehicles,
              color: '#1976d2',
              bgcolor: '#e3f2fd',
              icon: <DirectionsCar sx={{ fontSize: 32, color: '#fff' }} />
            },
            {
              label: 'Toplam Atama',
              value: stats.assignments,
              color: '#7b1fa2',
              bgcolor: '#f3e5f5',
              icon: <Assignment sx={{ fontSize: 32, color: '#fff' }} />
            },
            {
              label: 'Toplam KM',
              value: stats.totalKm.toLocaleString('tr-TR'),
              color: '#388e3c',
              bgcolor: '#e8f5e9',
              icon: <Speed sx={{ fontSize: 32, color: '#fff' }} />
            },
            {
              label: 'Toplam Harcama',
              value: stats.totalExpense.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' }),
              color: '#ff9800',
              bgcolor: '#fff3e0',
              icon: <MonetizationOn sx={{ fontSize: 32, color: '#fff' }} />
            },
            {
              label: 'Çalışan',
              value: employeeCount,
              color: '#00838f',
              bgcolor: '#e0f7fa',
              icon: <People sx={{ fontSize: 32, color: '#fff' }} />
            },
            {
              label: 'Tedarikçi',
              value: vendorCount,
              color: '#d84315',
              bgcolor: '#fbe9e7',
              icon: <ReceiptLong sx={{ fontSize: 32, color: '#fff' }} />
            }
          ].map((item, idx, arr) => (
            <Grid item xs={12/arr.length} key={item.label} sx={{ flexGrow: 1, flexBasis: 0, maxWidth: `${100/arr.length}%` }}>
              <StatCard sx={{ bgcolor: item.bgcolor, width: '100%' }}>
                <StatIconBox color={item.color}>{item.icon}</StatIconBox>
                <Typography variant="subtitle2" color={item.color}>{item.label}</Typography>
                <Typography variant="h5" color={item.color} fontWeight={700}>{item.value}</Typography>
              </StatCard>
            </Grid>
          ))}
        </Grid>
        {/* 2. Satır: Son 5 listeleri */}
        <Grid container spacing={3} mt={4} mb={4}>
          {[
            {
              title: 'Son 5 Harcama',
              data: lastExpenses,
              render: (e: any) => <>
                <span style={{ color: '#1976d2', fontWeight: 500 }}>{e.date}</span>
                <span style={{ color: '#fff' }}>{e.description}</span>
                <span style={{ color: '#ff9800', fontWeight: 600 }}>{e.amount.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' })}</span>
              </>,
              empty: 'Kayıt yok'
            },
            {
              title: 'Son 5 Kilometre',
              data: lastOdometers,
              render: (o: any) => <>
                <span style={{ color: '#388e3c', fontWeight: 500 }}>{o.date}</span>
                <span style={{ color: '#1976d2', fontWeight: 600 }}>{o.km} km</span>
              </>,
              empty: 'Kayıt yok'
            },
            {
              title: 'Son 5 Atama',
              data: lastAssignments,
              render: (a: any) => <>
                <span style={{ color: '#7b1fa2', fontWeight: 500 }}>{a.startDate}</span>
                <span style={{ color: '#ce93d8' }}>{a.employee ? 'Çalışan' : 'Havuz'}</span>
                <span style={{ color: '#1976d2' }}>{a.vehicle.plateNumber}</span>
              </>,
              empty: 'Kayıt yok'
            }
          ].map((item, idx, arr) => (
            <Grid item xs={12/arr.length} key={item.title} sx={{ flexGrow: 1, flexBasis: 0, maxWidth: `${100/arr.length}%` }}>
              <ListPaper >
                <Typography variant="subtitle1" mb={1} fontWeight={600}>{item.title}</Typography>
                {item.data.length === 0 ? <Typography color="text.secondary">{item.empty}</Typography> : (
                  <ListUl>
                    {item.data.map((d: any) => (
                      <ListLi key={d.id}>{item.render(d)}</ListLi>
                    ))}
                  </ListUl>
                )}
              </ListPaper>
            </Grid>
          ))}
        </Grid>
        {/* 3. Satır: Grafikler */}
        <Grid container spacing={3} >
          {[
            {
              title: 'Aylık Harcama (Son 12 Ay)',
              content: <Bar
                data={{
                  labels: monthlyExpense.map(m => m.month),
                  datasets: [
                    {
                      label: 'Harcama',
                      data: monthlyExpense.map(m => m.total),
                      backgroundColor: '#1976d2',
                      borderRadius: 4,
                      barPercentage: 0.6,
                    }
                  ]
                }}
                options={{
                  plugins: {
                    legend: { display: false },
                    tooltip: { callbacks: { label: (ctx) => ctx.parsed.y.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' }) } }
                  },
                  scales: {
                    x: { ticks: { color: '#fff' }, grid: { color: '#52595d' } },
                    y: { ticks: { color: '#fff' }, grid: { color: '#52595d' } }
                  }
                }}
              />
            },
            {
              title: 'Harcama Tiplerine Göre Dağılım',
              content: <Bar
                data={{
                  labels: Object.keys(expenseTypeStats),
                  datasets: [
                    {
                      label: 'Toplam',
                      data: Object.values(expenseTypeStats),
                      backgroundColor: ['#1976d2', '#d32f2f', '#388e3c', '#fbc02d', '#7b1fa2', '#ff9800'],
                      borderRadius: 4,
                      barPercentage: 0.6,
                    }
                  ]
                }}
                options={{
                  plugins: {
                    legend: { display: false },
                    tooltip: { callbacks: { label: (ctx) => ctx.parsed.y.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' }) } }
                  },
                  scales: {
                    x: { ticks: { color: '#fff' }, grid: { color: '#52595d' } },
                    y: { ticks: { color: '#fff' }, grid: { color: '#52595d' } }
                  }
                }}
              />
            }
          ].map((item, idx, arr) => (
            <Grid item xs={12/arr.length} key={item.title} sx={{ flexGrow: 1, flexBasis: 0, maxWidth: `${100/arr.length}%` }}>
              <Paper sx={{ p: 2, borderRadius: 4, boxShadow: '0 2px 12px 0 rgba(0,0,0,0.04)', width: '100%' }}>
                <Typography variant="subtitle1" mb={1} fontWeight={600}>{item.title}</Typography>
                {item.content}
              </Paper>
            </Grid>
          ))}
        </Grid>
      </Box>
    </ProtectedRoute>
  );
}