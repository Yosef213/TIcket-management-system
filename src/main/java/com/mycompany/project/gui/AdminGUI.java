/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.project.classes.User;
import com.mycompany.project.DB.AdminDAO;
import com.mycompany.project.DB.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
public class AdminGUI extends EventManagementGUI {
    public JButton users_button;
    private List<User> users;
    public AdminGUI(String title,UserDAO user) {
        super(title,user);
        users_button = new JButton("Users");
users_button.setFont(largeFont);
        toolbar.add(users_button);
            users_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAndDisplayUsers();
            }
        });
    }
    private void loadAndDisplayUsers(){
    users = AdminDAO.getAllUsers();
     JPanel managementPanel = new JPanel();
     managementPanel.setLayout(new BoxLayout(managementPanel, BoxLayout.Y_AXIS));
     for(User user : users){
          JPanel userPanel = new JPanel();
                    userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel username_label = new JLabel("Username: " + user.getUsername());
        JLabel phonenumber_Label = new JLabel("Phone Number: " + user.getPhone());
        JLabel email_label = new JLabel("Email:" + user.getEmail());
        JButton edit_button = new JButton("Edit");
        JButton booked_events = new JButton("Booked Events");
        edit_button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    UserEditDialog dialog = new UserEditDialog(AdminGUI.this, user);
                          dialog.setLocationRelativeTo(AdminGUI.this);
        dialog.setVisible(true);
                }});
        booked_events.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        BookedEventsDialog dialog = new BookedEventsDialog(AdminGUI.this, user);
        dialog.setLocationRelativeTo(AdminGUI.this);
        dialog.setVisible(true);
    }
});
        userPanel.add(username_label);
        userPanel.add(phonenumber_Label);
        userPanel.add(email_label);
        userPanel.add(edit_button);
        userPanel.add(booked_events);
        managementPanel.add(userPanel);
        
    }
         events_cards.removeAll();
    events_cards.add(managementPanel);
    events_cards.revalidate();
    events_cards.repaint();
    }

}