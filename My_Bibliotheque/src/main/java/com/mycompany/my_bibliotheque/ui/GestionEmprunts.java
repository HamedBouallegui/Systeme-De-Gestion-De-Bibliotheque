package com.mycompany.my_bibliotheque.ui;

import com.mycompany.my_bibliotheque.dao.EmpruntDAO;
import com.mycompany.my_bibliotheque.dao.LivreDAO;
import com.mycompany.my_bibliotheque.dao.UtilisateurDAO;
import com.mycompany.my_bibliotheque.models.Emprunt;
import com.mycompany.my_bibliotheque.models.Livre;
import com.mycompany.my_bibliotheque.models.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GestionEmprunts extends JFrame {
    
    private EmpruntDAO empruntDAO;
    private LivreDAO livreDAO;
    private UtilisateurDAO utilisateurDAO;
    private JComboBox<String> cmbLivres;
    private JComboBox<String> cmbUtilisateurs;
    private JTextField txtDateEmprunt, txtDateRetourPrevue;
    private JTable tableEmprunts;
    private DefaultTableModel tableModel;
    private JButton btnEnregistrer, btnMarquerRetourne, btnRechercher, btnRetour;
    private JCheckBox chkAfficherSeulementEnCours;
    
    // Map pour stocker les ID des livres et utilisateurs
    private java.util.Map<Integer, Integer> indexToLivreId = new java.util.HashMap<>();
    private java.util.Map<Integer, Integer> indexToUtilisateurId = new java.util.HashMap<>();
    
    public GestionEmprunts() {
        empruntDAO = new EmpruntDAO();
        livreDAO = new LivreDAO();
        utilisateurDAO = new UtilisateurDAO();
        
        // Configuration de la fenêtre
        setTitle("Gestion des Emprunts");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(225, 225, 220)); 
        
        // Panneau de formulaire
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.setBackground(new Color(235, 235, 230)); 
        
        JLabel lblLivre = new JLabel("Livre:");
        lblLivre.setForeground(new Color(50, 50, 50));
        formPanel.add(lblLivre);
        cmbLivres = new JComboBox<>();
        // cmbLivres.setForeground(new Color(50, 50, 50)); // JComboBox color is handled differently
        formPanel.add(cmbLivres);
        
        JLabel lblUtilisateur = new JLabel("Utilisateur:");
        lblUtilisateur.setForeground(new Color(50, 50, 50));
        formPanel.add(lblUtilisateur);
        cmbUtilisateurs = new JComboBox<>();
        // cmbUtilisateurs.setForeground(new Color(50, 50, 50)); // JComboBox color is handled differently
        formPanel.add(cmbUtilisateurs);
        
        JLabel lblDateEmprunt = new JLabel("Date d'emprunt (AAAA-MM-JJ):");
        lblDateEmprunt.setForeground(new Color(50, 50, 50));
        formPanel.add(lblDateEmprunt);
        txtDateEmprunt = new JTextField();
        txtDateEmprunt.setForeground(new Color(50, 50, 50));
        txtDateEmprunt.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        formPanel.add(txtDateEmprunt);
        
        JLabel lblDateRetourPrevue = new JLabel("Date de retour prévue (AAAA-MM-JJ):");
        lblDateRetourPrevue.setForeground(new Color(50, 50, 50));
        formPanel.add(lblDateRetourPrevue);
        txtDateRetourPrevue = new JTextField();
        txtDateRetourPrevue.setForeground(new Color(50, 50, 50));
        txtDateRetourPrevue.setText(LocalDate.now().plusDays(14).format(DateTimeFormatter.ISO_LOCAL_DATE));
        formPanel.add(txtDateRetourPrevue);
        
        // Panneau de boutons pour le formulaire
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formButtonPanel.setBackground(new Color(235, 235, 230));
        
        btnEnregistrer = new JButton("Enregistrer l'emprunt");
        btnEnregistrer.setBackground(new Color(200, 200, 200));
        btnEnregistrer.setForeground(Color.BLACK);
        btnEnregistrer.setBorder(BorderFactory.createRaisedBevelBorder());
        
        btnMarquerRetourne = new JButton("Marquer comme retourné");
        btnMarquerRetourne.setBackground(new Color(200, 200, 200));
        btnMarquerRetourne.setForeground(Color.BLACK);
        btnMarquerRetourne.setBorder(BorderFactory.createRaisedBevelBorder());
        
        formButtonPanel.add(btnEnregistrer);
        formButtonPanel.add(btnMarquerRetourne);
        
        // Panneau de filtrage
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(235, 235, 230));
        chkAfficherSeulementEnCours = new JCheckBox("Afficher seulement les emprunts en cours");
        chkAfficherSeulementEnCours.setBackground(new Color(235, 235, 230));
        chkAfficherSeulementEnCours.setSelected(true);
        filterPanel.add(chkAfficherSeulementEnCours);
        
        btnRechercher = new JButton("Actualiser");
        btnRechercher.setBackground(new Color(200, 200, 200));
        btnRechercher.setForeground(Color.BLACK);
        btnRechercher.setBorder(BorderFactory.createRaisedBevelBorder());
        filterPanel.add(btnRechercher);
        
        // Tableau des emprunts
        tableModel = new DefaultTableModel(new Object[]{"ID", "Livre", "Utilisateur", "Date d'emprunt", "Date de retour prévue", "Date de retour effective", "Statut"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non éditable
            }
        };
        tableEmprunts = new JTable(tableModel);
        tableEmprunts.setGridColor(new Color(180, 180, 180));
        tableEmprunts.getTableHeader().setBackground(new Color(210, 210, 210));
        tableEmprunts.getTableHeader().setForeground(Color.BLACK);
        tableEmprunts.setSelectionBackground(new Color(200, 200, 220));
        JScrollPane scrollPane = new JScrollPane(tableEmprunts);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        
        // Bouton de retour
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(225, 225, 220)); // Même couleur que le panneau principal
        btnRetour = new JButton("Retour au menu principal");
        btnRetour.setBackground(new Color(200, 200, 200));
        btnRetour.setForeground(Color.BLACK);
        btnRetour.setBorder(BorderFactory.createRaisedBevelBorder());
        bottomPanel.add(btnRetour);
        
        // Ajout des composants au panneau principal
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(formButtonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Ajout du panneau principal à la fenêtre
        add(mainPanel);
        
        // Chargement initial des données
        chargerLivres();
        chargerUtilisateurs();
        chargerEmprunts();
        
        // Ajout des écouteurs d'événements
        ajouterEcouteurs();
    }
    
    private void chargerLivres() {
        cmbLivres.removeAllItems();
        indexToLivreId.clear();
        
        List<Livre> livres = livreDAO.listerTous();
        int index = 0;
        for (Livre livre : livres) {
            if (livre.isDisponible()) {
                cmbLivres.addItem(livre.getTitre() + " - " + livre.getAuteur() + " (" + livre.getIsbn() + ")");
                indexToLivreId.put(index, livre.getId());
                index++;
            }
        }
    }
    
    private void chargerUtilisateurs() {
        cmbUtilisateurs.removeAllItems();
        indexToUtilisateurId.clear();
        
        List<Utilisateur> utilisateurs = utilisateurDAO.listerTous();
        int index = 0;
        for (Utilisateur utilisateur : utilisateurs) {
            cmbUtilisateurs.addItem(utilisateur.getNom() + " " + utilisateur.getPrenom() + " (" + utilisateur.getEmail() + ")");
            indexToUtilisateurId.put(index, utilisateur.getId());
            index++;
        }
    }
    
    private void chargerEmprunts() {
        // Effacer le tableau
        tableModel.setRowCount(0);
        
        List<Emprunt> emprunts;
        if (chkAfficherSeulementEnCours.isSelected()) {
            emprunts = empruntDAO.listerEmpruntsEnCours();
        } else {
            // Vous devrez ajouter une méthode pour lister tous les emprunts dans EmpruntDAO
            // Pour l'instant, nous utilisons la méthode existante
            emprunts = empruntDAO.listerEmpruntsEnCours();
        }
        
        for (Emprunt emprunt : emprunts) {
            String statut = emprunt.estRetourne() ? "Retourné" : (emprunt.estEnRetard() ? "En retard" : "En cours");
            String dateRetourEffective = emprunt.getDateRetourEffective() != null ? 
                                        emprunt.getDateRetourEffective().format(DateTimeFormatter.ISO_LOCAL_DATE) : 
                                        "Non retourné";
            
            tableModel.addRow(new Object[]{
                emprunt.getId(),
                emprunt.getLivre().getTitre(),
                emprunt.getUtilisateur().getNom() + " " + emprunt.getUtilisateur().getPrenom(),
                emprunt.getDateEmprunt().format(DateTimeFormatter.ISO_LOCAL_DATE),
                emprunt.getDateRetourPrevue().format(DateTimeFormatter.ISO_LOCAL_DATE),
                dateRetourEffective,
                statut
            });
        }
    }
    
    private void ajouterEcouteurs() {
        // Écouteur pour le bouton Enregistrer
        btnEnregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enregistrerEmprunt();
            }
        });
        
        // Écouteur pour le bouton Marquer comme retourné
        btnMarquerRetourne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marquerCommeRetourne();
            }
        });
        
        // Écouteur pour le bouton Rechercher/Actualiser
        btnRechercher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chargerEmprunts();
            }
        });
        
        // Écouteur pour la case à cocher
        chkAfficherSeulementEnCours.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chargerEmprunts();
            }
        });
        
        // Écouteur pour le bouton Retour
        btnRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Fermer cette fenêtre
            }
        });
    }
    
    private void enregistrerEmprunt() {
        try {
            if (cmbLivres.getSelectedIndex() == -1 || cmbUtilisateurs.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre et un utilisateur", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int livreId = indexToLivreId.get(cmbLivres.getSelectedIndex());
            int utilisateurId = indexToUtilisateurId.get(cmbUtilisateurs.getSelectedIndex());
            
            LocalDate dateEmprunt = LocalDate.parse(txtDateEmprunt.getText());
            LocalDate dateRetourPrevue = LocalDate.parse(txtDateRetourPrevue.getText());
            
            Emprunt emprunt = new Emprunt(livreId, utilisateurId);
            emprunt.setDateEmprunt(dateEmprunt);
            emprunt.setDateRetourPrevue(dateRetourPrevue);
            
            if (empruntDAO.ajouter(emprunt)) {
                JOptionPane.showMessageDialog(this, "Emprunt enregistré avec succès");
                // Recharger les listes car la disponibilité des livres a changé
                chargerLivres();
                chargerEmprunts();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement de l'emprunt", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void marquerCommeRetourne() {
        int selectedRow = tableEmprunts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt à marquer comme retourné", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String statut = (String) tableModel.getValueAt(selectedRow, 6);
        
        if ("Retourné".equals(statut)) {
            JOptionPane.showMessageDialog(this, "Cet emprunt est déjà marqué comme retourné", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (empruntDAO.marquerCommeRetourne(id)) {
            JOptionPane.showMessageDialog(this, "Emprunt marqué comme retourné avec succès");
            // Recharger les listes car la disponibilité des livres a changé
            chargerLivres();
            chargerEmprunts();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors du marquage de l'emprunt comme retourné", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}