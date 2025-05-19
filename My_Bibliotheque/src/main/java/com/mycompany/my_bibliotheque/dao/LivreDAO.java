package com.mycompany.my_bibliotheque.dao;

import com.mycompany.my_bibliotheque.models.Livre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {
    private Connection connection;
    
    public LivreDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    public boolean ajouter(Livre livre) {
        String query = "INSERT INTO livres (titre, auteur, isbn, annee_publication, categorie, disponible) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getIsbn());
            pstmt.setInt(4, livre.getAnneePublication());
            pstmt.setString(5, livre.getCategorie());
            pstmt.setBoolean(6, livre.isDisponible());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        livre.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du livre: " + e.getMessage());
        }
        return false;
    }
    
    public boolean modifier(Livre livre) {
        String query = "UPDATE livres SET titre = ?, auteur = ?, isbn = ?, annee_publication = ?, categorie = ?, disponible = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getIsbn());
            pstmt.setInt(4, livre.getAnneePublication());
            pstmt.setString(5, livre.getCategorie());
            pstmt.setBoolean(6, livre.isDisponible());
            pstmt.setInt(7, livre.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du livre: " + e.getMessage());
        }
        return false;
    }
    
    public boolean mettreAJourDisponibilite(int livreId, boolean disponible) {
        String query = "UPDATE livres SET disponible = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setBoolean(1, disponible);
            pstmt.setInt(2, livreId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la disponibilité du livre: " + e.getMessage());
        }
        return false;
    }
    
    public Livre trouverParId(int id) {
        String query = "SELECT * FROM livres WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extraireLivreDuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du livre par ID: " + e.getMessage());
        }
        return null;
    }
    
    public List<Livre> rechercher(String terme) {
        List<Livre> livres = new ArrayList<>();
        String query = "SELECT * FROM livres WHERE titre LIKE ? OR auteur LIKE ? OR isbn LIKE ? OR categorie LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String searchTerm = "%" + terme + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            pstmt.setString(4, searchTerm);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(extraireLivreDuResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de livres: " + e.getMessage());
        }
        return livres;
    }
    
    public List<Livre> listerTous() {
        List<Livre> livres = new ArrayList<>();
        String query = "SELECT * FROM livres";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                livres.add(extraireLivreDuResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les livres: " + e.getMessage());
        }
        return livres;
    }
    
    private Livre extraireLivreDuResultSet(ResultSet rs) throws SQLException {
        Livre livre = new Livre();
        livre.setId(rs.getInt("id"));
        livre.setTitre(rs.getString("titre"));
        livre.setAuteur(rs.getString("auteur"));
        livre.setIsbn(rs.getString("isbn"));
        livre.setAnneePublication(rs.getInt("annee_publication"));
        livre.setCategorie(rs.getString("categorie"));
        livre.setDisponible(rs.getBoolean("disponible"));
        return livre;
    }

    public boolean supprimer(int id) {
        String query = "DELETE FROM livres WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du livre: " + e.getMessage());
        }
        return false;
    }
}