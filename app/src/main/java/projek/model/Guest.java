package projek.model;

public class Guest extends User {
    public Guest(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "Guest";
    }
}

