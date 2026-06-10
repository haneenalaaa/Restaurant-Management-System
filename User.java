package advancedproj;

import javafx.scene.control.Alert;

public abstract class User {  
    protected String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // --- Overloading logic ---
    
    // Normal Login 
    public String getWelcomeMessage() {
        return "Welcome back, " + this.username + "!";
    }

    // Sign Up
    public String getWelcomeMessage(String specialNote) {
        return specialNote + " " + this.username + "! We're glad you joined us.";
    }

    public abstract String getRole(); 
    
    public String getUsername() { return username; }
}