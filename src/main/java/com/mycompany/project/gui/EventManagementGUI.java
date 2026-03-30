/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project.gui;

import com.mycompany.project.classes.Events;
import com.mycompany.project.DB.EventsDAO;
import com.mycompany.project.DB.UserDAO;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class EventManagementGUI extends AttendeeGUI {
     public JButton manageEventsButton;
          public JButton analyticsEventsButton;

     public List<Events> events;
     public JButton add_button;
     public JPanel headerPanel;
    public EventManagementGUI(String title,UserDAO user) {
        super(title,user);
        manageEventsButton = new JButton("Manage Events");
manageEventsButton.setFont(largeFont);
 analyticsEventsButton = new JButton("Analytics Events");
 analyticsEventsButton.setFont(largeFont);
        manageEventsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAndDisplayEvents();
            }
        });
analyticsEventsButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        loadAnalytics();
    }
});
        toolbar.add(manageEventsButton); 
        toolbar.add(analyticsEventsButton);
        this.setSize(1024, 768);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void loadAndDisplayEvents() {
        events = evdao.getAllEvents();
        events_cards.removeAll();
        add_button = new JButton("+");
        headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel managementPanel = new JPanel();
        managementPanel.setLayout(new BoxLayout(managementPanel, BoxLayout.Y_AXIS));

        headerPanel.add(add_button);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, headerPanel.getPreferredSize().height));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        managementPanel.add(headerPanel);
 add_button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        EventAddDialog dialog = new EventAddDialog(EventManagementGUI.this);
        dialog.setLocationRelativeTo(EventManagementGUI.this);
        dialog.setVisible(true);
    }
});
        for (Events event : events) {
            JPanel eventPanel = new JPanel();
                    eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        eventPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel eventLabel_title = new JLabel("Event: " + event.getTitle());
            JLabel eventLabel_date = new JLabel("Date:" + event.getDate());
            JLabel eventLabel_registrations = new JLabel("Total Registrations:" + evdao.getRegistrationsCountForEvent(event.getEvent_id()));
            eventPanel.add(eventLabel_title);
            eventPanel.add(eventLabel_date);
            JButton editButton = new JButton("Edit");
            JButton remove_button = new JButton("Remove");
            editButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                      EventEditDialog dialog = new EventEditDialog(
                EventManagementGUI.this, 
                event                     
        );
        dialog.setLocationRelativeTo(EventManagementGUI.this);
        dialog.setVisible(true);

                }
            });
            remove_button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    EventRemoveDialog dialog = new EventRemoveDialog(
                          EventManagementGUI.this, 
                event);
                            dialog.setLocationRelativeTo(EventManagementGUI.this);
        dialog.setVisible(true);
                }

            });
            eventPanel.add(eventLabel_registrations);
            eventPanel.add(editButton);
            eventPanel.add(remove_button);
            managementPanel.add(eventPanel);
        }

        events_cards.add(managementPanel);
        
        events_cards.revalidate();
        events_cards.repaint();
    }

public void loadAnalytics() {
    events_cards.removeAll();
    events_cards.setLayout(new BorderLayout());

    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel title = new JLabel("Analytics");
    title.setFont(new Font("Segoe UI", Font.BOLD, 26));
    container.add(title);
    container.add(Box.createVerticalStrut(15));

    JLabel perEventTitle = new JLabel("Total registrations per event");
    perEventTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
    container.add(perEventTitle);
    container.add(Box.createVerticalStrut(8));

String[] cols1 = {"Title", "Total Registrations"};
    javax.swing.table.DefaultTableModel model1 = new javax.swing.table.DefaultTableModel(cols1, 0);

    for (Object[] row : evdao.getRegistrationsPerEvent()) {
        model1.addRow(row);
    }

    JTable table1 = new JTable(model1);
    JScrollPane sp1 = new JScrollPane(table1);
    sp1.setPreferredSize(new Dimension(900, 250));
    container.add(sp1);

    container.add(Box.createVerticalStrut(20));

    JLabel perCatTitle = new JLabel("Most attended event categories");
    perCatTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
    container.add(perCatTitle);
    container.add(Box.createVerticalStrut(8));

    String[] cols2 = {"Category", "Total Registrations"};
    javax.swing.table.DefaultTableModel model2 = new javax.swing.table.DefaultTableModel(cols2, 0);

    java.util.List<Object[]> catRows = evdao.getRegistrationsPerCategory();
    for (Object[] row : catRows) {
        model2.addRow(row);
    }

    JTable table2 = new JTable(model2);
    JScrollPane sp2 = new JScrollPane(table2);
    sp2.setPreferredSize(new Dimension(900, 250));
    container.add(sp2);

    if (!catRows.isEmpty()) {
        Object[] top = catRows.get(0);
        container.add(Box.createVerticalStrut(10));
        JLabel topLabel = new JLabel("Top category: " + top[0] + " (" + top[1] + " registrations)");
        topLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        container.add(topLabel);
    }

    events_cards.add(container, BorderLayout.CENTER);
    events_cards.revalidate();
    events_cards.repaint();
}

}