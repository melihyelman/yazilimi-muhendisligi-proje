"use client"
import { useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import api from '@/services/api';

export default function NewTripLogPage() {
  const router = useRouter();
  const params = useSearchParams();
  const assignmentId = params.get('id');
  const [form, setForm] = useState({
    date: '',
    startLocation: '',
    endLocation: '',
    distance: '',
    description: '',
  });
  const [error, setError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    try {
      await api.post(`/trip-logs?assignmentId=${assignmentId}`, {
        ...form,
        distance: Number(form.distance),
      });
      router.push(`/dashboard/assignments/${assignmentId}`);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Bir hata oluştu');
    }
  };

  return (
    <div>
      <h1>Yeni Trip Log Ekle</h1>
      <form onSubmit={handleSubmit}>
        <input name="date" type="date" value={form.date} onChange={handleChange} required />
        <input name="startLocation" placeholder="Başlangıç Noktası" value={form.startLocation} onChange={handleChange} required />
        <input name="endLocation" placeholder="Bitiş Noktası" value={form.endLocation} onChange={handleChange} required />
        <input name="distance" type="number" placeholder="Mesafe (km)" value={form.distance} onChange={handleChange} required />
        <textarea name="description" placeholder="Açıklama" value={form.description} onChange={handleChange} />
        <button type="submit">Kaydet</button>
        {error && <div style={{color:'red'}}>{error}</div>}
      </form>
    </div>
  );
}
