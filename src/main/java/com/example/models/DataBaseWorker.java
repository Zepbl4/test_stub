package com.example.models;


import java.sql.*;

public class DataBaseWorker {
    private final String url = "jdbc:postgresql://192.168.0.11:5432/mydatabase";
    private final String username = "admin";
    private final String password = "admin";


    public User getUserByLogin(String login) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DriverManager.getConnection(url, username, password);

            statement = connection.createStatement();

            String sql = "SELECT users.login, users.password, users.date, emails.email " +
                    "FROM users LEFT JOIN emails ON users.login = emails.login " +
                    "WHERE users.login = '" + login + "'";

            resultSet = statement.executeQuery(sql);

            resultSet.next();
            if (login.equals(resultSet.getString("login"))) {
                user = new User(
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getDate("date"),
                        resultSet.getString("email")
                );
            }
            return user;

        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            return null;

        } finally {

            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();

            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public String insertUser(User user) {
        String insertUserSql = "INSERT INTO users (login, password, date) VALUES (?, ?, ?);\nINSERT INTO emails (login, email) VALUES (?, ?);";

        int affectedRows = 0;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement userStatement = connection.prepareStatement(insertUserSql)) {

            connection.setAutoCommit(false);

            userStatement.setString(1, user.getLogin());
            userStatement.setString(2, user.getPassword());
            userStatement.setDate(3, new java.sql.Date(user.getDate().getTime()));

            userStatement.setString(4, user.getLogin());
            userStatement.setString(5, user.getEmail());
            affectedRows = userStatement.executeUpdate();

            connection.commit();

            return "Rows affected: " + affectedRows;

        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }


    }
}

