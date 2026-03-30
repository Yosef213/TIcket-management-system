/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project.DB;

import com.mycompany.project.classes.User;
import com.mycompany.project.classes.Role;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
        public static List<User> getAllUsers(){
        String sql = "SELECT username, phone_number, email, role FROM User";
        List<User> users = new ArrayList<>();
        try{
            DBHandler db = new DBHandler();
            ResultSet rs = db.executeQuery(sql);
              while (rs.next()) {
                String username = rs.getString("username");
                String phone_number = rs.getString("phone_number");
                String email = rs.getString("email");
                String roleString = rs.getString("role");
                User user = new User(username, email, phone_number);
                Role userRole = Role.valueOf(roleString.replace(" ", "_"));
                user.setRole(userRole);
                users.add(user);
            }
             rs.close();
            db.closeConnection();
        }
        catch(Exception e){
            System.err.println("Error in getAllUsers(): " + e.getMessage());
        }
        return users;
    }
        public static boolean updateUser(String username, String phone_number, String email, String role){
        String sql = "UPDATE User SET phone_number = ?, email = ?, role = ? WHERE username = ?";
        boolean isUpdated = false;
        try{
            DBHandler db = new DBHandler();
            int rows = db.executeUpdate(sql,phone_number,email,role,username);
            isUpdated = rows > 0;
        }
        catch(Exception e){
                        System.err.println("Error in updateUser(): " + e.getMessage());

        }
        return isUpdated;
        }
        
}
