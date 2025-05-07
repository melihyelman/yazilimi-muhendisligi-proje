'use client';

import Link from 'next/link';
import { Box, Drawer, List, ListItem, ListItemText, AppBar, Toolbar, Typography, Button, Chip } from '@mui/material';
import ListItemButton from '@mui/material/ListItemButton';
import { usePathname, useRouter } from 'next/navigation';
import { useAuthStore } from '@/stores/auth';
import DashboardIcon from '@mui/icons-material/Dashboard';
import DirectionsCarIcon from '@mui/icons-material/DirectionsCar';
import AssignmentIndIcon from '@mui/icons-material/AssignmentInd';
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong';
import SpeedIcon from '@mui/icons-material/Speed';
import TimelineIcon from '@mui/icons-material/Timeline';
import AssessmentIcon from '@mui/icons-material/Assessment';
import ExpandLess from '@mui/icons-material/ExpandLess';
import ExpandMore from '@mui/icons-material/ExpandMore';
import Collapse from '@mui/material/Collapse';
import api from '../services/api';
import { useEffect, useState } from 'react';
import LogoutIcon from '@mui/icons-material/Logout';
import Avatar from '@mui/material/Avatar';

const drawerWidth = 220;

export default function AppLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const router = useRouter();
  const user = useAuthStore.getState().user;
  const role = user?.role;
  const logout = useAuthStore((s) => s.logout);

  // Auth persist: sayfa yenilendiğinde token ve user'ı store'a yükle
  const fetchUser = useAuthStore((s) => s.fetchUser);
  useEffect(() => {
    fetchUser();
  }, [fetchUser]);

  const handleLogout = () => {
    logout();
    router.push('/login');
  };

  // Dinamik menü state'leri
  const [vehicles, setVehicles] = useState<any[]>([]);
  const [openVehicles, setOpenVehicles] = useState(false);

  useEffect(() => {
    api.get('/vehicles').then(res => setVehicles(res.data)).catch(() => setVehicles([]));
  }, []);

  if (!user) {
    return <>{children}</>;
  }

  return (
    <>
      <Box sx={{ display: 'flex' }}>
        <AppBar position="fixed" sx={{ zIndex: 1201 }}>
          <Toolbar>
            <Typography variant="h6" sx={{ flexGrow: 1 }}>Araç Sizde Yükü Bizde A.Ş. </Typography>
            {user && (
              <Box display="flex" alignItems="center" gap={2}>
                <Chip label={user.role} color={user.role === 'ADMIN' ? 'primary' : user.role === 'VENDOR' ? 'secondary' : 'default'} variant="outlined" sx={{ fontWeight: 700, letterSpacing: 1, textTransform: 'uppercase' }} />
                <Typography sx={{ fontWeight: 500 }}>{user.name || user.username}</Typography>
                <Button variant="outlined" color={user.role === 'ADMIN' ? 'secondary' : 'primary'} onClick={handleLogout} startIcon={<LogoutIcon />}>Çıkış Yap</Button>
              </Box>
            )}
          </Toolbar>
        </AppBar>
        <Drawer
          variant="permanent"
          sx={{ width: drawerWidth, flexShrink: 0, [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' } }}
        >
          <Toolbar />
          <List>
            <ListItem disablePadding>
              <ListItemButton component={Link} href="/dashboard" selected={pathname === '/dashboard'}>
                <DashboardIcon />
                <ListItemText primary="Dashboard" sx={{ ml: 2 }} />
              </ListItemButton>
            </ListItem>
            {/* Araçlar ana menü ve alt menü */}
            <ListItem disablePadding>
              <ListItemButton component={Link} href="/dashboard/vehicles" selected={pathname === '/dashboard/vehicles'}>
                <DirectionsCarIcon />
                <ListItemText primary="Araçlar" sx={{ ml: 2 }} />
              </ListItemButton>
            </ListItem>
            {/* Çalışanlar ana menü, sadece tüm çalışanlar */}
            <ListItem disablePadding>
              <ListItemButton component={Link} href="/dashboard/employees" selected={pathname === '/dashboard/employees'}>
                <AssignmentIndIcon />
                <ListItemText primary="Çalışanlar" sx={{ ml: 2 }} />
              </ListItemButton>
            </ListItem>
            {/* Tedarikçiler ana menü, sadece tüm tedarikçiler */}
            <ListItem disablePadding>
              <ListItemButton component={Link} href="/dashboard/vendors" selected={pathname === '/dashboard/vendors'}>
                <AssignmentIndIcon />
                <ListItemText primary="Tedarikçiler" sx={{ ml: 2 }} />
              </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
              <ListItemButton component={Link} href="/dashboard/assignments" selected={pathname === '/dashboard/assignments'}>
                <AssignmentIndIcon />
                <ListItemText primary="Atamalar" sx={{ ml: 2 }} />
              </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
              <ListItemButton component={Link} href="/dashboard/trip-logs" selected={pathname === '/dashboard/trip-logs'}>
                <AssessmentIcon />
                <ListItemText primary="Seyahat Kayıtları" sx={{ ml: 2 }} />
              </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
              <ListItemButton component={Link} href="/dashboard/expenses" selected={pathname === '/dashboard/expenses'}>
                <ReceiptLongIcon />
                <ListItemText primary="Harcamalar" sx={{ ml: 2 }} />
              </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
              <ListItemButton component={Link} href="/dashboard/odometers" selected={pathname === '/dashboard/odometers'}>
                <SpeedIcon />
                <ListItemText primary="Kilometre" sx={{ ml: 2 }} />
              </ListItemButton>
            </ListItem>
            {(role === 'ADMIN' || role === 'VENDOR') && <>
              <ListItem>
                <ListItemText primary="Yönetim" sx={{ ml: 2, fontWeight: 'bold' }} />
              </ListItem>
              <ListItem disablePadding>
                <ListItemButton component={Link} href="/dashboard/expenses/reports" selected={pathname === '/dashboard/expenses/reports'}>
                  <ListItemText primary="Harcama Raporları" sx={{ ml: 2 }} />
                </ListItemButton>
              </ListItem>
              <ListItem disablePadding>
                <ListItemButton component={Link} href="/dashboard/reports" selected={pathname === '/dashboard/reports'}>
                  <ListItemText primary="Filo Raporları" sx={{ ml: 2 }} />
                </ListItemButton>
              </ListItem>
            </>}
          </List>
        </Drawer>
        <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
          <Toolbar />
          {children}
        </Box>
      </Box>
    </>
  );
}