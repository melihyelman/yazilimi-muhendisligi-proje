## Genel Teknoloji Yapısı

* **Framework**: Next.js 14 (App Router)
* **Durum Yönetimi**: Zustand
* **HTTP İstekleri**: Axios
* **UI Bileşenleri**: Material UI
* **Form Yönetimi**: React Hook Form
* **Yetkilendirme**: JWT (localStorage tabanlı saklama)

---

## Sayfa ve Dosya Yapısı

```
/app
 ├─ layout.tsx               → Sidebar + Header
 ├─ page.tsx                 → Root: login kontrolü ve yönlendirme
 ├─ login/page.tsx           → Giriş sayfası
 └─ dashboard/
     ├─ layout.tsx          → Sidebar menüsü
     ├─ page.tsx            → Ana dashboard ekranı
     ├─ vehicles/           → Araç CRUD
     ├─ assignments/        → Atamalar (pool/employee)
     ├─ expenses/           → Harcamalar
     ├─ odometers/          → Kilometre verileri
     ├─ trip-logs/          → Kullanıcı mesafeleri
     └─ reports/            → Raporlar
/components
 /Sidebar.tsx
 /ProtectedRoute.tsx
 /forms/
  AraçFormu.tsx, HarcamaFormu.tsx, vs...
/lib
 /store.ts
 /api.ts
 /auth.ts
```
