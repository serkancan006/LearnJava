package org.example.todoApp.repositories;

import com.google.inject.Inject;
import org.example.anatations.Repository;
import org.example.todoApp.DatabaseContext;
import org.example.todoApp.models.Note;
import org.example.todoApp.models.UserNote;
import org.example.todoApp.repositories.Interfaces.INoteRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NoteRepository implements INoteRepository {
    private final DatabaseContext databaseContext;

    @Inject
    public NoteRepository(DatabaseContext databaseContext) {
        this.databaseContext = databaseContext;
    }
    // Kullanıcının notlarını al
    @Override
    public List<Note> getNotesByUser(int userId) throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM Notes WHERE UserId = ?";  // Kullanıcı ID'sine göre notları getir

        try (Connection conn = databaseContext.getConnection();
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
    @Override
    public List<Note> getNotesByUser(int userId, String notAciklama) throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM Notes WHERE UserId = ?";

        if (notAciklama != null) {
            // LIKE komutuyla ve case-insensitive (büyük/küçük harf duyarsız) yapmak için LOWER kullanımı
            sql += " AND LOWER(NotAciklama) LIKE LOWER(?)";
        }

        try (Connection conn = databaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);  // Kullanıcı ID'sini sorguya ekle

            if (notAciklama != null) {
                // LIKE için parametreyi % işaretiyle ekle
                pstmt.setString(2, "%" + notAciklama + "%");  // % ile kısmi eşleşme yap
            }

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

    @Override
    public List<UserNote> getAllUserNotes() throws SQLException {
        List<UserNote> userNotes = new ArrayList<>();

        String sql = "SELECT n.id, n.notAdi, n.notAciklama, u.username " +
                "FROM Notes n " +
                "JOIN Users u ON n.userId = u.id";

        try (Connection conn = databaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                userNotes.add(new UserNote(
                        rs.getInt("Id"),
                        rs.getString("NotAdi"),
                        rs.getString("NotAciklama"),
                        rs.getString("username")
                ));
            }
        }

        return userNotes;
    }


    // Not ekle
    @Override
    public void addNote(int userId, String title, String description) throws SQLException {
        String sql = "INSERT INTO Notes (NotAdi, NotAciklama, UserId) VALUES (?, ?, ?)";

        try (Connection conn = databaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
        }
    }

    // Not sil
    @Override
    public void deleteNote(int id) throws SQLException {
        String sql = "DELETE FROM Notes WHERE Id = ?";

        try (Connection conn = databaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    @Override
    public int getNoteCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Notes";

        try (Connection conn = databaseContext.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }


}
