package projek.model;

public class KamarDeluxe extends Kamar {
    public KamarDeluxe(String nomor) {
        super(nomor, 750000);
    }

    @Override
    public String getTipe() {
        return "Deluxe";
    }
}
