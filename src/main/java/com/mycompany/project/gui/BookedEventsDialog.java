package com.mycompany.project.gui;

import com.mycompany.project.classes.Events;
import com.mycompany.project.DB.EventsDAO;
import com.mycompany.project.classes.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class BookedEventsDialog extends JDialog {

    private JLabel usernameLabel;
    private JTextField usernameField;

    private JTable eventsTable;
    private DefaultTableModel model;

    private JButton closeButton;

    public BookedEventsDialog(Frame owner, User user) {
        super(owner, "Booked Events", true);

        JPanel main_panel = new JPanel(new GridLayout(3, 1, 8, 8));
        main_panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        usernameField.setText(user.getUsername());
        usernameField.setEditable(false);

        topRow.add(usernameLabel);
        topRow.add(usernameField);

        String[] cols = {"Title", "Date", "Category", "Location", "Ticket Num"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        eventsTable = new JTable(model);
        eventsTable.setRowHeight(24);

        JScrollPane tableScroll = new JScrollPane(eventsTable);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        bottomRow.add(closeButton);

        main_panel.add(topRow);
        main_panel.add(tableScroll);
        main_panel.add(bottomRow);

        setContentPane(main_panel);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        loadBookedEvents(user.getUsername());

        setSize(800, 450);
        setLocationRelativeTo(owner);
    }

    private void loadBookedEvents(String username) {
        model.setRowCount(0);

        List<Events> events = EventsDAO.getBookedEventsForUser(username);

        if (events == null || events.isEmpty()) {
            model.addRow(new Object[]{"No booked events", "", "", "", ""});
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        for (Events ev : events) {
            String dateStr = (ev.getDate() != null) ? sdf.format(ev.getDate()) : "";
            String ticketStr = (ev.getTicketNum() > 0) ? String.valueOf(ev.getTicketNum()) : "N/A";

            model.addRow(new Object[]{
                    ev.getTitle(),
                    dateStr,
                    ev.getCategory(),
                    ev.getLocaiton(),
                    ticketStr
            });
        }
    }
}
