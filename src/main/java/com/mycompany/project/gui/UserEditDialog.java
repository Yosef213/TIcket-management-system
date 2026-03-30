/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project.gui;
import com.mycompany.project.classes.User;
import com.mycompany.project.DB.AdminDAO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class UserEditDialog extends JDialog {
    private JLabel username_label;
    private JTextField username_field;
    private JLabel email_label;
    private JTextField email_field;
    private JLabel phoneNumber_label;
    private JTextField phoneNumber_field;
    public JLabel role_label;
    public ButtonGroup role_bg; 
    public JRadioButton attendee_rb;
    public JRadioButton event_organizer_rb;
    public JRadioButton admin_rb;
    private JPanel main_panel;
    private JButton save_button;
    private JButton cancel_button; 
    public UserEditDialog(Frame owner, User user){
                 super(owner, "Edit User", true);
                   main_panel = new JPanel(new GridLayout(5, 2, 8, 8));
                   username_label = new JLabel("Username:");
                   username_field = new JTextField(10);
                   username_field.setText(user.getUsername());
                   username_field.setEditable(false);
                   email_label = new JLabel("Email");
                   email_field = new JTextField(10);
                   email_field.setText(user.getEmail());
                   phoneNumber_label = new JLabel("Phone Number:");
                   phoneNumber_field = new JTextField(10);
                   phoneNumber_field.setText(user.getPhone());
                   role_label = new JLabel("Role:");
                   role_bg = new ButtonGroup();
                   attendee_rb = new JRadioButton("Attendee");
                   event_organizer_rb = new JRadioButton("Event Organizer");
                           admin_rb = new JRadioButton("Admin");
                           JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        role_bg.add(attendee_rb);
        role_bg.add(event_organizer_rb);
        role_bg.add(admin_rb);
        String currentRole = user.getRoleString();
          if (currentRole.equals("Admin")) {
                admin_rb.setSelected(true);
            } else if (currentRole.equals("Attendee")) {
                attendee_rb.setSelected(true);
            } else if (currentRole.equals("Event Organizer")) { 
                event_organizer_rb.setSelected(true);
            }
      
        
        
        rolePanel.add(attendee_rb);
        rolePanel.add(event_organizer_rb);
        rolePanel.add(admin_rb);
        save_button = new JButton("Save");
        cancel_button = new JButton("Cancel");
        main_panel.add(username_label);
        main_panel.add(username_field);
        main_panel.add(email_label);
        main_panel.add(email_field);
        main_panel.add(phoneNumber_label);
        main_panel.add(phoneNumber_field);
        main_panel.add(role_label);
        main_panel.add(rolePanel);
        main_panel.add(save_button);
        main_panel.add(cancel_button);
        cancel_button.addActionListener(e -> dispose());
        save_button.addActionListener(e -> saveUserChanges());
                 setContentPane(main_panel);
                 this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                 this.pack();
    }
    private void saveUserChanges(){
    String username = username_field.getText().trim();
    String email    = email_field.getText().trim();
    String phone    = phoneNumber_field.getText().trim();
    String role; 
      Pattern vaildEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
       boolean ValidEmail = vaildEmailPattern.matcher(email).matches();
        if (admin_rb.isSelected()) {
        role = "Admin";
    } else if (attendee_rb.isSelected()) {
        role = "Attendee";
    } else {
        role = "Event Organizer";
    }
        if ( email.isEmpty() || phone.isEmpty() || role.isEmpty()){
                JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields. None can be left blank.",
                "Registration Error: Missing Input",
                JOptionPane.ERROR_MESSAGE);
            }
        if (!ValidEmail){
                     JOptionPane.showMessageDialog(this,
                            "Please Ensure the email is correct",
                            "Email Error",
                            JOptionPane.ERROR_MESSAGE);
                                       return;
        }
        boolean ok = AdminDAO.updateUser(username,phone,email,role);
            if (ok) {
        JOptionPane.showMessageDialog(this, "User updated successfully.");
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Failed to update user.", "Error",
                                      JOptionPane.ERROR_MESSAGE);
    }
    }
}
