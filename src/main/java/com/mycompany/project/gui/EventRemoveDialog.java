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
public class EventRemoveDialog extends JDialog implements ActionListener {
            private Events event;
            private JLabel confirmation_label; 
            private JButton confirm_button;
            private JButton cancel_button; 
                    private JPanel main_panel;
            private JPanel button_panel; 
    public EventRemoveDialog(Frame owner, Events event){
        super(owner, "Remove " + event.getTitle(), true);
        this.event = event; 
                 main_panel = new JPanel(new GridLayout(2, 1, 10, 10));
        confirmation_label = new JLabel("Are you sure you want to remove this event?");
        button_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        confirm_button = new JButton("Confirm");
        cancel_button = new JButton("Cancel");
            cancel_button.addActionListener(this);
    confirm_button.addActionListener(this);
        button_panel.add(confirm_button);
        button_panel.add(cancel_button);
        main_panel.add(confirmation_label);
        main_panel.add(button_panel);
                 setContentPane(main_panel);
                 this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                 this.pack();
    }
     public void actionPerformed(ActionEvent e){
         EventsDAO evdao = new EventsDAO();
         if (e.getSource() == cancel_button){
             this.dispose();
         }
         else if (e.getSource() == confirm_button){
             if(evdao.removeEvent(event.getEvent_id())){
                  JOptionPane.showMessageDialog(this, "Remove Sucess","Sucesss",JOptionPane.INFORMATION_MESSAGE);
                  this.dispose();
             }
             else {
               JOptionPane.showMessageDialog(this,"Remove Failed", "Failed",JOptionPane.ERROR_MESSAGE);
               this.dispose();
             }
         }
     }
}
