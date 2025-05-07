'use client';
import { useAuthStore } from '../stores/auth';
import { useRouter } from 'next/navigation';
import React, { useEffect } from 'react';

export default function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const token = useAuthStore((s) => s.token);
  const user = useAuthStore((s) => s.user);
  const router = useRouter();

  useEffect(() => {
    if (!token || !user) {
      router.replace('/login');
    }
  }, [token, user, router]);

  if (!token || !user) return null;
  return <>{children}</>;
}