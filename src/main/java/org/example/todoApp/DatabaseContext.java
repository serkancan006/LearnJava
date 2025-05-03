package org.example.todoApp;

import org.example.anatations.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseContext {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=JavaLearnDb;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456Aa*";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
