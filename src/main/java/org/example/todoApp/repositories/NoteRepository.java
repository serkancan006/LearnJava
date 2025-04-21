package org.example.todoApp.repositories;

import org.example.todoApp.DatabaseContext;
import org.example.todoApp.models.Note;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteRepository {

    // Kullanıcının notlarını al
    public List<Note> getNotesByUser(int userId) throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM Notes WHERE UserId = ?";  // Kullanıcı ID'sine göre notları getir

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);  // Kullanıcı ID'sini sorguya ekle
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                notes.add(new Note(
                        rs.getInt("Id"),
                        rs.getString("NotAdi"),
                        rs.getString("NotAciklama")
                ));
            }
        }
        return notes;
    }


    // Not ekle
    public void addNote(int userId, String title, String description) throws SQLException {
        String sql = "INSERT INTO Notes (NotAdi, NotAciklama, UserId) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
        }
    }

    // Not sil
    public void deleteNote(int id) throws SQLException {
        String sql = "DELETE FROM Notes WHERE Id = ?";

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
