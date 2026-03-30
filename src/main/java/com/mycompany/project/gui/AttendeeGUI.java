package com.mycompany.project.gui;
import com.toedter.calendar.JDateChooser;  
import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.project.DB.DBHandler;
import javax.swing.*;
import java.awt.*;
import com.mycompany.project.DB.EventsDAO;
import com.mycompany.project.classes.Events;
import com.mycompany.project.DB.RegistrationsDAO;
import com.mycompany.project.DB.UserDAO;
import java.awt.event.ActionEvent;
import com.mycompany.project.utils.QrUtil;
import java.sql.*;
import java.awt.event.ActionListener;
import java.util.List;
import com.mycompany.project.Project;

public class AttendeeGUI extends JFrame implements ActionListener {
    JPanel attendee_panel;
    JToolBar toolbar;
    JButton upcoming_button;
    JButton booked_button;
        JButton profile_button;

    JPanel toolbar_panel;
    EventsDAO evdao;
    List<Events> events;
    JPanel events_cards;
     UserDAO currentUser; 
                RegistrationsDAO regDAO = new RegistrationsDAO();
        Font largeFont = new Font("Segoe UI", Font.PLAIN, 25); 

    public AttendeeGUI(String title, UserDAO user) {
        super(title);
        currentUser = user; 
        this.setSize(1024, 768);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main_panel = (JPanel) getContentPane();
        main_panel.setLayout(new BorderLayout());

        toolbar_panel = new JPanel(new BorderLayout());

        toolbar = new JToolBar(JToolBar.VERTICAL);
        toolbar.setFloatable(false);

        upcoming_button = new 	JButton("Upcoming Events");
        booked_button = new 	JButton("Booked Events");
        profile_button = new 	JButton("My Profile");
        Font largeFont = new Font("Segoe UI", Font.PLAIN, 25);  
upcoming_button.setFont(largeFont);
booked_button.setFont(largeFont);
profile_button.setFont(largeFont);

        
        toolbar.add(upcoming_button);
        toolbar.add(booked_button);
        toolbar.add(profile_button);

        toolbar_panel.add(toolbar, BorderLayout.CENTER);
        main_panel.add(toolbar_panel, BorderLayout.WEST);

        events_cards = new JPanel();
        events_cards.setLayout(new BoxLayout(events_cards, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(events_cards);
        main_panel.add(scrollPane, BorderLayout.CENTER);

       evdao = new EventsDAO();

upcoming_button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        loadUpcomingEvents();
    }
});
        booked_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {

                loadBookedEvents();  
            }
        });
       loadUpcomingEvents();


        this.setVisible(true);
        profile_button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        loadUserProfile();
    }
});

    }
    
public void loadUpcomingEvents() {
    events_cards.removeAll();
    events_cards.setLayout(new BorderLayout());

    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    filterPanel.add(new JLabel("Category:"));
    JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"All", "Workshop", "Seminar", "Concert", "Festival"});
    filterPanel.add(categoryComboBox);

    filterPanel.add(new JLabel("Location:"));
    JComboBox<String> locationComboBox = new JComboBox<>(new String[]{"All", "Riyadh", "Jeddah", "Dammam", "Buraydah"});
    filterPanel.add(locationComboBox);

    filterPanel.add(new JLabel("Start Date:"));
    JDateChooser startDateChooser = new JDateChooser();
    filterPanel.add(startDateChooser);

    filterPanel.add(new JLabel("End Date:"));
    JDateChooser endDateChooser = new JDateChooser();
    filterPanel.add(endDateChooser);

    JButton filterButton = new JButton("Filter Events");
    filterPanel.add(filterButton);

    JPanel eventCardsContainer = new JPanel();
    eventCardsContainer.setLayout(new BoxLayout(eventCardsContainer, BoxLayout.Y_AXIS));

    events_cards.add(filterPanel, BorderLayout.NORTH);

    JScrollPane eventScrollPane = new JScrollPane(eventCardsContainer);
    eventScrollPane.setBorder(null);
    events_cards.add(eventScrollPane, BorderLayout.CENTER);

    java.util.Date today = new java.util.Date();
    startDateChooser.setMinSelectableDate(today);
    endDateChooser.setMinSelectableDate(today);
    startDateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
        java.util.Date s = startDateChooser.getDate();
        if (s == null) {
            endDateChooser.setMinSelectableDate(today);
            return;
        }

        endDateChooser.setMinSelectableDate(s);

        java.util.Date en = endDateChooser.getDate();
        if (en != null && en.before(s)) {
            endDateChooser.setDate(s);
        }
    });

    filterButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            String category = (String) categoryComboBox.getSelectedItem();
            String location = (String) locationComboBox.getSelectedItem();
            java.util.Date startDate = startDateChooser.getDate();
            java.util.Date endDate = endDateChooser.getDate();

            String sql = "SELECT event_id, title, date, category, location, seat_cap FROM Events WHERE date >= CURDATE()";

            if (category != null && !category.equals("All")) sql += " AND category = ?";
            if (location != null && !location.equals("All")) sql += " AND location = ?";
            if (startDate != null) sql += " AND date >= ?";
            if (endDate != null) sql += " AND date <= ?";

            try {
                DBHandler db = new DBHandler();
                PreparedStatement ps = db.getConnection().prepareStatement(sql);

                int paramIndex = 1;
                if (category != null && !category.equals("All")) ps.setString(paramIndex++, category);
                if (location != null && !location.equals("All")) ps.setString(paramIndex++, location);
                if (startDate != null) ps.setDate(paramIndex++, new java.sql.Date(startDate.getTime()));
                if (endDate != null) ps.setDate(paramIndex++, new java.sql.Date(endDate.getTime()));

                ResultSet rs = ps.executeQuery();
                eventCardsContainer.removeAll();

                if (rs != null && rs.next()) {
                    do {
                        int id = rs.getInt("event_id");
                        String title = rs.getString("title");
                        Date date = rs.getDate("date");
                        String eventCategory = rs.getString("category");
                        String eventLocation = rs.getString("location");
                        int seatCap = rs.getInt("seat_cap");

                        Events event = new Events(id, title, date, eventCategory, eventLocation, seatCap);
                        JPanel eventPanel = createEventCard(event);
                        eventCardsContainer.add(eventPanel);
                    } while (rs.next());
                } else {
                    eventCardsContainer.add(new JLabel("No events found."));
                }

                eventCardsContainer.revalidate();
                eventCardsContainer.repaint();

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                System.getLogger(AttendeeGUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    });

    filterButton.doClick();

    events_cards.revalidate();
    events_cards.repaint();
}


