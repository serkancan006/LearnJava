package org.example;

import org.example.todoApp.Note;
import org.example.todoApp.NoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final List<Note> notes = new ArrayList<Note>();
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //System.out.println("Hello and welcome!");
        //Kafka
        //KafkaService kafkaService = new KafkaService();
        //kafkaService.runKafkaApp();

        // To-do App
        NoteRepository repo = new NoteRepository();
        while (true) {
            System.out.println("\n1) Notları Listele\n2) Not Ekle\n3) Not Sil\n4) Çıkış");
            System.out.print("Seçiminiz: ");
            String secim = scanner.nextLine();

            try {
                switch (secim) {
                    case "1":
                        List<Note> notes = repo.getAllNotes();
                        if (notes.isEmpty()) {
                            System.out.println("Hiç not yok.");
                        } else {
                            for (Note note : notes) {
                                System.out.println(note);
                            }
                        }
                        break;
                    case "2":
                        System.out.print("Not Adı: ");
                        String adi = scanner.nextLine();
                        System.out.print("Not Açıklaması: ");
                        String aciklama = scanner.nextLine();
                        repo.addNote(adi, aciklama);
                        System.out.println("Not eklendi.");
                        break;
                    case "3":
                        System.out.print("Silmek istediğiniz not ID'si: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        repo.deleteNote(id);
                        System.out.println("Not silindi.");
                        break;
                    case "4":
                        System.out.println("Çıkılıyor...");
                        return;
                    default:
                        System.out.println("Geçersiz seçim.");
                }
            } catch (Exception e) {
                System.out.println("Hata: " + e.getMessage());
            }
        }
    }
}


