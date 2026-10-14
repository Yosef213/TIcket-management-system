package com.mycompany.project.DB;

import java.sql.*;

public class DBHandler {

    private Connection connection;
    private PreparedStatement ps;

    public DBHandler() throws SQLException {
        try {
            this.connection = getConnection();
        } catch (Exception e) {
            throw new SQLException("Failed to initialize DB connection: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws Exception {
        final String URL = "jdbc:mysql://-.-.-.-:----/EventSystem";
        final String USER = "remote_user";
        final String PASS = "Qq11@@EeRr";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);

        } catch (Exception e) {
            throw new SQLException("An error happened while connecting to DB: " + e.getMessage(), e);
        }
    }

    public ResultSet executeQuery(String query, Object... params) throws SQLException {
        ps = connection.prepareStatement(query);
        setParameters(ps, params);
        return ps.executeQuery(); 
    }

    public int executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setParameters(ps, params);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

            return rowsAffected;

        } catch (SQLException e) {
            throw new SQLException("Failed to update the database. " + e.getMessage(), e);
        }
    }

    private void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    public void closeConnection() {
        try {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close database resources: " + e.getMessage(), e);
        }
    }
    
}
