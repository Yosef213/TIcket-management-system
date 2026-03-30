package com.mycompany.project;

import javax.swing.*;
import java.awt.event.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.project.DB.UserDAO;
import com.mycompany.project.gui.*;

import com.mycompany.project.utils.ConfigManager;
public class Project {
    public UserDAO currentUser;
    private final ConfigManager cmg = new ConfigManager();

    public Project () {
        if (cmg.checkConfig()) {
            UserDAO u = cmg.readConfig();
            if (u != null) {
                currentUser = u;
                currentUser.setUserInformation();
                openByRole(currentUser);
                return; 
            }
        }


        LoginGUI login = new LoginGUI("Login");
        login.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (login.loginSuccessful) {
                    currentUser = login.user;
                    currentUser.setUserInformation();
                    openByRole(currentUser);
                } else {
                    System.exit(0);
                }
            }
        });

        login.setVisible(true);
    }

    private void openByRole(UserDAO user) {
        switch (user.getRoleString()) {
            case "Admin":
                new AdminGUI("Admin Menu", user).setVisible(true);
                break;
            case "Attendee":
                new AttendeeGUI("Attendee Menu", user).setVisible(true);
                break;
            case "Event_Organizer":
                new EventManagementGUI("Event Organizer Menu", user).setVisible(true);
                break;
            default:
                new LoginGUI("Login").setVisible(true);
                break;
        }
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
                SwingUtilities.invokeLater(Project::new);

    }
}