public void loadBookedEvents() {
  events_cards.removeAll();
    events_cards.setLayout(new BoxLayout(events_cards, BoxLayout.Y_AXIS));
    events = evdao.getBookedEventsForUser(currentUser.getUsername());  
   
   
    if (events != null && !events.isEmpty()) {
        for (Events e : events) {
            JPanel card = createEventCard(e);
            events_cards.add(card);

        }
    } else {
        events_cards.add(new JLabel("No booked events found."));
    }

    // Refresh the UI to show the new events
    events_cards.revalidate();
    events_cards.repaint();
}


   private JPanel createEventCard(Events event) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    Font largeFont = new Font("Segoe UI", Font.PLAIN, 18);
    JButton qrcode_button = new JButton("Show Ticket QR Code");
    JButton cancel_button = new JButton("Cancel Ticket");
    JButton waitlist_button = new JButton("Join Waitlist");

    // Display event details
   JLabel titleLabel = new JLabel("Title: " + event.getTitle());
    titleLabel.setFont(largeFont);
    panel.add(titleLabel);

    JLabel dateLabel = new JLabel("Date: " + event.getDate());
    dateLabel.setFont(largeFont);
    panel.add(dateLabel);

    JLabel categoryLabel = new JLabel("Category: " + event.getCategory());
    categoryLabel.setFont(largeFont);
    panel.add(categoryLabel);

    JLabel locationLabel = new JLabel("Location: " + event.getLocaiton());
    locationLabel.setFont(largeFont);
    panel.add(locationLabel);

    JLabel seatsAvailableLabel = new JLabel("Seats available: " + event.getSeatcap());
    seatsAvailableLabel.setFont(largeFont);
    panel.add(seatsAvailableLabel);
    
    panel.add(Box.createVerticalStrut(10));

    JButton ticketButton = new JButton("Generate Ticket");
    ticketButton.setActionCommand("" + event.getEvent_id());
    
    boolean isRegistered = regDAO.isUserRegisteredForEvent(currentUser.getUsername(), event.getEvent_id());
    boolean isOnWaitlist = regDAO.isUserOnWaitlist(currentUser.getUsername(), event.getEvent_id());
    
    if (isRegistered) {
        ticketButton.setText("Booked");
        ticketButton.setEnabled(false);
        
        if(calledFrom("loadBookedEvents")) {
            ticketButton.setVisible(false);
            ticketButton.setFont(largeFont);
            qrcode_button.setFont(largeFont);
            cancel_button.setFont(largeFont);
            panel.add(qrcode_button);
            panel.add(cancel_button);
            
            cancel_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int eventId = event.getEvent_id();
                    int confirm = JOptionPane.showConfirmDialog(
                            AttendeeGUI.this,
                            "Cancel this ticket?",
                            "Confirm Cancel",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm != JOptionPane.YES_OPTION) return;

                    boolean ok = regDAO.cancelRegistration(currentUser.getUsername(), eventId);

                    if (ok) {
                        String nextUser = regDAO.getNextWaitlistUser(eventId);
                        if (nextUser != null) {
                            regDAO.registerEvent(nextUser, eventId);
                            regDAO.generateTicket(nextUser, eventId);
                            regDAO.removeFromWaitlist(nextUser, eventId);
                            JOptionPane.showMessageDialog(null, 
                                "Ticket cancelled successfully! Waitlisted user has been registered.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Ticket cancelled successfully!");
                        }
                        refreshEventList();
                    } else {
                        JOptionPane.showMessageDialog(null, "Cancel failed.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            qrcode_button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try {
                        int eventId = event.getEvent_id();
                        int ticketNo = event.getTicketNum();

                        if (ticketNo <= 0) {
                            JOptionPane.showMessageDialog(null, "No ticket number found for this event.");
                            return;
                        }

                        java.awt.image.BufferedImage qr =
                                com.mycompany.project.utils.QrUtil.generateTicketQrImage(ticketNo, 300);
                        showQrDialog(qr, "Ticket QR - " + ticketNo);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Failed to generate QR: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
    }
    
    ticketButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int eventId = Integer.parseInt(e.getActionCommand());

            if (!regDAO.isUserRegisteredForEvent(currentUser.getUsername(), eventId)) {
                if (regDAO.registerEvent(currentUser.getUsername(), eventId)) {
                    regDAO.generateTicket(currentUser.getUsername(), eventId);
                    JOptionPane.showMessageDialog(null, "Ticket generated successfully!");
                    refreshEventList();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to register.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                regDAO.generateTicket(currentUser.getUsername(), eventId);
                JOptionPane.showMessageDialog(null, "Ticket generated successfully!");
                refreshEventList();
            }
        }
    });
    
    if(event.getSeatcap() <= 0 && !isRegistered) {
        ticketButton.setText("Not available");
        ticketButton.setEnabled(false);
        
        if (isOnWaitlist) {
            waitlist_button.setText("On Waitlist");
            waitlist_button.setEnabled(false);
        } else {
            waitlist_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int eventId = event.getEvent_id();
                    boolean added = regDAO.addToWaitlist(currentUser.getUsername(), eventId);
                    
                    if (added) {
                        JOptionPane.showMessageDialog(null, 
                            "You've been added to the waitlist! You'll be automatically registered when a seat becomes available.");
                        refreshEventList();
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "Failed to join waitlist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
        waitlist_button.setFont(largeFont); 
        panel.add(waitlist_button);
    }
    
    panel.add(ticketButton);

    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));

    return panel;
}
private void refreshEventList() {
    loadBookedEvents();
    events_cards.revalidate();
    events_cards.repaint();
}
    public void actionPerformed(ActionEvent e) {
                int eventId =Integer.parseInt(e.getActionCommand());
                   Events selected = null;
            if (events != null) {
                for (Events ev : events) {
                    if (ev.getEvent_id() == eventId) {
                        selected = ev;
                        break;
                    }
                }
            }


        }
   public void loadUserProfile() {

    events_cards.removeAll(); 

    JPanel profilePanel = new JPanel();
    profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
    profilePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
    profilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel title = new JLabel("My Profile");
    title.setFont(new Font("Segoe UI", Font.BOLD, 28));
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    profilePanel.add(title);
    profilePanel.add(Box.createVerticalStrut(30));

    profilePanel.add(createProfileLabel("Username: " + currentUser.getUsername()));
    profilePanel.add(Box.createVerticalStrut(15));

    profilePanel.add(createProfileLabel("Email: " + currentUser.getEmail())); 
    profilePanel.add(Box.createVerticalStrut(15));

    profilePanel.add(createProfileLabel("Phone Number: " + currentUser.getPhone()));
    profilePanel.add(Box.createVerticalStrut(25));
    
    JButton editButton = new JButton("Edit Profile");
    JButton logout_button = new JButton("Logout");
    editButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    editButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    editButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            UserEditDialog dialog = new UserEditDialog(AttendeeGUI.this, currentUser);
            dialog.attendee_rb.setVisible(false);
            dialog.event_organizer_rb.setVisible(false);
            dialog.admin_rb.setVisible(false);
            dialog.role_label.setVisible(false);
            dialog.setLocationRelativeTo(AttendeeGUI.this);
            dialog.setVisible(true); 
            

            loadUserProfile();
        }
    });
    logout_button.addActionListener(e -> {
    new com.mycompany.project.utils.ConfigManager().clearConfig(); 
    this.dispose();                                              
    javax.swing.SwingUtilities.invokeLater(Project::new);          
});
    
    profilePanel.add(editButton);
    profilePanel.add(logout_button);
    profilePanel.add(Box.createVerticalGlue());

    events_cards.add(profilePanel);
    events_cards.revalidate();
    events_cards.repaint();
}

private JLabel createProfileLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    return label;
}
private void showQrDialog(java.awt.image.BufferedImage img, String title) {
    JDialog dialog = new JDialog(this, title, true);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    JLabel label = new JLabel(new ImageIcon(img));
    label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    dialog.getContentPane().add(label, BorderLayout.CENTER);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}
private boolean calledFrom(String methodName) {
    for (StackTraceElement el : Thread.currentThread().getStackTrace()) {
        if (el.getMethodName().equals(methodName)) return true;
    }
    return false;
}

}