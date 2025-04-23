package org.example.todoApp.repositories;

import org.example.todoApp.DatabaseContext;
import org.example.todoApp.models.User;
import org.example.todoApp.models.UserNoteCount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

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

    public List<UserNoteCount> getUsersWithNoteCount() throws SQLException {
        List<UserNoteCount> result = new ArrayList<>();
        // Left join çünkü notu olmayan kullanıcılarda olabilirdi.
        String sql = """
            SELECT u.Username, COUNT(n.Id) AS NoteCount
            FROM Users u
            LEFT JOIN Notes n ON u.Id = n.UserId
            GROUP BY u.Id, u.Username
            ORDER BY u.Username
            """;

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement p = conn.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            while (rs.next()) {
                result.add(new UserNoteCount(
                        rs.getString("Username"),
                        rs.getInt("NoteCount")
                ));
            }
        }

        return result;
    }
}
