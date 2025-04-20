package org.example.todoApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteRepository {
    public List<Note> getAllNotes() throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM Notes";

        try (Connection conn = DatabaseContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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

    public void addNote(String title, String description) throws SQLException {
        String sql = "INSERT INTO Notes (NotAdi, NotAciklama) VALUES (?, ?)";

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
        }
    }

    public void deleteNote(int id) throws SQLException {
        String sql = "DELETE FROM Notes WHERE Id = ?";

        try (Connection conn = DatabaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
