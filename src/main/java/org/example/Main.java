package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.inject.Inject;
import org.example.todoApp.DatabaseInitializer;
import org.example.todoApp.models.Note;
import org.example.todoApp.models.User;
import org.example.todoApp.models.UserNote;
import org.example.todoApp.models.UserNoteCount;
import org.example.todoApp.repositories.Interfaces.INoteRepository;
import org.example.todoApp.repositories.Interfaces.IUserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final Scanner scanner;
    private final IUserRepository userRepository;
    private final INoteRepository noteRepository;
    private final DatabaseInitializer databaseInitializer;

    @Inject
    public Main(IUserRepository userRepository, INoteRepository noteRepository, Scanner scanner, DatabaseInitializer databaseInitializer){
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
        this.scanner = scanner;
        this.databaseInitializer =databaseInitializer;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppContext());
        Main mainApp = injector.getInstance(Main.class);
        mainApp.run();
    }

    private void run() {
        try {
            databaseInitializer.initialize();
        } catch (SQLException e) {
            System.err.println("Veritabanı başlatılamadı: " + e.getMessage());
            return;
        }

        while (true) {
            showMainMenu();
            int mainChoice = getUserChoice();
            switch (mainChoice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> manageOther();
                case 4 -> {
                    System.out.println("Çıkılıyor...");
                    return;
                }
                default -> System.out.println("Geçersiz seçenek!");
            }
        }
    }

    // Main menüsünü göster
    private void showMainMenu() {
        System.out.println("\nAna Menü:");
        System.out.println("1. Giriş Yap");
        System.out.println("2. Kayıt Ol");
        System.out.println("3. Diğer");
        System.out.println("4. Çıkış");
    }

    // Kullanıcıdan seçim almak
    private int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();  
        return choice;
    }

    // Kullanıcı giriş işlemleri
    private void login() {
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
    private void register() {
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
    private void manageOther() {
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
    private void showOtherMenu() {
        System.out.println("\nDiğer Yöntemler Yönetim Menüsü:");
        System.out.println("1. Toplam Kullanıcı Sayısını Göster");
        System.out.println("2. Toplam Note Sayısını Göster");
        System.out.println("3. Tüm Notları KUllanıcıları ile Göster");
        System.out.println("4. Kullanıcılara ait not sayılarını göster");
        System.out.println("5. Çıkış");
    }

    // Toplam Kullanıcı sayısı
    private void showTotalUser(){
        try {
            int result = userRepository.getUserCount();
            System.out.println("Toplam Kullanıcı Sayısı = " + result);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Toplam Not sayısı
    private void showTotalNote(){
        try {
            int result = noteRepository.getNoteCount();
            System.out.println("Toplam Not Sayısı = " + result);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Tüm notları kullanıcıları ile göster
    private void showAllNotesByUser(){
        try {
            List<UserNote> result = noteRepository.getAllUserNotes();
            result.forEach(System.out::println);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Kullanıcılara ait not sayıları
    private void showUserNoteCount(){
        try {
            List<UserNoteCount> result = userRepository.getUsersWithNoteCount();
            result.forEach(System.out::println);
        }catch (SQLException e){
            handleSQLException(e);
        }
    }

    // Not yönetimi işlemleri
    private void manageNotes(User user) {
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
                    showNotesFilterDescription(user);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Geçersiz seçenek!");
            }
        }
    }

    // Not menüsünü göster
    private void showNoteMenu() {
        System.out.println("\nNot Yönetim Menüsü:");
        System.out.println("1. Notları Göster");
        System.out.println("2. Yeni Not Ekle");
        System.out.println("3. Not Sil");
        System.out.println("4. Notları Göster açıklama ile filtrele");
        System.out.println("5. Çıkış");
    }

    // Kullanıcı notlarını göster
    private void showNotes(User user) {
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
    private void addNote(User user) {
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
    private void deleteNote() {
        System.out.println("Silmek istediğiniz notun ID'sini girin: ");
        int noteId = scanner.nextInt();
        scanner.nextLine();

        try {
            noteRepository.deleteNote(noteId);
            System.out.println("Not silindi!");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Kullanıcı notları filtreleyerek  görüntüle
    private void showNotesFilterDescription(User user){
        try {
            System.out.println("Not açıklaması ne içermeli: ");
            String description = scanner.next();
            scanner.nextLine();
            List<Note> notes = noteRepository.getNotesByUser(user.getId(), description);
            if (notes.isEmpty()) {
                System.out.println("Hiç not bulunamadı.");
            } else {
                notes.forEach(System.out::println);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // SQL hatalarını yönet
    private void handleSQLException(SQLException e) {
        System.err.println("Bir hata oluştu: " + e.getMessage());
    }
}
