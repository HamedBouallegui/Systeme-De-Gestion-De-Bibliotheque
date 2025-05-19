package com.mycompany.my_bibliotheque.dao;

import com.mycompany.my_bibliotheque.models.Etudiant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDAO {
    private Connection connection;
    
    public EtudiantDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    public boolean ajouter(Etudiant etudiant) {
        String query = "INSERT INTO etudiants (nom, prenom, numero_etudiant, classe) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setString(3, etudiant.getNumeroEtudiant());
            pstmt.setString(4, etudiant.getClasse());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        etudiant.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'étudiant: " + e.getMessage());
        }
        return false;
    }
    
    public boolean modifier(Etudiant etudiant) {
        String query = "UPDATE etudiants SET nom = ?, prenom = ?, numero_etudiant = ?, classe = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setString(3, etudiant.getNumeroEtudiant());
            pstmt.setString(4, etudiant.getClasse());
            pstmt.setInt(5, etudiant.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'étudiant: " + e.getMessage());
        }
        return false;
    }
    
    public boolean supprimer(int id) {
        String query = "DELETE FROM etudiants WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'étudiant: " + e.getMessage());
        }
        return false;
    }
    
    public Etudiant trouverParId(int id) {
        String query = "SELECT * FROM etudiants WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extraireEtudiantDuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'étudiant par ID: " + e.getMessage());
        }
        return null;
    }
    
    public List<Etudiant> rechercher(String terme) {
        List<Etudiant> etudiants = new ArrayList<>();
        String query = "SELECT * FROM etudiants WHERE nom LIKE ? OR prenom LIKE ? OR numero_etudiant LIKE ? OR classe LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String searchTerm = "%" + terme + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            pstmt.setString(4, searchTerm);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    etudiants.add(extraireEtudiantDuResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche d'étudiants: " + e.getMessage());
        }
        return etudiants;
    }
    
    public List<Etudiant> listerTous() {
        List<Etudiant> etudiants = new ArrayList<>();
        String query = "SELECT * FROM etudiants";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                etudiants.add(extraireEtudiantDuResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les étudiants: " + e.getMessage());
        }
        return etudiants;
    }
    
    private Etudiant extraireEtudiantDuResultSet(ResultSet rs) throws SQLException {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(rs.getInt("id"));
        etudiant.setNom(rs.getString("nom"));
        etudiant.setPrenom(rs.getString("prenom"));
        etudiant.setNumeroEtudiant(rs.getString("numero_etudiant"));
        etudiant.setClasse(rs.getString("classe"));
        return etudiant;
    }
}