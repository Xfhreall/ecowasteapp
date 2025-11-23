# EcoWaste App

EcoWaste adalah aplikasi Android yang membantu pengguna dalam mengelola dan memilah sampah secara lebih efisien.
Repository ini berisi source code aplikasi yang dibangun menggunakan **Android Studio + Kotlin**.

---

## Cara Clone & Menjalankan Project

Ikuti langkah berikut untuk menjalankan project di perangkat lokal:

### 1. Clone Repository

Pastikan Git sudah terinstall.

```bash
git clone https://github.com/Xfhreall/ecowasteapp.git
cd ecowasteapp
```

### 2. Buka di Android Studio

1. Buka **Android Studio**
2. Pilih **File > Open**
3. Arahkan ke folder project hasil clone
4. Tunggu Android Studio melakukan **Gradle Sync** secara otomatis

Jika Gradle tidak sync otomatis, jalankan:

```
File > Sync Project with Gradle Files
```

---

## Persiapan Sebelum Menjalankan

Pastikan:

* Menggunakan **Android Studio versi terbaru**
* Android SDK minimal sesuai konfigurasi `compileSdk` pada project
* Emulator atau perangkat fisik sudah disiapkan

Tidak perlu mengubah `local.properties` karena file tersebut tidak termasuk dalam repo dan akan dibuat otomatis oleh Android Studio berdasarkan lokasi SDK kamu.

---

## ▶Menjalankan Aplikasi

1. Pilih **device/emulator** di bagian atas Android Studio
2. Klik tombol **Run (▶)**
3. Aplikasi akan otomatis build dan berjalan di perangkat yang dipilih

---

## Struktur Project (Ringkas)

* `app/` — Source code utama (Kotlin, Layout XML, Manifest)
* `gradle/` — File konfigurasi Gradle
* `build.gradle` — Pengaturan build level project dan module
* `assets/`, `res/` — Resource aplikasi

---

## Kontribusi

Pull Request terbuka untuk perbaikan bug, penambahan fitur, atau peningkatan kualitas kode.

---