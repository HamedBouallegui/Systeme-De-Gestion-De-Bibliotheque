package com.mycompany.my_bibliotheque.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                initializeDatabase();
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            }
        }
        return connection;
    }
    
    private static void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Création des tables si elles n'existent pas
            String createLivresTable = "CREATE TABLE IF NOT EXISTS livres ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "titre VARCHAR(255) NOT NULL,"
                    + "auteur VARCHAR(255) NOT NULL,"
                    + "isbn VARCHAR(20) UNIQUE,"
                    + "annee_publication INT,"
                    + "categorie VARCHAR(100),"
                    + "disponible BOOLEAN DEFAULT TRUE"
                    + ")";
            
            String createUtilisateursTable = "CREATE TABLE IF NOT EXISTS utilisateurs ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nom VARCHAR(100) NOT NULL,"
                    + "prenom VARCHAR(100) NOT NULL,"
                    + "email VARCHAR(255) UNIQUE,"
                    + "telephone VARCHAR(20),"
                    + "adresse TEXT"
                    + ")";
            
            String createEmpruntsTable = "CREATE TABLE IF NOT EXISTS emprunts ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "livre_id INT NOT NULL,"
                    + "utilisateur_id INT NOT NULL,"
                    + "date_emprunt DATE NOT NULL,"
                    + "date_retour_prevue DATE NOT NULL,"
                    + "date_retour_effective DATE,"
                    + "FOREIGN KEY (livre_id) REFERENCES livres(id),"
                    + "FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)"
                    + ")";
            
            stmt.executeUpdate(createLivresTable);
            stmt.executeUpdate(createUtilisateursTable);
            stmt.executeUpdate(createEmpruntsTable);
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
        }
    }
}