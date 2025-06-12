package projek.model;

public class KamarStandard extends Kamar {
    public KamarStandard(String nomor) {
        super(nomor, 500000);
    }

    @Override
    public String getTipe() {
        return "Standard";
    }
}
