# 🏨**HOTEL MANAGEMENT SYSTEM**

Aplikasi perhotelan ini adalah sistem manajemen hotel yang dirancang untuk memudahkan pengelolaan reservasi kamar hotel. 
Aplikasi ini memunginkan pengguna untuk melakukan hal-hal berikut:

1. Pengguna: Tamu dapat mendaftar, masuk, dan melakukan reservasi kamar.

2. Admin: Admin dapat mengelola data pengguna, kamar, dan reservasi, serta memantau status kamar yang masih tersedia.

3. Reservasi: Pengguna dapat melihat kamar yang tersedia, melakukan reservasi, danmenghitung total biaya berdasarkan jumlah hari yang diinginkan.

## **CARA MENJALANKAN APLIKASI**

- Pastikan perangkat telah terpasang Java Development Kit(JDK) 8 atau lebih baru dan JavaFX SDK.

- Unduh atau clone proyek ini ke komputer atau laptop.

- Buka proyek dengan IDE

- Pastikan konfigurasi proyek sudah mengacu ke JDK dan JavaFX SDK yang terpasang.

- Jalankan kelas utama 'App.java' sebagai aplikasi Java.

- Aplikasi akan menampilkan halaman login

- Setelah login, kita dapat mengakses dasboard sesuai peran masing-masing.


## **STRUKTUR FOLDER**
```
FINALAB_TEST/
├── .gradle/
├── .vscode/
├── app/
├── build/
├── data/
│   ├── kamar.json
│   ├── reservasi.json
│   └── users.json
├── resources/
│   └── image/
│       ├── kamardeluxe.jpg
│       └── kamarstandard.jpg
└── src/
    └── main/
        └── java/
            └── projek/
                ├── data/
                │   └── DataStore.java
                ├── model/
                │   ├── Admin.java
                │   ├── Guest.java
                │   ├── Kamar.java
                │   ├── KamarDeluxe.java
                │   ├── KamarStandard.java
                │   ├── Reservasi.java
                │   └── User.java
                └── scenes/
                    ├── AdminSceneBuilder.java
                    ├── GuestSceneBuilder.java
                    ├── LoginSceneBuilder.java
                    └── App.java
```

projek/data/
Berisi 'DataStore.java' yang mengellola penyimpanan dan pemuatan data (pengguna, kamar, reservasi) dalam file JSON.

projek/model/

Berisi kelas model data abstrack dan konkrit:

- 'User.java' (abstract) dengan subclass 'Admin.java' dan 'Guest.java'

- 'Kamar.java' (abstract) dengan subclass 'KamarStandard.java' dan KamarDeluxe.java'

- 'Reservasi.java' untuk data reservasi hotel

projek/scenes/

- 'LoginSceneBuilder.java' untuk halaman login dan pendaftaran

- 'GuestScenesBuilder.java' untuk dashboard pelanggan

- 'AdminScenesBuilder.java' untuk dashboard admin

projek/App.java
Gerbang masuk utama aplikasi mengatur pergantian scene dan memulai aplikasi.

## **PENERAPAN PILAR OOP**

- Encapsulation:

  Data dan fungsional dikemas dalam kelas dengan variabel private atau protected, diakses lewat getter dan setter untuk menjaga keamanan dan integritas data.

- Inheritance:

  Kelas dasar 'User.java' dan 'Kamar.java' menjadi superclass untuk kelas-kelas spesifik seperti 'Admin.java', 'Guset.java', 'KamarStandard.java', dan 'KamarDeluxe.java', Hal ini memudahkan pewarisan.

- Polymorphism:

  Metode seperti 'getRole()' atau 'getTipe()' diimplementasikan secara berbeda di subclass, sehingga  objek dapat berperilaku sesuai tipe spesifiknya meskipun diakses lewat referensi kelas dasar.

- Abstraction:

  Kelas abstack mendefinisikan kerangka kerja dengan metode abstrack tanpa implementasi. ini membuat kode lebih modular dan mudah di kembangkan.


