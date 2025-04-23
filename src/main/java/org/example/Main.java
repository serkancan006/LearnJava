package org.example;

import org.example.todoApp.DatabaseInitializer;
import org.example.todoApp.models.Note;
import org.example.todoApp.models.User;
import org.example.todoApp.models.UserNote;
import org.example.todoApp.models.UserNoteCount;
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
                    manageOther();
                    break;
                case 4:
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
        System.out.println("3. Diğer");
        System.out.println("4. Çıkış");
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

    // Manage Other
    private static void manageOther() {
        while (true) {
            showOtherMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    showTotalUser();
                    break;
                case 2:
                    showTotalNote();
                    break;
                case 3:
                    showAllNotesByUser();
                    break;
                case 4:
                    showUserNoteCount();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Geçersiz seçenek!");
            }
        }
    }

    // OTher menüsünü göster
    private static void showOtherMenu() {
        System.out.println("\nDiğer Yöntemler Yönetim Menüsü:");
        System.out.println("1. Toplam Kullanıcı Sayısını Göster");
        System.out.println("2. Toplam Note Sayısını Göster");
        System.out.println("3. Tüm Notları KUllanıcıları ile Göster");
        System.out.println("4. Kullanıcılara ait not sayılarını göster");
        System.out.println("5. Çıkış");
    }

    // Toplam Kullanıcı sayısı
    private static void showTotalUser(){
        try {
            int result = userRepository.getUserCount();
            System.out.println("Toplam Kullanıcı Sayısı = " + result);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Toplam Not sayısı
    private static void showTotalNote(){
        try {
            int result = noteRepository.getNoteCount();
            System.out.println("Toplam Not Sayısı = " + result);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Tüm notları kullanıcıları ile göster
    private static void showAllNotesByUser(){
        try {
            List<UserNote> result = noteRepository.getAllUserNotes();
            result.forEach(System.out::println);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Kullanıcılara ait not sayıları
    private static void showUserNoteCount(){
        try {
            List<UserNoteCount> result = userRepository.getUsersWithNoteCount();
            result.forEach(System.out::println);
        }catch (SQLException e){
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
    }
}
