'use client';
import { Geist, Geist_Mono } from "next/font/google";
import { ThemeProvider, createTheme, CssBaseline } from '@mui/material';
import "./globals.css";
import AppLayout from '../components/AppLayout';

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    background: {
      default: '#1e1e1e',
      paper: '#23272e',
    },
    primary: {
      main: '#388bfd',
    },
    secondary: {
      main: '#7c3aed',
    },
    text: {
      primary: '#e6e6e6',
      secondary: '#b3b3b3',
    },
  },
  shape: { borderRadius: 8 },
  typography: {
    fontFamily: 'Inter, Roboto, Arial, sans-serif',
  },
});

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="tr" style={{backgroundColor: '#23272e', color: '#e6e6e6', fontFamily: `${geistSans.variable}, ${geistMono.variable}, sans-serif`}}>
      <body style={{backgroundColor: '#23272e', color: '#e6e6e6', fontFamily: `${geistSans.variable}, ${geistMono.variable}, sans-serif`}}>
        <ThemeProvider theme={darkTheme}>
          <CssBaseline />
          <AppLayout>{children}</AppLayout>
        </ThemeProvider>
      </body>
    </html>
  );
}
