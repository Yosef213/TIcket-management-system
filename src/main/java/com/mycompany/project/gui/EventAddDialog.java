package com.mycompany.project.gui;

import com.mycompany.project.DB.EventsDAO;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class EventAddDialog extends JDialog implements ActionListener {

    private JLabel event_titlelabel;
    private JTextField event_titlefield;

    private JLabel event_datelabel;
    private JButton event_datechange;
    private JDateChooser dateChooser;

    private JLabel event_categorylabel;
    private JComboBox event_categoryfield;

    private JLabel event_locationlabel;
    private JComboBox event_locationfield;

    private JLabel event_seatcaplabel;
    private JTextField event_seatcapfield;

    private JButton save_button;
    private JButton cancel_button;
    private JPanel main_panel;

    private final EventsDAO evdao = new EventsDAO();
    public String[] category_array = {"Workshop","Seminar","Concert","Festival"};
        public String[] location_array = {"Riyadh","Buraydah","Jeddah","Dammam"};

    public EventAddDialog(Frame owner) {
        super(owner, "Add Event", true);

        main_panel = new JPanel(new GridLayout(7, 2, 4, 4));

        event_titlelabel = new JLabel("Event Title");
        event_titlefield = new JTextField(15);

        event_datelabel = new JLabel("Event Date");

        java.util.Date today = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        event_datechange = new JButton(sdf.format(today));
        dateChooser = new JDateChooser();
        dateChooser.setDate(today);
        dateChooser.setMinSelectableDate(today);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        editor.setEditable(false);
        editor.setVisible(false);

        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.add(event_datechange, BorderLayout.CENTER);
        datePanel.add(dateChooser, BorderLayout.EAST);

        event_datechange.addActionListener(e -> {
            dateChooser.getJCalendar().setWeekOfYearVisible(true);
            dateChooser.getCalendarButton().doClick();
            dateChooser.requestFocusInWindow();
        });

        dateChooser.addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                java.util.Date newDate = (java.util.Date) evt.getNewValue();
                if (newDate != null) {
                    event_datechange.setText(sdf.format(newDate));
                }
            }
        });

        event_categorylabel = new JLabel("Event Category");
        event_categoryfield = new JComboBox(category_array);

        event_locationlabel = new JLabel("Event Location");
        event_locationfield = new JComboBox(location_array);

        event_seatcaplabel = new JLabel("Seat Cap");
        event_seatcapfield = new JTextField(10);

        save_button = new JButton("Save");
        cancel_button = new JButton("Cancel");

        save_button.addActionListener(this);
        cancel_button.addActionListener(this);

        main_panel.add(event_titlelabel);
        main_panel.add(event_titlefield);

        main_panel.add(event_datelabel);
        main_panel.add(datePanel);

        main_panel.add(event_categorylabel);
        main_panel.add(event_categoryfield);

        main_panel.add(event_locationlabel);
        main_panel.add(event_locationfield);

        main_panel.add(event_seatcaplabel);
        main_panel.add(event_seatcapfield);

        main_panel.add(save_button);
        main_panel.add(cancel_button);

        setContentPane(main_panel);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel_button) {
            dispose();
            return;
        }

        if (e.getSource() == save_button) {
            int seatcap;
            String title = event_titlefield.getText();

            try {
                seatcap = Integer.parseInt(event_seatcapfield.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a number in seatcap",
                        "Seatcap is not a number",
                        JOptionPane.ERROR_MESSAGE
                        
                );
                
                return;
            }
            if (seatcap <= 0) {
                                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a number positive in seatcap",
                        "Seat Capacity is not a positive number",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
if (title == null || title.trim().isEmpty()) {
    JOptionPane.showMessageDialog(
        this,
        "Title field cannot be empty",
        "Title field Empty!",
        JOptionPane.ERROR_MESSAGE
    );
    return;
}
            boolean ok = evdao.addEvent(   
                    event_titlefield.getText().trim(),
                    dateChooser.getDate(),
                    event_categoryfield.getSelectedItem().toString().trim(),
                    event_locationfield.getSelectedItem().toString().trim(),
                    seatcap
            );

            if (ok) {
                JOptionPane.showMessageDialog(
                        this,
                        "Event added successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add event",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
