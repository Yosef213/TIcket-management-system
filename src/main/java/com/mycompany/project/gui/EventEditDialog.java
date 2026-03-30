/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project.gui;

import com.mycompany.project.classes.Events;
import com.mycompany.project.DB.EventsDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
    import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
public class EventEditDialog extends JDialog implements ActionListener {
    private JLabel event_titlelabel;
    private JTextField event_titlefield;
    private JLabel event_date; 
    private JButton event_datechange;
    private JLabel event_categorylabel;
    private JComboBox event_categoryfield;
    private JLabel event_locationlabel;
    private JComboBox event_locationfield;
    private JLabel event_seatcaplabel;
    private JTextField event_seatcapfield;
    private JDateChooser dateChooser;
    private JButton save_button;
    private JButton cancel_button; 
        private JPanel main_panel;
        private Events event;
            public String[] category_array = {"Workshop","Seminar","Concert","Festival"};
        public String[] location_array = {"Riyadh","Buraydah","Jeddah","Dammam"};

    public EventEditDialog(Frame owner, Events event){
         super(owner, "Edit Event", true);
         this.event = event; 
         main_panel = new JPanel(new GridLayout(7,2,4,4));
         event_titlelabel = new JLabel("Event Title");
         event_titlefield = new JTextField(15);
         event_titlefield.setText(event.getTitle());
         event_date = new JLabel("Event Date");
         event_datechange = new JButton(new SimpleDateFormat("MM/dd/yyyy").format(event.getDate()));
dateChooser = new JDateChooser();
dateChooser.setDate(event.getDate());

java.util.Date today = new java.util.Date();                
dateChooser.setMinSelectableDate(today);                     
if (dateChooser.getDate() != null && dateChooser.getDate().before(today)) {  
    dateChooser.setDate(today);                           
    event_datechange.setText(new SimpleDateFormat("MM/dd/yyyy").format(today));
}
         JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        editor.setEditable(false);
        editor.setVisible(false);
          JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.add(event_datechange, BorderLayout.CENTER);
        datePanel.add(dateChooser, BorderLayout.EAST);
      event_datechange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateChooser.getJCalendar().setWeekOfYearVisible(true);
                dateChooser.getCalendarButton().doClick();
                dateChooser.requestFocusInWindow();
            }
        });
         dateChooser.addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                java.util.Date newDate = (java.util.Date) evt.getNewValue();
                if (newDate != null) {
                    event_datechange.setText(
                        new SimpleDateFormat("MM/dd/yyyy").format(newDate)
                    );
                }
            }
        });
         event_categorylabel = new JLabel("Event Category");
         event_categoryfield = new JComboBox(category_array);
         selectItemByString(event_categoryfield,event.getCategory());
         event_locationlabel = new JLabel("Event Location");
         event_locationfield = new JComboBox(location_array);
         selectItemByString(event_locationfield,event.getLocaiton());
         event_seatcaplabel = new JLabel("Seat Cap");
         event_seatcapfield = new JTextField(10);
         event_seatcapfield.setText(Integer.toString(event.getSeatcap()));
         save_button = new JButton("Save");
         save_button.addActionListener(this);
         cancel_button = new JButton("Cancel");
         cancel_button.addActionListener(this);
         main_panel.add(event_titlelabel);
         main_panel.add(event_titlefield);
         main_panel.add(event_date);
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
                 this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                 this.pack();
    }
    public void actionPerformed(ActionEvent e){
                EventsDAO evdao = new EventsDAO();
    int editedseatcap = event.getSeatcap();
    if (e.getSource() == cancel_button){
        this.dispose();
    }
    else if (e.getSource() == save_button){
                    String title = event_titlefield.getText();

        try {
            editedseatcap = Integer.parseInt(event_seatcapfield.getText());
        }
        catch(Exception ex){
                                    JOptionPane.showMessageDialog(this,"Please enter a number in seatcap", "Seatcap is not a number:",JOptionPane.ERROR_MESSAGE);
                                    return;

        }
        if (editedseatcap <= 0) {
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
        if (evdao.updateEvent(
    event.getEvent_id(),
    event_titlefield.getText(),
    dateChooser.getDate(),
    event_categoryfield.getSelectedItem().toString().trim(),
    event_locationfield.getSelectedItem().toString().trim(),
    editedseatcap
)){
            JOptionPane.showMessageDialog(this, "Edit Sucess","Sucesss",JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        }
    }
    }
      private static void selectItemByString(JComboBox cb, String getter) {
    for (int i=0; i<cb.getItemCount(); i++) {
      if (cb.getItemAt(i).equals(getter)) {
        cb.setSelectedIndex(i);
        break;
      }
    }
    return;
  }
}
