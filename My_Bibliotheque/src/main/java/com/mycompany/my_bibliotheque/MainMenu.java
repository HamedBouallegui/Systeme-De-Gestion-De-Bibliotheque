package com.mycompany.my_bibliotheque;

import com.mycompany.my_bibliotheque.dao.EmpruntDAO;
import com.mycompany.my_bibliotheque.dao.LivreDAO;
import com.mycompany.my_bibliotheque.dao.UtilisateurDAO;
import com.mycompany.my_bibliotheque.ui.GestionLivres;
import com.mycompany.my_bibliotheque.ui.GestionUtilisateurs;
import com.mycompany.my_bibliotheque.ui.GestionEmprunts; // Décommentez cette ligne
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    
    private JButton btnGestionLivres;
    private JButton btnGestionUtilisateurs;
    private JButton btnGestionEmprunts;
    
    public MainMenu() {
        // Configuration de la fenêtre principale
        setTitle("Gestion de Bibliothèque");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panneau principal avec un fond coloré
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 240)); // Light cream background
        
        // Titre en haut
        JLabel titleLabel = new JLabel("SYSTÈME DE GESTION DE BIBLIOTHÈQUE", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(26, 82, 118)); // Deep blue for title
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Panneau central avec les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        buttonPanel.setOpaque(false);
        
        // Boutons stylisés
        btnGestionLivres = createStyledButton("Gestion des Livres");
        btnGestionUtilisateurs = createStyledButton("Gestion des Utilisateurs");
        btnGestionEmprunts = createStyledButton("Gestion des Emprunts");
        
        // Ajout des boutons au panneau
        buttonPanel.add(btnGestionLivres);
        buttonPanel.add(btnGestionUtilisateurs);
        buttonPanel.add(btnGestionEmprunts);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Pied de page
        JLabel footerLabel = new JLabel("© 2023 - My Bibliotheque", JLabel.CENTER);
        footerLabel.setForeground(new Color(26, 82, 118)); // Deep blue for footer
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
        
        // Ajout du panneau principal à la fenêtre
        add(mainPanel);
        
        // Configuration des actions des boutons
        setupButtonActions();
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(20, 143, 119)); // Teal color for buttons
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 126, 34)); // Orange on hover
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(20, 143, 119)); // Back to teal
            }
        });
        
        return button;
    }
    
    private void setupButtonActions() {
        // Action pour le bouton Gestion des Livres
        btnGestionLivres.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionLivres().setVisible(true);
            }
        });
        
        btnGestionUtilisateurs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUtilisateurs().setVisible(true);
                // JOptionPane.showMessageDialog(MainMenu.this, "Fonctionnalité en cours de développement", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        btnGestionEmprunts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Décommentez la ligne suivante et commentez la ligne d'après
                new GestionEmprunts().setVisible(true);
                // JOptionPane.showMessageDialog(MainMenu.this, "Fonctionnalité en cours de développement", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    public static void main(String[] args) {
        // Appliquer un look and feel moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Lancer l'application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }
}
