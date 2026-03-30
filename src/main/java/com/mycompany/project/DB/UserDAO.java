
package com.mycompany.project.DB;
import com.mycompany.project.classes.User;
import com.mycompany.project.classes.Role;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import com.mycompany.project.classes.Events;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends User {
    private static final int WORK_FACTOR = 12;
    public UserDAO(String username, String password) {
        super(username, password);
    }
    public UserDAO(String username, String password, String phone, String email) {
        super(username, password, phone, email);
    }
    public UserDAO(User user){
        super(user.getUsername(),user.getPassword());
    }
    private String generatePasswordHash(String plaintextPassword) {
        String salt = BCrypt.gensalt(WORK_FACTOR); 
        String hashedPassword = BCrypt.hashpw(plaintextPassword, salt);
        return hashedPassword;
    }
   private String retrieveStoredHashFromDB() {
    String sql = "SELECT password FROM User WHERE username = ?";
    try {
        DBHandler db = new DBHandler();
        ResultSet rs = db.executeQuery(sql, getUsername());
        
        if (rs.next()) {
            String hash = rs.getString("password");
            rs.close();
            db.closeConnection();
            return hash;
        }
        rs.close();
        db.closeConnection();
        return null;

    } catch (Exception e) {
        System.out.println("DB Retrieve Password Hash Error: " + e.getMessage());
        return null;
    }}
  public void insertUser() throws Exception {
    String hashedPassword = generatePasswordHash(this.getPassword());
    String sql = "INSERT INTO User (username, password, phone_number, email, role) VALUES (?, ?, ?, ?, ?)";

    try {
        DBHandler db = new DBHandler();
        db.executeUpdate(sql, this.getUsername(), hashedPassword, this.getPhone(), this.getEmail(), this.getRole().name());
        db.closeConnection();
    } catch (Exception e) {
        System.err.println("Registration failed: " + e.getMessage());
        throw new RuntimeException("Registration failed due to database error.", e);
    }
}
    
    public boolean Login(){
        String enteredPassword = this.getPassword();
        String storedHash = retrieveStoredHashFromDB();
        if (storedHash == null ){
        return false;
    }
        return BCrypt.checkpw(enteredPassword, storedHash);
    }
    public void setUserInformation() {
        String sql = "SELECT username, email, phone_number, role FROM User WHERE username = ?";

        try {
            DBHandler db = new DBHandler();
            ResultSet rs = db.executeQuery(sql, this.getUsername());

            if (rs.next()) {
                this.setEmail(rs.getString("email"));
                this.setPhone(rs.getString("phone_number"));
                String roleString = rs.getString("role");
                Role userRole = null;
                if (roleString != null) {
                    userRole = Role.valueOf(roleString.replace(" ", "_"));
                }
                this.setRole(userRole);

                rs.close();
                db.closeConnection();

            }

            rs.close();
            db.closeConnection();

        } catch (Exception e) {
            System.err.println("Error in getUserInformation(): " + e.getMessage());
        }
    }
}

