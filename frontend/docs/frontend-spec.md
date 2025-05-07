# Motorlu Araç Filo Yönetimi - Frontend Talimatnamesi (Next.js)

Bu doküman, proje frontend geliştiricisinin (veya yapay zekanın) uygulamayı doğru mimari ile inşa etmesi için oluşturulmuştur. Proje Next.js 14 App Router ile geliştirilecek olup, Material UI tasarım kütüphanesi kullanılacak, durum yönetimi için Zustand tercih edilecektir.

---

## Genel Teknoloji Yapısı

* **Framework**: Next.js 14 (App Router)
* **Durum Yönetimi**: Zustand
* **HTTP İstekleri**: Axios
* **UI Bileşenleri**: Material UI
* **Form Yönetimi**: React Hook Form
* **Yetkilendirme**: JWT (localStorage tabanlı saklama)

---

## Sayfa ve Dosya Yapısı

```bash
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
     ├─ forecast/           → Hareketli Ortalama tahmin ekranı
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

---

## Auth Akışı

* Kullanıcı `/login` ekranından giriş yapar.
* JWT token localStorage'a yazılır.
* `/dashboard/*` dizinindeki tüm sayfalar token kontrolü ile korunur.
* Kullanıcı rolüne göre menü öğeleri dinamik görüntülenir.

AUTH hariç diğer bütün endpointler bearer token ister.