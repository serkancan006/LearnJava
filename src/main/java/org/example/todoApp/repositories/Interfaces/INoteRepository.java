package org.example.todoApp.repositories.Interfaces;

import org.example.todoApp.models.Note;
import org.example.todoApp.models.UserNote;

import java.sql.SQLException;
import java.util.List;

public interface INoteRepository {

    // Kullanıcının notlarını al
    List<Note> getNotesByUser(int userId) throws SQLException;

    List<Note> getNotesByUser(int userId, String notAciklama) throws SQLException;

    List<UserNote> getAllUserNotes() throws SQLException;

    // Not ekle
    void addNote(int userId, String title, String description) throws SQLException;

    // Not sil
    void deleteNote(int id) throws SQLException;

    int getNoteCount() throws SQLException;
}
