package org.example.demo.Modele;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    private static final String URL = "jdbc:sqlite:test.db"; // URL de la base de données

    public static void initDB() {
        // Initialise la base de données et crée la table si elle n'existe pas
        String sql = "CREATE TABLE IF NOT EXISTS joueur (" +
                "name TEXT, " +
                "time INTEGER);";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Table 'joueur' vérifiée/créée avec succès.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la création de la table: " + e.getMessage());
        }
    }

    public static void saveUser(User user) {
        // Enregistre un utilisateur dans la base de données
        String sql = "INSERT INTO joueur(name, time) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            System.out.println("➡️ Insertion en cours...");
            System.out.println("Nom: " + user.getName());
            System.out.println("Temps: " + user.getTime());

            pstmt.setString(1, user.getName());
            pstmt.setInt(2, user.getTime());
            pstmt.executeUpdate();

            System.out.println("✅ Utilisateur enregistré dans la base de données.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'insertion: " + e.getMessage());
        }
    }
}