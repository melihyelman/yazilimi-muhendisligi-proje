"use client"
import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import api from '@/services/api';

export default function TripLogsPage() {
  const params = useParams();
  const router = useRouter();
  const assignmentId = params?.id;
  const [tripLogs, setTripLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!assignmentId) return;
    setLoading(true);
    api.get(`/assignments/${assignmentId}/trip-logs/`)
      .then(res => {
        setTripLogs(res.data);
        setLoading(false);
      })
      .catch(() => {
        setError('Kayıtlar yüklenemedi.');
        setLoading(false);
      });
  }, [assignmentId]);

  if (loading) return <div>Yükleniyor...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      <h1>Trip Log Kayıtları</h1>
      <button onClick={() => router.push(`/dashboard/assignments/${assignmentId}/trip-logs/new/`)}>
        Yeni Trip Log Ekle
      </button>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Başlangıç KM</th>
            <th>Bitiş KM</th>
            <th>Tarih</th>
            <th>Açıklama</th>
          </tr>
        </thead>
        <tbody>
          {tripLogs.length === 0 ? (
            <tr><td colSpan={5}>Kayıt bulunamadı.</td></tr>
          ) : (
            tripLogs.map((log: any) => (
              <tr key={log.id}>
                <td>{log.id}</td>
                <td>{log.start_odometer}</td>
                <td>{log.end_odometer}</td>
                <td>{log.date}</td>
                <td>{log.description}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
