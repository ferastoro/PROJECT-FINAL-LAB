# 🏨**HOTEL MANAGEMENT SYSTEM**

Aplikasi perhotelan ini adalah sistem manajemen hotel yang dirancang untuk memudahkan pengelolaan reservasi kamar hotel. 
Aplikasi ini memunginkan pengguna untuk melakukan hal-hal berikut:

1. Pengguna: Tamu dapat mendaftar, masuk, dan melakukan reservasi kamar.
2. Admin: Admin dapat mengelola data pengguna, kamar, dan reservasi, serta memantau status kamar yang masih tersedia.
3. Reservasi: Pengguna dapat melihat kamar yang tersedia, melakukan reservasi, danmenghitung total biaya berdasarkan jumlah hari yang diinginkan.

**CARA KERJA**
Aplikasi ini dibangun menggunakan Java dan JavaFX untuk antarmuka pengguna. Berikut cara kerja aplikasi ini:
1. Login: Pengguna dapat masuk ke dalam aplikasi menggunakan username dan password. Terdapat dua jenis pengguna, yaitu Admin dan Tamu.
2. Manajemen Kamar: Admin dapat menambah, mengedit, dan menghapus data kamar. Kamar dibedakan menjadi dua tipe, yaitu kamar Standard dan kamar Deluxe, dengan harga dan fasilitas yang berbeda.
3. Reservasi: Tamu dapat memilih kamar yang tersedia, menentukan tanggal check-in dan check-out, serta menghitung total biaya berdasarkan harga perhari.
4. Penyimpana Data: Data pengguna, kamar, dan reservasi disimpan dalam formatJSON di dalam folder 'Data'. Aplikasi akan memuat data ini saat dijalankan dan menyimpannya kembali saat aplikasi ditutup.

**CARA MENJALANKAN APLIKASI**
1. Pastikan perangkat telah terpasang Java Development Kit(JDK) 8 atau lebih baru dan JavaFX SDK.
2. Unduh atau clone proyek ini ke komputer atau laptop.
3. Buka proyek dengan IDE
4. Pastikan konfigurasi proyek sudah mengacu ke JDK dan JavaFX SDK yang terpasang.
5. Jalankan kelas utama 'App.java' sebagai aplikasi Java.
6. Aplikasi akan menampilkan halaman login
7. Setelah login, kita dapat mengakses dasboard sesuai peran masing-masing.


**STRUKTUR FOLDER**
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
1. 'User.java' (abstract) dengan subclass 'Admin.java' dan 'Guest.java'
2. 'Kamar.java' (abstract) dengan subclass 'KamarStandard.java' dan KamarDeluxe.java'
3. 'Reservasi.java' untuk data reservasi hotel

projek/scenes/
1. 'LoginSceneBuilder.java' untuk halaman login dan pendaftaran
2. 'GuestScenesBuilder.java' untuk dashboard pelanggan
3. 'AdminScenesBuilder.java' untuk dashboard admin

projek/App.java
Gerbang masuk utama aplikasi mengatur pergantian scene dan memulai aplikasi.

**PENERAPAN PILAR OOP**
1. Encapsulation:
   Data dan fungsional dikemas dalam kelas dengan variabel private atau protected, diakses lewat getter dan setter untuk menjaga keamanan dan integritas data.
2. Inheritance:
   Kelas dasar 'User.java' dan 'Kamar.java' menjadi superclass untuk kelas-kelas spesifik seperti 'Admin.java', 'Guset.java', 'KamarStandard.java', dan 'KamarDeluxe.java', Hal ini memudahkan pewarisan.
3. Polymorphism:
   Metode seperti 'getRole()' atau 'getTipe()' diimplementasikan secara berbeda di subclass, sehingga  objek dapat berperilaku sesuai tipe spesifiknya meskipun diakses lewat referensi kelas dasar.
4. Abstraction:
   Kelas abstack mendefinisikan keranngka kerja dengan metode abstrack tanpa implementasi. ini membuat kode lebih modular dan mudah di kembangkan.


