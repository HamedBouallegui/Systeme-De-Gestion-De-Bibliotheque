package com.mycompany.my_bibliotheque.ui;

import com.mycompany.my_bibliotheque.dao.LivreDAO;
import com.mycompany.my_bibliotheque.models.Livre;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GestionLivres extends JFrame {
    
    private LivreDAO livreDAO;
    private JTextField txtTitre, txtAuteur, txtIsbn, txtAnnee, txtCategorie, txtRecherche;
    private JCheckBox chkDisponible;
    private JTable tableLivres;
    private DefaultTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnRechercher, btnRetour;
    
    public GestionLivres() {
        livreDAO = new LivreDAO();
        
        // Configuration de la fenêtre
        setTitle("Gestion des Livres");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(225, 225, 220)); 
        
        // Panneau de formulaire
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.setBackground(new Color(235, 235, 230)); 
        
        JLabel lblTitre = new JLabel("Titre:");
        lblTitre.setForeground(new Color(50, 50, 50));
        formPanel.add(lblTitre);
        txtTitre = new JTextField();
        txtTitre.setForeground(new Color(50, 50, 50));
        formPanel.add(txtTitre);
        
        JLabel lblAuteur = new JLabel("Auteur:");
        lblAuteur.setForeground(new Color(50, 50, 50));
        formPanel.add(lblAuteur);
        txtAuteur = new JTextField();
        txtAuteur.setForeground(new Color(50, 50, 50));
        formPanel.add(txtAuteur);
        
        JLabel lblIsbn = new JLabel("ISBN:");
        lblIsbn.setForeground(new Color(50, 50, 50));
        formPanel.add(lblIsbn);
        txtIsbn = new JTextField();
        txtIsbn.setForeground(new Color(50, 50, 50));
        formPanel.add(txtIsbn);
        
        JLabel lblAnnee = new JLabel("Année de publication:");
        lblAnnee.setForeground(new Color(50, 50, 50));
        formPanel.add(lblAnnee);
        txtAnnee = new JTextField();
        txtAnnee.setForeground(new Color(50, 50, 50));
        formPanel.add(txtAnnee);
        
        JLabel lblCategorie = new JLabel("Catégorie:");
        lblCategorie.setForeground(new Color(50, 50, 50));
        formPanel.add(lblCategorie);
        txtCategorie = new JTextField();
        txtCategorie.setForeground(new Color(50, 50, 50));
        formPanel.add(txtCategorie);
        
        JLabel lblDisponible = new JLabel("Disponible:");
        lblDisponible.setForeground(new Color(50, 50, 50));
        formPanel.add(lblDisponible);
        chkDisponible = new JCheckBox();
        chkDisponible.setBackground(new Color(235, 235, 230)); // Match form background
        chkDisponible.setSelected(true);
        formPanel.add(chkDisponible);
        
        // Panneau de boutons pour le formulaire
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formButtonPanel.setBackground(new Color(235, 235, 230));
        
        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(new Color(200, 200, 200));
        btnAjouter.setForeground(Color.BLACK);
        btnAjouter.setBorder(BorderFactory.createRaisedBevelBorder());
        
        btnModifier = new JButton("Modifier");
        btnModifier.setBackground(new Color(200, 200, 200));
        btnModifier.setForeground(Color.BLACK);
        btnModifier.setBorder(BorderFactory.createRaisedBevelBorder());
        
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
        
        // Tableau des livres
        tableModel = new DefaultTableModel(new Object[]{"ID", "Titre", "Auteur", "ISBN", "Année", "Catégorie", "Disponible"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non éditable
            }
        };
        tableLivres = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableLivres);
        
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
        chargerLivres();
        
        // Ajout des écouteurs d'événements
        ajouterEcouteurs();
    }
    
    private void chargerLivres() {
        // Effacer le tableau
        tableModel.setRowCount(0);
        
        // Charger tous les livres
        List<Livre> livres = livreDAO.listerTous();
        for (Livre livre : livres) {
            tableModel.addRow(new Object[]{
                livre.getId(),
                livre.getTitre(),
                livre.getAuteur(),
                livre.getIsbn(),
                livre.getAnneePublication(),
                livre.getCategorie(),
                livre.isDisponible() ? "Oui" : "Non"
            });
        }
    }
    
    private void ajouterEcouteurs() {
        // Écouteur pour le bouton Ajouter
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterLivre();
            }
        });
        
        // Écouteur pour le bouton Modifier
        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifierLivre();
            }
        });
        
        // Écouteur pour le bouton Supprimer
        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerLivre();
            }
        });
        
        // Écouteur pour le bouton Rechercher
        btnRechercher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rechercherLivres();
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
        tableLivres.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableLivres.getSelectedRow() != -1) {
                afficherLivreSelectionne();
            }
        });
    }
    
    private void ajouterLivre() {
        try {
            Livre livre = new Livre();
            livre.setTitre(txtTitre.getText());
            livre.setAuteur(txtAuteur.getText());
            livre.setIsbn(txtIsbn.getText());
            livre.setAnneePublication(Integer.parseInt(txtAnnee.getText()));
            livre.setCategorie(txtCategorie.getText());
            livre.setDisponible(chkDisponible.isSelected());
            
            if (livreDAO.ajouter(livre)) {
                JOptionPane.showMessageDialog(this, "Livre ajouté avec succès");
                viderFormulaire();
                chargerLivres();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du livre", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une année valide", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void modifierLivre() {
        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre à modifier", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Livre livre = new Livre();
            livre.setId(id);
            livre.setTitre(txtTitre.getText());
            livre.setAuteur(txtAuteur.getText());
            livre.setIsbn(txtIsbn.getText());
            livre.setAnneePublication(Integer.parseInt(txtAnnee.getText()));
            livre.setCategorie(txtCategorie.getText());
            livre.setDisponible(chkDisponible.isSelected());
            
            if (livreDAO.modifier(livre)) {
                JOptionPane.showMessageDialog(this, "Livre modifié avec succès");
                viderFormulaire();
                chargerLivres();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du livre", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une année valide", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void supprimerLivre() {
        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre à supprimer", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce livre ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            if (livreDAO.supprimer(id)) {
                JOptionPane.showMessageDialog(this, "Livre supprimé avec succès");
                viderFormulaire();
                chargerLivres();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du livre", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void rechercherLivres() {
        String terme = txtRecherche.getText().trim();
        if (terme.isEmpty()) {
            chargerLivres(); // Si vide, charger tous les livres
            return;
        }
        
        // Effacer le tableau
        tableModel.setRowCount(0);
        
        // Rechercher les livres
        List<Livre> livres = livreDAO.rechercher(terme);
        for (Livre livre : livres) {
            tableModel.addRow(new Object[]{
                livre.getId(),
                livre.getTitre(),
                livre.getAuteur(),
                livre.getIsbn(),
                livre.getAnneePublication(),
                livre.getCategorie(),
                livre.isDisponible() ? "Oui" : "Non"
            });
        }
    }
    
    private void afficherLivreSelectionne() {
        int selectedRow = tableLivres.getSelectedRow();
        txtTitre.setText((String) tableModel.getValueAt(selectedRow, 1));
        txtAuteur.setText((String) tableModel.getValueAt(selectedRow, 2));
        txtIsbn.setText((String) tableModel.getValueAt(selectedRow, 3));
        txtAnnee.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtCategorie.setText((String) tableModel.getValueAt(selectedRow, 5));
        chkDisponible.setSelected(tableModel.getValueAt(selectedRow, 6).equals("Oui"));
    }
    
    private void viderFormulaire() {
        txtTitre.setText("");
        txtAuteur.setText("");
        txtIsbn.setText("");
        txtAnnee.setText("");
        txtCategorie.setText("");
        chkDisponible.setSelected(true);
        tableLivres.clearSelection();
    }
}