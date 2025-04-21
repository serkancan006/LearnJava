package org.example.todoApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Note {
    private int id;
    private String notAdi;
    private String notAciklama;
}
