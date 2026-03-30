package com.mycompany.project.DB;

import java.sql.*;

public class RegistrationsDAO {

    public RegistrationsDAO() {

    }


public boolean registerEvent(String username, int eventId) {
        if (isUserRegisteredForEvent(username, eventId)) {
            return false; 
        }

        String insertQuery = "INSERT INTO registrations (username, event_id) VALUES (?, ?)";
        String updateSeatsQuery = "UPDATE Events SET seat_cap = seat_cap - 1 WHERE event_id = ? AND seat_cap > 0";
        try {
            DBHandler db = new DBHandler();
            int rowsAffected = db.executeUpdate(insertQuery, username, eventId);

            if (rowsAffected > 0) {
                db.executeUpdate(updateSeatsQuery, eventId);
            }
            db.closeConnection();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to generate a ticket for the registered event
    public void generateTicket(String username, int eventId) {
        String getRegistrationQuery = "SELECT registration_id FROM registrations WHERE username = ? AND event_id = ?";
        
        try {
            DBHandler db = new DBHandler();
            ResultSet rs = db.executeQuery(getRegistrationQuery, username, eventId);

            if (rs.next()) {
                int registrationId = rs.getInt("registration_id");
                rs.close(); // Close this result set before starting a new query on the same connection if needed

                // Check for duplicate ticket
                String checkTicketQuery = "SELECT COUNT(*) FROM tickets WHERE registration_id = ?";
                ResultSet checkRs = db.executeQuery(checkTicketQuery, registrationId);
                
                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    System.out.println("Ticket already generated.");
                    checkRs.close();
                    db.closeConnection();
                    return;
                }
                checkRs.close();

                // Generate and Insert Ticket
                int ticketNum = (int) (Math.random() * 1000000); 
                String ticketInsertQuery = "INSERT INTO tickets (registration_id, ticket_num, status) VALUES (?, ?, 'Valid')";
                
                db.executeUpdate(ticketInsertQuery, registrationId, ticketNum);
                System.out.println("Ticket generated: " + ticketNum);
            } else {
                rs.close();
            }
            db.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserRegisteredForEvent(String username, int eventId) {
        String query = "SELECT COUNT(*) FROM registrations WHERE username = ? AND event_id = ?";
        boolean isRegistered = false;
        try {
            DBHandler db = new DBHandler();
            ResultSet rs = db.executeQuery(query, username, eventId);
            if (rs.next()) {
                isRegistered = rs.getInt(1) > 0;
            }
            rs.close();
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isRegistered;
    }
    public boolean cancelRegistration(String username, int eventId) {
    String getRegIdQuery = "SELECT registration_id FROM registrations WHERE username = ? AND event_id = ?";
    String deleteTicketQuery = "DELETE FROM tickets WHERE registration_id = ?";
    String deleteRegQuery = "DELETE FROM registrations WHERE registration_id = ?";
    String incSeatQuery = "UPDATE Events SET seat_cap = seat_cap + 1 WHERE event_id = ?";

    try {
        DBHandler db = new DBHandler();

        ResultSet rs = db.executeQuery(getRegIdQuery, username, eventId);
        if (!rs.next()) {
            rs.close();
            db.closeConnection();
            return false; // not registered
        }

        int regId = rs.getInt("registration_id");
        rs.close();

        // 1) remove ticket(s)
        db.executeUpdate(deleteTicketQuery, regId);

        // 2) remove registration (this is the “main” success condition)
        int deleted = db.executeUpdate(deleteRegQuery, regId);

        // 3) increase seat cap only if registration actually deleted
        if (deleted > 0) {
            db.executeUpdate(incSeatQuery, eventId);
        }

        db.closeConnection();
        return deleted > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    public boolean isUserOnWaitlist(String username, int eventId) {
    String sql = "SELECT COUNT(*) FROM waitlist WHERE username = ? AND event_id = ?";
    try {
        DBHandler db = new DBHandler();
        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ps.setString(1, username);
        ps.setInt(2, eventId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    }   catch (Exception ex) {
            System.getLogger(RegistrationsDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    return false;
}


public boolean addToWaitlist(String username, int eventId) {
    String sql = "INSERT INTO waitlist (username, event_id, added_date) VALUES (?, ?, NOW())";
    try {
        DBHandler db = new DBHandler();
        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ps.setString(1, username);
        ps.setInt(2, eventId);
        int result = ps.executeUpdate();
        db.closeConnection();
        return result > 0;
    }   catch (Exception ex) {
            System.getLogger(RegistrationsDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
}


public String getNextWaitlistUser(int eventId) {
    String sql = "SELECT username FROM waitlist WHERE event_id = ? ORDER BY added_date ASC LIMIT 1";
    try {
        DBHandler db = new DBHandler();
        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ps.setInt(1, eventId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String username = rs.getString("username");
            db.closeConnection();
            return username;
        }
        db.closeConnection();
    }   catch (Exception ex) {
            System.getLogger(RegistrationsDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    return null;
}


public boolean removeFromWaitlist(String username, int eventId) {
    String sql = "DELETE FROM waitlist WHERE username = ? AND event_id = ?";
    try {
        DBHandler db = new DBHandler();
        PreparedStatement ps = db.getConnection().prepareStatement(sql);
        ps.setString(1, username);
        ps.setInt(2, eventId);
        int result = ps.executeUpdate();
        db.closeConnection();
        return result > 0;
    }   catch (Exception ex) {
            System.getLogger(RegistrationsDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
}

}