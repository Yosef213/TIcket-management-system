package com.mycompany.project.classes;

public class User {
    private String username;
    private String password;
    private String phone; 
    private String email; 

    private Role role; 
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    public User(String username,String password, String phone,String email){
        this.username = username;
        this.password = password;
        this.phone = phone; 
        this.email = email;
        this.role = role.Attendee;
    }
    public User(String username, String email,String phone_number){
        this.username = username;
        this.email = email;
        this.phone = phone_number;
                this.role = role.Attendee;

    }
        public User(String username, String email,String phone_number,Role role){
        this.username = username;
        this.email = email;
        this.phone = phone_number;
                this.role = role;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
    public String getRoleString(){
        return role.toString();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}