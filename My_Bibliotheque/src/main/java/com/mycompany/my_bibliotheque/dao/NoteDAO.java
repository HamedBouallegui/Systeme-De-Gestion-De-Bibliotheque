package com.mycompany.my_bibliotheque.dao;

import com.mycompany.my_bibliotheque.models.Note;
import com.mycompany.my_bibliotheque.models.Etudiant;
import com.mycompany.my_bibliotheque.models.Matiere;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {
    private Connection connection;
    private EtudiantDAO etudiantDAO;
    private MatiereDAO matiereDAO;
    
    public NoteDAO() {
        this.connection = DatabaseConnection.getConnection();
        this.etudiantDAO = new EtudiantDAO();
        this.matiereDAO = new MatiereDAO();
    }
    
    public boolean ajouter(Note note) {
        String query = "INSERT INTO notes (etudiant_id, matiere_id, valeur) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, note.getEtudiantId());
            pstmt.setInt(2, note.getMatiereId());
            pstmt.setDouble(3, note.getValeur());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        note.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la note: " + e.getMessage());
        }
        return false;
    }
    
    public boolean modifier(Note note) {
        String query = "UPDATE notes SET etudiant_id = ?, matiere_id = ?, valeur = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, note.getEtudiantId());
            pstmt.setInt(2, note.getMatiereId());
            pstmt.setDouble(3, note.getValeur());
            pstmt.setInt(4, note.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la note: " + e.getMessage());
        }
        return false;
    }
    
    public boolean supprimer(int id) {
        String query = "DELETE FROM notes WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la note: " + e.getMessage());
        }
        return false;
    }
    
    public List<Note> listerNotesParEtudiant(int etudiantId) {
        List<Note> notes = new ArrayList<>();
        String query = "SELECT * FROM notes WHERE etudiant_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, etudiantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notes.add(extraireNoteDuResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des notes par étudiant: " + e.getMessage());
        }
        return notes;
    }
    
    public List<Note> listerNotesParMatiere(int matiereId) {
        List<Note> notes = new ArrayList<>();
        String query = "SELECT * FROM notes WHERE matiere_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, matiereId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notes.add(extraireNoteDuResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des notes par matière: " + e.getMessage());
        }
        return notes;
    }
    
    private Note extraireNoteDuResultSet(ResultSet rs) throws SQLException {
        Note note = new Note();
        note.setId(rs.getInt("id"));
        note.setEtudiantId(rs.getInt("etudiant_id"));
        note.setMatiereId(rs.getInt("matiere_id"));
        note.setValeur(rs.getDouble("valeur"));
        
        // Charger les objets associés
        note.setEtudiant(etudiantDAO.trouverParId(note.getEtudiantId()));
        note.setMatiere(matiereDAO.trouverParId(note.getMatiereId()));
        
        return note;
    }
}