package org.example;

import org.example.todoApp.DatabaseInitializer;
import org.example.todoApp.models.Note;
import org.example.todoApp.models.User;
import org.example.todoApp.repositories.NoteRepository;
import org.example.todoApp.repositories.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserRepository userRepository = new UserRepository();
    private static final NoteRepository noteRepository = new NoteRepository();

    public static void main(String[] args) {
        try {
            DatabaseInitializer.initialize();
        } catch (SQLException e) {
            System.err.println("Veritabanı başlatılamadı: " + e.getMessage());
            return;
        }

        while (true) {
            showMainMenu();
            int mainChoice = getUserChoice();
            switch (mainChoice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Çıkılıyor...");
                    return;
                default:
                    System.out.println("Geçersiz seçenek!");
            }
        }
    }

    // Main menüsünü göster
    private static void showMainMenu() {
        System.out.println("\nAna Menü:");
        System.out.println("1. Giriş Yap");
        System.out.println("2. Kayıt Ol");
        System.out.println("3. Çıkış");
    }

    // Kullanıcıdan seçim almak
    private static int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();  
        return choice;
    }

    // Kullanıcı giriş işlemleri
    private static void login() {
        System.out.println("Kullanıcı Adınızı Girin: ");
        String username = scanner.nextLine();
        System.out.println("Şifrenizi Girin: ");
        String password = scanner.nextLine();

        try {
            User user = userRepository.login(username, password);
            if (user != null) {
                System.out.println("Hoş geldiniz, " + user.getUsername() + "!");
                manageNotes(user);
            } else {
                System.out.println("Hatalı kullanıcı adı veya şifre!");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Kullanıcı kayıt işlemleri
    private static void register() {
        System.out.print("Kullanıcı Adınızı Girin: ");
        String username = scanner.nextLine();
        System.out.print("Şifrenizi Girin: ");
        String password = scanner.nextLine();
        System.out.print("Email Adresinizi Girin: ");
        String email = scanner.nextLine();

        try {
            // ÖNCE: kullanıcı adı var mı diye bak
            if (userRepository.existsByUsername(username)) {
                System.out.println("Bu kullanıcı adı zaten kayıtlı, lütfen başka bir kullanıcı adı seçin.");
                return;
            }
            userRepository.register(username, password, email);
            System.out.println("Kayıt başarıyla tamamlandı!");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Not yönetimi işlemleri
    private static void manageNotes(User user) {
        while (true) {
            showNoteMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    showNotes(user);
                    break;

                case 2:
                    addNote(user);
                    break;

                case 3:
                    deleteNote();
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Geçersiz seçenek!");
            }
        }
    }

    // Not menüsünü göster
    private static void showNoteMenu() {
        System.out.println("\nNot Yönetim Menüsü:");
        System.out.println("1. Notları Göster");
        System.out.println("2. Yeni Not Ekle");
        System.out.println("3. Not Sil");
        System.out.println("4. Çıkış");
    }

    // Kullanıcı notlarını göster
    private static void showNotes(User user) {
        try {
            List<Note> notes = noteRepository.getNotesByUser(user.getId());
            if (notes.isEmpty()) {
                System.out.println("Hiç not bulunamadı.");
            } else {
                notes.forEach(System.out::println);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Yeni not ekle
    private static void addNote(User user) {
        showNotes(user);
        System.out.println("Not Başlığını Girin: ");
        String title = scanner.nextLine();
        System.out.println("Not Açıklamasını Girin: ");
        String description = scanner.nextLine();

        try {
            noteRepository.addNote(user.getId(), title, description);
            System.out.println("Not eklendi!");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Not sil
    private static void deleteNote() {
        System.out.println("Silmek istediğiniz notun ID'sini girin: ");
        int noteId = scanner.nextInt();
        scanner.nextLine();  // buffer temizleme

        try {
            noteRepository.deleteNote(noteId);
            System.out.println("Not silindi!");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // SQL hatalarını yönet
    private static void handleSQLException(SQLException e) {
        System.err.println("Bir hata oluştu: " + e.getMessage());
        e.printStackTrace();  // Hata detaylarını yazdırabilirsiniz
    }
}
