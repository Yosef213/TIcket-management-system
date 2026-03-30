package com.mycompany.project.DB;

import com.mycompany.project.classes.Events;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventsDAO {

    public static List<Events> getAllEvents() {
        String sql = "SELECT event_id, title, date, category, location, seat_cap FROM Events";
        List<Events> events = new ArrayList<>();

        try {
            DBHandler db = new DBHandler();
            ResultSet rs = db.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("event_id");
                String title = rs.getString("title");
                Timestamp ts = rs.getTimestamp("date");
                Date date = (ts != null) ? new Date(ts.getTime()) : null;
                String category = rs.getString("category");
                String location = rs.getString("location");
                int seatCap = rs.getInt("seat_cap");

                Events ev = new Events(id, title, date, category, location, seatCap);
                events.add(ev);
            }

            rs.close();
            db.closeConnection();

        } catch (Exception e) {
            System.err.println("Error loading events: " + e.getMessage());
        }

        return events;
    }

public static List<Events> getBookedEventsForUser(String username) {
    String sql = "SELECT e.event_id, e.title, e.date, e.category, e.location, e.seat_cap, t.ticket_num " +
                 "FROM Events e " +
                 "JOIN registrations r ON e.event_id = r.event_id " +
                 "LEFT JOIN tickets t ON r.registration_id = t.registration_id " +
                 "WHERE r.username = ?"; 

    List<Events> events = new ArrayList<>();

    try {
        DBHandler db = new DBHandler();
        ResultSet rs = db.executeQuery(sql, username);

        while (rs.next()) {
            int id = rs.getInt("event_id");
            String title = rs.getString("title");
            Timestamp ts = rs.getTimestamp("date");
            Date date = (ts != null) ? new Date(ts.getTime()) : null;
            String category = rs.getString("category");
            String location = rs.getString("location");
            int seatCap = rs.getInt("seat_cap");
            int ticketNum = rs.getInt("ticket_num"); 
            boolean hasTicket = !rs.wasNull(); 

            Events ev = new Events(id, title, date, category, location, seatCap);
      
            if (hasTicket) {
                ev.setTicketNum(ticketNum); 
            }
            
            events.add(ev);
        }
        rs.close();
        db.closeConnection();

    } catch (SQLException e) {
        System.err.println("Error fetching booked events: " + e.getMessage());
    }

    return events;
}
 public static boolean updateEvent(int eventId, String title, Date date, String category, String location, int seatCap) {
        String sql = "UPDATE Events SET title = ?, date = ?, category = ?, location = ?, seat_cap = ? WHERE event_id = ?";
        boolean isUpdated = false;

        try {
            DBHandler db = new DBHandler();
            int rowsUpdated = db.executeUpdate(sql, title, new Timestamp(date.getTime()), category, location, seatCap, eventId);

            if (rowsUpdated > 0) {
                isUpdated = true; 
            }

            db.closeConnection();

        } catch (SQLException e) {
            System.err.println("Error updating event: " + e.getMessage());
        }

        return isUpdated; 
    }
public static boolean addEvent(String title, Date date, String category, String location, int seatCap) {
    String sql = "INSERT INTO Events (title, date, category, location, seat_cap) " +
                 "VALUES (?, ?, ?, ?, ?)";

    boolean isInserted = false;

    try {
        DBHandler db = new DBHandler();
        
        int generated = db.executeUpdate(
            sql, 
            title, 
            new java.sql.Timestamp(date.getTime()), 
            category, 
            location, 
            seatCap
        );

        isInserted = generated > 0;
        
 
        db.closeConnection();

    } catch (SQLException e) {
        System.err.println("Error adding event: " + e.getMessage());
    }

    return isInserted;
}
public static boolean removeEvent(int eventId) {
        String sql = "DELETE FROM Events WHERE event_id = ?";
        boolean isDeleted = false;

        try {
            DBHandler db = new DBHandler();
            db.executeUpdate("DELETE FROM tickets WHERE registration_id IN (SELECT registration_id FROM registrations WHERE event_id = ?)", eventId);
            db.executeUpdate("DELETE FROM registrations WHERE event_id = ?", eventId);
            int rowsDeleted = db.executeUpdate(sql, eventId);

            if (rowsDeleted > 0) {
                isDeleted = true;
            }

            db.closeConnection();

        } catch (SQLException e) {
            System.err.println("Error removing event with ID " + eventId + ": " + e.getMessage());
        }

        return isDeleted;
    }
public int getRegistrationsCountForEvent(int eventId) {
    String sql = "SELECT COUNT(*) FROM registrations WHERE event_id = ?";
    try {
        DBHandler db = new DBHandler();
        ResultSet rs = db.executeQuery(sql, eventId);
        int count = 0;
        if (rs.next()) count = rs.getInt(1);
        rs.close();
        db.closeConnection();
        return count;
    } catch (Exception e) {
        e.printStackTrace();
        return 0;
    }
}
public java.util.List<Object[]> getRegistrationsPerEvent() {
    String sql =
        "SELECT e.title, COUNT(r.registration_id) AS total " +
        "FROM Events e " +
        "LEFT JOIN registrations r ON e.event_id = r.event_id " +
        "GROUP BY e.event_id, e.title " +
        "ORDER BY total DESC";

    java.util.List<Object[]> rows = new java.util.ArrayList<>();

    try {
        DBHandler db = new DBHandler();
        ResultSet rs = db.executeQuery(sql);

        while (rs.next()) {
            rows.add(new Object[]{
                rs.getString("title"),
                rs.getInt("total")
            });
        }

        rs.close();
        db.closeConnection();
    } catch (Exception e) {
        System.err.println("Error analytics per event: " + e.getMessage());
    }

    return rows;
}


public java.util.List<Object[]> getRegistrationsPerCategory() {
    String sql =
        "SELECT e.category, COUNT(r.registration_id) AS total " +
        "FROM Events e " +
        "LEFT JOIN registrations r ON e.event_id = r.event_id " +
        "GROUP BY e.category " +
        "ORDER BY total DESC";

    java.util.List<Object[]> rows = new java.util.ArrayList<>();

    try {
        DBHandler db = new DBHandler();
        ResultSet rs = db.executeQuery(sql);

        while (rs.next()) {
            rows.add(new Object[]{
                rs.getString("category"),
                rs.getInt("total")
            });
        }

        rs.close();
        db.closeConnection();
    } catch (Exception e) {
        System.err.println("Error analytics per category: " + e.getMessage());
    }

    return rows;
}

}
