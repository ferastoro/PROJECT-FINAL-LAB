package projek.model;

public class Guest extends User {
    public Guest(String username, String password) {
        super(username, password);
        this.fullName = username;
    }
    
    public Guest(String username, String password, String fullName) {
        super(username, password, fullName); 
        if (this.fullName == null || this.fullName.trim().isEmpty()) {
            this.fullName = username;
        }
    }
    
    @Override
    public String getRole() {
        return "guest";
    }
}