package com.model;

import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/project";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345678";


    public DatabaseConnector() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}