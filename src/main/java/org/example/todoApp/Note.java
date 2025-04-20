package org.example.todoApp;

public class Note {
    private int id;
    private String notAdi;
    private String notAciklama;

    public Note(int id, String notAdi, String notAciklama) {
        this.id = id;
        this.notAdi = notAdi;
        this.notAciklama = notAciklama;
    }

    public int getId() { return id; }
    public String getNotAdi() { return notAdi; }
    public String getNotAciklama() { return notAciklama; }

    @Override
    public String toString() {
        return id + " - " + notAdi + " : " + notAciklama;
    }
}
