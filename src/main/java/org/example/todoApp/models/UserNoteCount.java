package org.example.todoApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserNoteCount {
    private String username;
    private int noteCount;
}
