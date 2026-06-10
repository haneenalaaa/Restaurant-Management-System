/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package advancedproj;

/**
 *
 * @author Mega
 */
public class Admin extends User {
    
   

    public Admin(String username, String password) {
        super(username, password);
    }

    

    
    public void display() {
        System.out.println("----- Admin Info -----");
        System.out.println("Username: " + username);
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}
    
    
    
    
    
    
    
    
    

