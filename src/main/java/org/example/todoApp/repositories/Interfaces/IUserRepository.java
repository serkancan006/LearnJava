package org.example.todoApp.repositories.Interfaces;

import org.example.todoApp.models.User;
import org.example.todoApp.models.UserNoteCount;

import java.sql.SQLException;
import java.util.List;

public interface IUserRepository {
    boolean existsByUsername(String username) throws SQLException;

    User login(String username, String password) throws SQLException;

    void register(String username, String password, String email) throws SQLException;

    int getUserCount() throws SQLException;

    List<UserNoteCount> getUsersWithNoteCount() throws SQLException;
}
