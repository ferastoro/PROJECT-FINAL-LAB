package projek.model;

import java.time.LocalDate;

public class Reservasi {
    private String idReservasi; // TAMBAHKAN: ID untuk reservasi
    private Guest tamu;
    private Kamar kamar;
    private LocalDate checkIn;
    private LocalDate checkOut;

    // Constructor yang ada bisa tetap digunakan saat membuat reservasi baru dari aplikasi
    public Reservasi(Guest tamu, Kamar kamar, LocalDate checkIn, LocalDate checkOut) {
        this.tamu = tamu;
        this.kamar = kamar;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        // ID Reservasi bisa di-generate di sini atau di DataStore saat menambah
        // Contoh sederhana: this.idReservasi = "RES" + System.currentTimeMillis();
    }

    // Constructor alternatif bisa berguna untuk DataStore saat memuat dari file
    // Namun, lebih baik DataStore yang melakukan resolve objek Guest dan Kamar
    // Jadi, kita akan tambahkan setter dan getter untuk ID, dan DataStore akan mengelola sisanya.

    // TAMBAHKAN getter dan setter untuk idReservasi
    public String getIdReservasi() {
        return idReservasi;
    }

    public void setIdReservasi(String idReservasi) {
        this.idReservasi = idReservasi;
    }
    // AKHIR TAMBAHAN

    public long getJumlahMalam() {
        if (checkIn == null || checkOut == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    public double getTotalHarga() {
        if (kamar == null) {
            return 0;
        }
        return kamar.hitungHarga((int) getJumlahMalam());
    }

    public Guest getTamu() {
        return tamu;
    }

    public void setTamu(Guest tamu) {
        this.tamu = tamu;
    }

    public Kamar getKamar() {
        return kamar;
    }

    public void setKamar(Kamar kamar) {
        this.kamar = kamar;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    // Untuk kemudahan debugging atau logging
    @Override
    public String toString() {
        return "Reservasi{" +
                "idReservasi='" + idReservasi + '\'' +
                ", tamu=" + (tamu != null ? tamu.getUsername() : "null") +
                ", kamar=" + (kamar != null ? kamar.getNomor() : "null") +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                '}';
    }
}