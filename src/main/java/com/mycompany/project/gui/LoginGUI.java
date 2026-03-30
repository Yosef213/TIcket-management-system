
package com.mycompany.project.gui;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import com.mycompany.project.DB.UserDAO;
import com.mycompany.project.utils.ConfigManager;

import com.formdev.flatlaf.*;
public class LoginGUI extends JFrame implements ActionListener {
        private JLabel username_label;
        private JTextField username_textfield;
        private JLabel password_label;
        private JPasswordField password_passwordfield;
        private JButton login_button;
        private JPanel login_panel;
        private JButton register_button; 
                   private JOptionPane optionpane = new JOptionPane();
                   private ConfigManager cmg = new ConfigManager();
        public UserDAO user; 
               public boolean loginSuccessful = false;
        public JCheckBox remember_button;
        public LoginGUI(String title){
            
        super(title);
        this.setSize(1024, 768);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel main_panel = (JPanel) getContentPane();
        login_panel = new JPanel(new GridLayout(4,2));
        username_label = new JLabel("Username:");
        username_textfield = new JTextField(10);
        password_label = new JLabel("Password:");
        password_passwordfield = new JPasswordField(10);
        login_button = new JButton("Login");
        register_button = new JButton("Register");
        remember_button = new JCheckBox("Remember me");
        login_panel.add(username_label);
        login_panel.add(username_textfield);
        login_panel.add(password_label);
        login_panel.add(password_passwordfield);
        login_panel.add(login_button);
        login_panel.add(register_button);
        login_panel.add(remember_button);
        main_panel.add(login_panel);
        login_button.addActionListener(this);
        register_button.addActionListener(this);
        this.pack();
        this.setVisible(true);
        }
        public void actionPerformed(ActionEvent e){
        String username = username_textfield.getText().trim();
        char[] passwordChars = password_passwordfield.getPassword();
        String password = new String(passwordChars);
            if (e.getSource() == login_button){
                user = new UserDAO(username,password);
                if (user.Login()){
                    loginSuccessful = true;
                    if(remember_button.isSelected()){
                        cmg.writeConfig(user);
                    }
                    LoginGUI.this.dispose();
                }
                else{
                    JOptionPane.showMessageDialog(this,"Login Failed: Please Check your Credentials ", "Login failed:",JOptionPane.ERROR_MESSAGE);
                }
            }
            if (e.getSource() == register_button){
                RegisterGUI register = new RegisterGUI("Register");
            }
        }
        public static void main(String[] args) {
            FlatDarkLaf.setup();

                    LoginGUI login = new LoginGUI("Login");

    }
}
