'use client';
import { useEffect, useState } from 'react';
import { Box, Typography, Paper, Divider, CircularProgress, Grid, Chip, TableContainer, Table, TableHead, TableRow, TableCell, TableBody } from '@mui/material';
import api from '../../../../services/api';
import { useParams } from 'next/navigation';
import ProtectedRoute from '../../../../components/ProtectedRoute';

interface Vehicle {
  id: number;
  plateNumber: string;
  brand: string;
  model: string;
  ownership: string;
  leaseStartDate: string;
  leaseEndDate: string;
  assignments?: Assignment[];
  readings?: OdometerReading[];
  expenses?: Expense[];
  assignmentIds?: number[];
  readingIds?: number[];
  expenseIds?: number[];
}

interface Assignment {
  id: number;
  type?: string;
  employeeName?: string;
  startDate: string;
  endDate: string;
}

interface OdometerReading {
  id: number;
  date: string;
  km: number;
}

interface Expense {
  id: number;
  date: string;
  amount: number;
  description: string;
}

export default function VehicleDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [vehicle, setVehicle] = useState<Vehicle | null>(null);
  const [assignments, setAssignments] = useState<Assignment[]>([]);
  const [readings, setReadings] = useState<OdometerReading[]>([]);
  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    api.get(`/vehicles/${id}`)
      .then((vehRes) => {
        const v = vehRes.data;
        setVehicle(v);
        setAssignments(v.assignments || []);
        setReadings(v.readings || []);
        setExpenses(v.expenses || []);
      })
      .catch(() => setError('Araç veya ilişkili veriler bulunamadı'))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh"><CircularProgress /></Box>;
  if (error) return <Typography color="error">{error}</Typography>;
  if (!vehicle) return null;

  return (
    <ProtectedRoute>
      <Box sx={{ width: '100%', minHeight: '80vh', py: { xs: 1, md: 3 }, px: { xs: 0, md: 2 } }}>
        {/* Araç Bilgisi Kartı */}
        <Paper elevation={3} sx={{ width: '100%', mx: 'auto', mb: 4, borderRadius: 3, p: { xs: 2, md: 4 }, boxShadow: '0 4px 24px 0 rgba(60,72,88,0.10)' }}>
          <Grid container spacing={2} alignItems="center" justifyContent='space-between'>
            {/* Sol: Plaka */}
            <Grid item xs={12} md={4}>
              <Box sx={{ width: '100%', height: '100%', borderRadius: 2, color: '#1976d2', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 32, fontWeight: 700, p:3, mx: 'auto', bgcolor: '#52595d', border: '2px solidrgb(18, 85, 152)' }}>
                {vehicle.plateNumber}
              </Box>
            </Grid>
            {/* Orta: Marka ve Model */}
            <Grid item xs={12} md={4}>
              <Box display="flex" flexDirection="column" alignItems="center">
                <Typography variant="h4" fontWeight={700} color="text.primary" align="center">{vehicle.brand} {vehicle.model}</Typography>
                <Typography color="text.secondary" fontSize={18} align="center" mt={1}>{vehicle.model}</Typography>
              </Box>
            </Grid>
            {/* Sağ: Sahiplik ve Kiralama Bilgisi */}
            <Grid item xs={12} md={4}>
              <Box display="flex" flexDirection="column" alignItems="center">
                <Typography fontWeight={600} color="text.primary" fontSize={18} align="center">{vehicle.ownership}</Typography>
                {vehicle.ownership === 'LEASED' && (
                  <Typography fontSize={15} color="text.secondary" align="center" mt={1}>
                    {vehicle.leaseStartDate} - {vehicle.leaseEndDate}
                  </Typography>
                )}
              </Box>
            </Grid>
          </Grid>
        </Paper>
        <Divider sx={{ my: 3, width: '100%', mx: 'auto' }} />
        {/* Bilgi Kartları */}
        <Grid container spacing={2} sx={{ width: '100%', margin: 0, maxWidth: '100%' }}>
          {[{
            title: 'Atamalar',
            icon: '📝',
            count: assignments.length,
            content: assignments.length === 0 ? (
              <Typography color="text.secondary" align="center">Atama kaydı yok.</Typography>
            ) : (
              <TableContainer>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>Tip</TableCell>
                      <TableCell>Çalışan</TableCell>
                      <TableCell>Başlangıç</TableCell>
                      <TableCell>Bitiş</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {assignments.map(a => (
                      <TableRow key={a.id}>
                        <TableCell>{a.type === 'employee' ? 'Çalışan' : 'Havuz'}</TableCell>
                        <TableCell>{a.employeeName || '-'}</TableCell>
                        <TableCell>{a.startDate}</TableCell>
                        <TableCell>{a.endDate}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            )
          }, {
            title: 'Kilometreler',
            icon: '📈',
            count: readings.length,
            content: readings.length === 0 ? (
              <Typography color="text.secondary" align="center">Kayıt yok.</Typography>
            ) : (
              <TableContainer>
                <Table >
                  <TableHead>
                    <TableRow>
                      <TableCell>Tarih</TableCell>
                      <TableCell>Kilometre</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {readings.map(r => (
                      <TableRow key={r.id}>
                        <TableCell>{r.date}</TableCell>
                        <TableCell>{r.km}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            )
          }, {
            title: 'Harcamalar',
            icon: '💸',
            count: expenses.length,
            content: expenses.length === 0 ? (
              <Typography color="text.secondary" align="center">Harcama kaydı yok.</Typography>
            ) : (
              <TableContainer>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>Tarih</TableCell>
                      <TableCell>Tutar</TableCell>
                      <TableCell>Açıklama</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {expenses.map(e => (
                      <TableRow key={e.id}>
                        <TableCell>{e.date}</TableCell>
                        <TableCell>{e.amount}₺</TableCell>
                        <TableCell>{e.description}</TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            )
          }].map((section, idx, arr) => (
            <Grid item xs={12} md={4} key={section.title} sx={{ flexGrow: 1, flexBasis: 0, maxWidth: `${100/arr.length}%` }}>
              <Paper sx={{ p: 2, borderRadius: 2, width: '100%', minHeight: 370, display: 'flex', flexDirection: 'column', justifyContent: 'flex-start', boxShadow: '0 2px 12px 0 rgba(60,72,88,0.08)', flex: 1 }}>
                <Box width='100%' display="flex" alignItems="center" gap={1} mb={2}>
                  <span style={{ fontSize: 24 }}>{section.icon}</span>
                  <Typography width='100%' display="flex" alignItems="center" justifyContent='space-between' variant="h6" fontWeight={600}>{section.title} <Chip  label={section.count} color={section.count ? 'primary' : 'default'} /></Typography>
                </Box>
                <Box flex={1} display="flex" flexDirection="column" justifyContent="center">
                  {section.content}
                </Box>
              </Paper>
            </Grid>
          ))}
        </Grid>
      </Box>
    </ProtectedRoute>
  );
}