package projek.model;

public abstract class User {
    protected String username;
    protected String password;
    protected String fullName;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

     public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public abstract String getRole();

    public String getUsername() { 
        return username;
    }
    public String getPassword() { 
        return password; 
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}