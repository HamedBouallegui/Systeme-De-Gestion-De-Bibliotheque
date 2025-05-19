package com.mycompany.my_bibliotheque.ui;

import com.mycompany.my_bibliotheque.dao.UtilisateurDAO;
import com.mycompany.my_bibliotheque.models.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GestionUtilisateurs extends JFrame {
    
    private UtilisateurDAO utilisateurDAO;
    private JTextField txtNom, txtPrenom, txtEmail, txtTelephone, txtAdresse, txtRecherche;
    private JTable tableUtilisateurs;
    private DefaultTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnRechercher, btnRetour;
    
    public GestionUtilisateurs() {
        utilisateurDAO = new UtilisateurDAO();
        
        // Configuration de la fenêtre
        setTitle("Gestion des Utilisateurs");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(225, 225, 220)); // Couleur beige clair traditionnel
        
        // Panneau de formulaire
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.setBackground(new Color(235, 235, 230)); // Beige plus clair pour le formulaire
        
        JLabel lblNom = new JLabel("Nom:");
        lblNom.setForeground(new Color(50, 50, 50));
        formPanel.add(lblNom);
        txtNom = new JTextField();
        txtNom.setForeground(new Color(50, 50, 50));
        formPanel.add(txtNom);
        
        JLabel lblPrenom = new JLabel("Prénom:");
        lblPrenom.setForeground(new Color(50, 50, 50));
        formPanel.add(lblPrenom);
        txtPrenom = new JTextField();
        txtPrenom.setForeground(new Color(50, 50, 50));
        formPanel.add(txtPrenom);
        
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(new Color(50, 50, 50));
        formPanel.add(lblEmail);
        txtEmail = new JTextField();
        txtEmail.setForeground(new Color(50, 50, 50));
        formPanel.add(txtEmail);
        
        JLabel lblTelephone = new JLabel("Téléphone:");
        lblTelephone.setForeground(new Color(50, 50, 50));
        formPanel.add(lblTelephone);
        txtTelephone = new JTextField();
        txtTelephone.setForeground(new Color(50, 50, 50));
        formPanel.add(txtTelephone);
        
        JLabel lblAdresse = new JLabel("Adresse:");
        lblAdresse.setForeground(new Color(50, 50, 50));
        formPanel.add(lblAdresse);
        txtAdresse = new JTextField();
        txtAdresse.setForeground(new Color(50, 50, 50));
        formPanel.add(txtAdresse);
        
        // Panneau de boutons pour le formulaire
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formButtonPanel.setBackground(new Color(235, 235, 230));
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        
        formButtonPanel.add(btnAjouter);
        formButtonPanel.add(btnModifier);
        formButtonPanel.add(btnSupprimer);
        
        // Panneau de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(235, 235, 230));
        JLabel lblRechercher = new JLabel("Rechercher:");
        lblRechercher.setForeground(new Color(50, 50, 50));
        searchPanel.add(lblRechercher);
        txtRecherche = new JTextField(20);
        txtRecherche.setForeground(new Color(50, 50, 50));
        searchPanel.add(txtRecherche);
        btnRechercher = new JButton("Rechercher");
        btnRechercher.setBackground(new Color(200, 200, 200));
        btnRechercher.setForeground(Color.BLACK);
        btnRechercher.setBorder(BorderFactory.createRaisedBevelBorder());
        searchPanel.add(btnRechercher);
        
        // Tableau des utilisateurs
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénom", "Email", "Téléphone", "Adresse"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non éditable
            }
        };
        tableUtilisateurs = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableUtilisateurs);
        
        // Bouton de retour
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRetour = new JButton("Retour au menu principal");
        bottomPanel.add(btnRetour);
        
        // Ajout des composants au panneau principal
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(formButtonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Ajout du panneau principal à la fenêtre
        add(mainPanel);
        
        // Chargement initial des données
        chargerUtilisateurs();
        
        // Ajout des écouteurs d'événements
        ajouterEcouteurs();
    }
    
    private void chargerUtilisateurs() {
        // Effacer le tableau
        tableModel.setRowCount(0);
        
        // Charger tous les utilisateurs
        List<Utilisateur> utilisateurs = utilisateurDAO.listerTous();
        for (Utilisateur utilisateur : utilisateurs) {
            tableModel.addRow(new Object[]{
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getTelephone(),
                utilisateur.getAdresse()
            });
        }
    }
    
    private void ajouterEcouteurs() {
        // Écouteur pour le bouton Ajouter
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterUtilisateur();
            }
        });
        
        // Écouteur pour le bouton Modifier
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierUtilisateur();
            }
        });
        
        // Écouteur pour le bouton Supprimer
        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerUtilisateur();
            }
        });
        
        // Écouteur pour le bouton Rechercher
        btnRechercher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherUtilisateurs();
            }
        });
        
        // Écouteur pour le bouton Retour
        btnRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fermer cette fenêtre
            }
        });
        
        // Écouteur pour la sélection dans le tableau
        tableUtilisateurs.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableUtilisateurs.getSelectedRow() != -1) {
                afficherUtilisateurSelectionne();
            }
        });
    }
    
    private void ajouterUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(txtNom.getText());
        utilisateur.setPrenom(txtPrenom.getText());
        utilisateur.setEmail(txtEmail.getText());
        utilisateur.setTelephone(txtTelephone.getText());
        utilisateur.setAdresse(txtAdresse.getText());
        
        if (utilisateurDAO.ajouter(utilisateur)) {
            JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès");
            viderFormulaire();
            chargerUtilisateurs();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'utilisateur", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void modifierUtilisateur() {
        int selectedRow = tableUtilisateurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à modifier", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        utilisateur.setNom(txtNom.getText());
        utilisateur.setPrenom(txtPrenom.getText());
        utilisateur.setEmail(txtEmail.getText());
        utilisateur.setTelephone(txtTelephone.getText());
        utilisateur.setAdresse(txtAdresse.getText());
        
        if (utilisateurDAO.modifier(utilisateur)) {
            JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès");
            viderFormulaire();
            chargerUtilisateurs();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'utilisateur", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void supprimerUtilisateur() {
        int selectedRow = tableUtilisateurs.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à supprimer", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet utilisateur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            if (utilisateurDAO.supprimer(id)) {
                JOptionPane.showMessageDialog(this, "Utilisateur supprimé avec succès");
                viderFormulaire();
                chargerUtilisateurs();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'utilisateur", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void rechercherUtilisateurs() {
        String terme = txtRecherche.getText().trim();
        if (terme.isEmpty()) {
            chargerUtilisateurs(); // Si vide, charger tous les utilisateurs
            return;
        }
        
        // Effacer le tableau
        tableModel.setRowCount(0);
        
        // Rechercher les utilisateurs
        List<Utilisateur> utilisateurs = utilisateurDAO.rechercher(terme);
        for (Utilisateur utilisateur : utilisateurs) {
            tableModel.addRow(new Object[]{
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getTelephone(),
                utilisateur.getAdresse()
            });
        }
    }
    
    private void afficherUtilisateurSelectionne() {
        int selectedRow = tableUtilisateurs.getSelectedRow();
        txtNom.setText((String) tableModel.getValueAt(selectedRow, 1));
        txtPrenom.setText((String) tableModel.getValueAt(selectedRow, 2));
        txtEmail.setText((String) tableModel.getValueAt(selectedRow, 3));
        txtTelephone.setText((String) tableModel.getValueAt(selectedRow, 4));
        txtAdresse.setText((String) tableModel.getValueAt(selectedRow, 5));
    }
    
    private void viderFormulaire() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        txtTelephone.setText("");
        txtAdresse.setText("");
        tableUtilisateurs.clearSelection();
    }
}