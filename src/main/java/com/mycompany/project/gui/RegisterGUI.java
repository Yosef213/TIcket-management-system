
package com.mycompany.project.gui;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import com.mycompany.project.DB.UserDAO;
import java.util.regex.Pattern;
public class RegisterGUI extends JFrame implements ActionListener{
            private JLabel username_label;
        private JTextField username_textfield;
             private JLabel password_label;
        private JPasswordField password_passwordfield;
        private JLabel email_label;
        private JTextField email_textfield;
        private JLabel phonenum_label;
        private JTextField phonenum_textfield;
        private JButton register_button; 
        private JPanel register_panel;
           private JOptionPane optionpane = new JOptionPane();
    public RegisterGUI(String title){
         super(title);
        this.setSize(1024, 768);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel main_panel = (JPanel) getContentPane();
        register_panel = new JPanel(new GridLayout(5,2));
        username_label = new JLabel("Username:");
        username_textfield = new JTextField(10);
        password_label = new JLabel("Password:");
        password_label.setToolTipText("Password Must be longer than 8 and has at least one capital letter on it");
        password_passwordfield = new JPasswordField(10);
        email_label = new JLabel("Email:");
        email_textfield = new JTextField(15);
        phonenum_label = new JLabel("Phone Number:");
        phonenum_textfield = new JTextField(10);
        register_button = new JButton("Register");
        register_button.addActionListener(this);
        register_panel.add(username_label);
        register_panel.add(username_textfield);
        register_panel.add(password_label);
        register_panel.add(password_passwordfield);
        register_panel.add(email_label);
        register_panel.add(email_textfield);
        register_panel.add(phonenum_label);
                register_panel.add(phonenum_textfield);
                register_panel.add(register_button);

        main_panel.add(register_panel);
                this.pack();
        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        String username = username_textfield.getText().trim();
        char[] passwordChars = password_passwordfield.getPassword();
        String password = new String(passwordChars);
        String email = email_textfield.getText().trim();
        String phoneNumber = phonenum_textfield.getText().trim();
        Pattern capitalLetterPattern = Pattern.compile("[A-Z]");
       boolean hasCapitalLetter = capitalLetterPattern.matcher(password).find();
       Pattern vaildEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
       boolean ValidEmail = vaildEmailPattern.matcher(email).matches();
        if (e.getSource() == register_button){
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()){
                JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields. None can be left blank.",
                "Registration Error: Missing Input",
                JOptionPane.ERROR_MESSAGE);
            }
            else { 
                if (password.length() < 8 || !hasCapitalLetter){
                    JOptionPane.showMessageDialog(this,"Please ensure the password length is more than 8 and have a one Capital letter at least on it",
                            "Password Error",
                            JOptionPane.ERROR_MESSAGE);
                                       return;

                }
                if (!ValidEmail){
                    JOptionPane.showMessageDialog(this,
                            "Please Ensure the email is correct",
                            "Email Error",
                            JOptionPane.ERROR_MESSAGE);
                                       return;

                }
                else {
                    UserDAO user = new UserDAO(username,password,phoneNumber,email);
                    try {
                        user.insertUser();
                        JOptionPane.showMessageDialog(this, "Register Sucess","Sucesss",JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch (Exception ex ){
                        JOptionPane.showMessageDialog(this,ex.getMessage(), "Registration failed:",JOptionPane.ERROR_MESSAGE);
                    }
                   this.dispose();
                }
            }
        }
        }

}
