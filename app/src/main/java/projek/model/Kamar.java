package projek.model;

public abstract class Kamar {
    protected String nomor;
    protected double harga;
    protected boolean tersedia; 

    public Kamar(String nomor, double harga) {
        this.nomor = nomor;
        this.harga = harga;
        this.tersedia = true; 
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public boolean isTersedia() {
        return tersedia;
    }

    public void setTersedia(boolean tersedia) {
        this.tersedia = tersedia;
    }

    public double hitungHarga(int jumlahMalam) {
        return harga * jumlahMalam;
    }

    public abstract String getTipe(); 
}