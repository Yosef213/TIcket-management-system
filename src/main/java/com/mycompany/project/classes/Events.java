/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.project.classes;
import java.util.Date;

public class Events {
    private int event_id;
    private String title;
    private Date date;
    public String[] category_array = {"Workshop","Seminar","Concert","Festival"};
    private String category;
        public String[] location_array = {"Riyadh","Buryadh","Jeddah","Dammam"};

    private String location;
    private int seat_cap;
    private Integer ticketNum;
    /*public Events(int event_id, String title, Date date, String category, String location, int seat_cap) {
        this.event_id = event_id;
        this.title = title;
        this.date = date;
        this.category = category;
        this.location = location;
        this.seat_cap = seat_cap;
        
    }*/
    public Events(int event_id, String title, Date date, String category, String location, int seat_cap) {
        this.event_id = event_id;
        this.title = title;
        this.date = date;
        this.category = category;
        this.location = location;
        this.seat_cap = seat_cap;
        this.ticketNum = ticketNum;
    }

    public int getEvent_id() {
        return event_id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getLocaiton() {
        return location;
    }

    public int getSeatcap() {
        return seat_cap;
    }
    public Integer getTicketNum() {
        return ticketNum;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLocaiton(String locaiton) {
        this.location = location;
    }

    public void setSeatcap(int seatcap) {
        this.seat_cap = seat_cap;
    }
    public void setTicketNum(Integer ticketNum) {
        this.ticketNum = ticketNum;
    }
    
    
}