package org.example.todoApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserNote {
    private int id;
    private String notAdi;
    private String notAciklama;
    private String username;
}
