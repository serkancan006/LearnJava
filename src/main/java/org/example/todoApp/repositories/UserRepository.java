package org.example.todoApp.repositories;

import org.example.todoApp.DatabaseContext;
import org.example.todoApp.models.User;

import java.sql.*;

public class UserRepository {
    // Varolan kullanıcı adı kontrolü
    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT 1 FROM Users WHERE Username = ?";
        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, username);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next();
            }
        }
    }

    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, username);
            p.setString(2, password);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("Id"),
                            rs.getString("Username"),
                            rs.getString("Password"),
                            rs.getString("Email")
                    );
                }
            }
        }
        return null;
    }

    public void register(String username, String password, String email) throws SQLException {
        String sql = "INSERT INTO Users (Username, Password, Email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, username);
            p.setString(2, password);
            p.setString(3, email);
            p.executeUpdate();
        }
    }

    public int getUserCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Users";

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }
}
