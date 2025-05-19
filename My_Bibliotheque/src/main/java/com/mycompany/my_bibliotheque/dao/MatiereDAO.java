package com.mycompany.my_bibliotheque.dao;

import com.mycompany.my_bibliotheque.models.Matiere;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatiereDAO {
    private Connection connection;
    
    public MatiereDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    public boolean ajouter(Matiere matiere) {
        String query = "INSERT INTO matieres (nom, coefficient) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, matiere.getNom());
            pstmt.setDouble(2, matiere.getCoefficient());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        matiere.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la matière: " + e.getMessage());
        }
        return false;
    }
    
    public boolean modifier(Matiere matiere) {
        String query = "UPDATE matieres SET nom = ?, coefficient = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, matiere.getNom());
            pstmt.setDouble(2, matiere.getCoefficient());
            pstmt.setInt(3, matiere.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la matière: " + e.getMessage());
        }
        return false;
    }
    
    public boolean supprimer(int id) {
        String query = "DELETE FROM matieres WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la matière: " + e.getMessage());
        }
        return false;
    }
    
    public Matiere trouverParId(int id) {
        String query = "SELECT * FROM matieres WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extraireMatiereDuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la matière par ID: " + e.getMessage());
        }
        return null;
    }
    
    public List<Matiere> listerToutes() {
        List<Matiere> matieres = new ArrayList<>();
        String query = "SELECT * FROM matieres";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                matieres.add(extraireMatiereDuResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de toutes les matières: " + e.getMessage());
        }
        return matieres;
    }
    
    private Matiere extraireMatiereDuResultSet(ResultSet rs) throws SQLException {
        Matiere matiere = new Matiere();
        matiere.setId(rs.getInt("id"));
        matiere.setNom(rs.getString("nom"));
        matiere.setCoefficient(rs.getDouble("coefficient"));
        return matiere;
    }
}