package com.model;

import java.sql.*;

public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/project";
    private static final String USER = "postgres";
    private static final String PASSWORD = "100DjDf800";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}