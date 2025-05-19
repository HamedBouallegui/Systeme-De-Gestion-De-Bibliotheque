package com.mycompany.my_bibliotheque.dao;

import com.mycompany.my_bibliotheque.models.Emprunt;
import com.mycompany.my_bibliotheque.models.Livre;
import com.mycompany.my_bibliotheque.models.Utilisateur;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {
    private Connection connection;
    private LivreDAO livreDAO;
    private UtilisateurDAO utilisateurDAO;
    
    public EmpruntDAO() {
        this.connection = DatabaseConnection.getConnection();
        this.livreDAO = new LivreDAO();
        this.utilisateurDAO = new UtilisateurDAO();
    }
    
    public boolean ajouter(Emprunt emprunt) {
        String query = "INSERT INTO emprunts (livre_id, utilisateur_id, date_emprunt, date_retour_prevue) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, emprunt.getLivreId());
            pstmt.setInt(2, emprunt.getUtilisateurId());
            pstmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            pstmt.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        emprunt.setId(rs.getInt(1));
                    }
                }
                // Mettre à jour la disponibilité du livre
                livreDAO.mettreAJourDisponibilite(emprunt.getLivreId(), false);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'emprunt: " + e.getMessage());
        }
        return false;
    }
    
    public boolean marquerCommeRetourne(int empruntId) {
        String query = "UPDATE emprunts SET date_retour_effective = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, empruntId);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Récupérer l'emprunt pour obtenir l'ID du livre
                Emprunt emprunt = trouverParId(empruntId);
                if (emprunt != null) {
                    // Mettre à jour la disponibilité du livre
                    livreDAO.mettreAJourDisponibilite(emprunt.getLivreId(), true);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du marquage de l'emprunt comme retourné: " + e.getMessage());
        }
        return false;
    }
    
    public Emprunt trouverParId(int id) {
        String query = "SELECT * FROM emprunts WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extraireEmpruntDuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'emprunt par ID: " + e.getMessage());
        }
        return null;
    }
    
    public List<Emprunt> listerEmpruntsEnCours() {
        List<Emprunt> emprunts = new ArrayList<>();
        String query = "SELECT * FROM emprunts WHERE date_retour_effective IS NULL";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                emprunts.add(extraireEmpruntDuResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des emprunts en cours: " + e.getMessage());
        }
        return emprunts;
    }
    
    private Emprunt extraireEmpruntDuResultSet(ResultSet rs) throws SQLException {
        Emprunt emprunt = new Emprunt();
        emprunt.setId(rs.getInt("id"));
        emprunt.setLivreId(rs.getInt("livre_id"));
        emprunt.setUtilisateurId(rs.getInt("utilisateur_id"));
        emprunt.setDateEmprunt(rs.getDate("date_emprunt").toLocalDate());
        emprunt.setDateRetourPrevue(rs.getDate("date_retour_prevue").toLocalDate());
        
        Date dateRetourEffective = rs.getDate("date_retour_effective");
        if (dateRetourEffective != null) {
            emprunt.setDateRetourEffective(dateRetourEffective.toLocalDate());
        }
        
        // Charger les objets associés
        emprunt.setLivre(livreDAO.trouverParId(emprunt.getLivreId()));
        emprunt.setUtilisateur(utilisateurDAO.trouverParId(emprunt.getUtilisateurId()));
        
        return emprunt;
    }
}