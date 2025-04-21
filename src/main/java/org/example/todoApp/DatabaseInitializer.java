package org.example.todoApp;

import java.sql.*;

public class DatabaseInitializer {

    public static void initialize() throws SQLException {
        try (Connection conn = DatabaseContext.getConnection()) {
            // İlk olarak Users tablosunu kontrol et ve oluştur
            if (!tableExists(conn, "Users")) {
                try (Statement stmt = conn.createStatement()) {
                    String createUsers = """
                        CREATE TABLE Users (
                            Id INT IDENTITY(1,1) PRIMARY KEY,
                            Username NVARCHAR(100) NOT NULL UNIQUE,
                            Password NVARCHAR(100) NOT NULL,
                            Email NVARCHAR(255) NOT NULL UNIQUE
                        );
                    """;
                    stmt.execute(createUsers);
                    System.out.println("✅ Users tablosu oluşturuldu.");
                }
            } else {
                System.out.println("ℹ️ Users tablosu zaten mevcut.");
            }

            // Sonra Notes tablosunu kontrol et ve oluştur
            if (!tableExists(conn, "Notes")) {
                try (Statement stmt = conn.createStatement()) {
                    String createNotes = """
                        CREATE TABLE Notes (
                            Id INT IDENTITY(1,1) PRIMARY KEY,
                            NotAdi NVARCHAR(255),
                            NotAciklama NVARCHAR(MAX),
                            UserId INT NOT NULL,
                            CONSTRAINT FK_Notes_Users
                              FOREIGN KEY(UserId) REFERENCES Users(Id)
                              ON DELETE CASCADE
                        );
                    """;
                    stmt.execute(createNotes);
                    System.out.println("✅ Notes tablosu oluşturuldu.");
                }
            } else {
                System.out.println("ℹ️ Notes tablosu zaten mevcut.");
            }
        }
    }
    // tablo var mı yok mu kontrol et
    private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }
}
