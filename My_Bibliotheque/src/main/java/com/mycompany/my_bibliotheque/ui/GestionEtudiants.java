package com.mycompany.my_bibliotheque.ui;

import com.mycompany.my_bibliotheque.dao.EtudiantDAO;
import com.mycompany.my_bibliotheque.dao.MatiereDAO;
import com.mycompany.my_bibliotheque.dao.NoteDAO;
import com.mycompany.my_bibliotheque.models.Etudiant;
import com.mycompany.my_bibliotheque.models.Matiere;
import com.mycompany.my_bibliotheque.models.Note;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Font;

public class GestionEtudiants extends JFrame {
    
    private EtudiantDAO etudiantDAO;
    private MatiereDAO matiereDAO;
    private NoteDAO noteDAO;
    private JTextField txtNom, txtPrenom, txtNumeroEtudiant, txtClasse, txtRecherche;
    private JTable tableEtudiants;
    private DefaultTableModel tableModelEtudiants;
    private JTable tableNotes;
    private DefaultTableModel tableModelNotes;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnRechercher, btnRetour;
    private JButton btnAjouterNote, btnSupprimerNote;
    private JComboBox<Matiere> cmbMatieres;
    private JTextField txtValeurNote;
    private JTabbedPane tabbedPane;
    private JPanel panelAdmis, panelRecales;
    private JTable tableAdmis, tableRecales;
    private DefaultTableModel tableModelAdmis, tableModelRecales;
    
    public GestionEtudiants() {
        etudiantDAO = new EtudiantDAO();
        matiereDAO = new MatiereDAO();
        noteDAO = new NoteDAO();
        
        // Configuration de la fenêtre
        setTitle("Gestion des Étudiants et des Notes");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du panneau à onglets
        tabbedPane = new JTabbedPane();
        
        // Onglet Gestion des étudiants
        JPanel panelGestionEtudiants = creerPanelGestionEtudiants();
        tabbedPane.addTab("Gestion des étudiants", panelGestionEtudiants);
        
        // Onglet Gestion des notes
        JPanel panelGestionNotes = creerPanelGestionNotes();
        tabbedPane.addTab("Gestion des notes", panelGestionNotes);
        
        // Onglet Résultats
        JPanel panelResultats = creerPanelResultats();
        tabbedPane.addTab("Résultats", panelResultats);
        
        // Ajout du panneau à onglets à la fenêtre
        add(tabbedPane);
        
        // Chargement initial des données
        chargerEtudiants();
        chargerMatieres(); // Pour le JComboBox dans la gestion des notes
        // chargerResultats(); // Appelé dans l'onglet résultats
        
        // Ajout des écouteurs d'événements
        ajouterEcouteurs();
    }
    
    private JPanel creerPanelGestionEtudiants() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 245));
        
        // Panneau de formulaire
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Informations de l'étudiant"));
        
        formPanel.add(new JLabel("Nom:"));
        txtNom = new JTextField();
        formPanel.add(txtNom);
        
        formPanel.add(new JLabel("Prénom:"));
        txtPrenom = new JTextField();
        formPanel.add(txtPrenom);
        
        formPanel.add(new JLabel("Numéro étudiant:"));
        txtNumeroEtudiant = new JTextField();
        formPanel.add(txtNumeroEtudiant);
        
        formPanel.add(new JLabel("Classe:"));
        txtClasse = new JTextField();
        formPanel.add(txtClasse);
        
        // Panneau de boutons pour le formulaire
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        
        formButtonPanel.add(btnAjouter);
        formButtonPanel.add(btnModifier);
        formButtonPanel.add(btnSupprimer);
        
        // Panneau de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Rechercher:"));
        txtRecherche = new JTextField(20);
        searchPanel.add(txtRecherche);
        btnRechercher = new JButton("Rechercher");
        searchPanel.add(btnRechercher);
        
        // Tableau des étudiants
        tableModelEtudiants = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénom", "Numéro Étudiant", "Classe"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non éditable
            }
        };
        tableEtudiants = new JTable(tableModelEtudiants);
        JScrollPane scrollPaneEtudiants = new JScrollPane(tableEtudiants);
        
        // Bouton de retour
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRetour = new JButton("Retour au menu principal");
        bottomPanel.add(btnRetour);
        
        // Ajout des composants au panneau principal de l'onglet
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(formButtonPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPaneEtudiants, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel creerPanelGestionNotes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 245));

        // Panneau pour sélectionner l'étudiant et la matière, et entrer la note
        JPanel formNotesPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formNotesPanel.setBorder(BorderFactory.createTitledBorder("Enregistrer une note"));

        formNotesPanel.add(new JLabel("Étudiant:"));
        // Pour sélectionner un étudiant, il est préférable d'utiliser un JComboBox ou de sélectionner depuis la tableEtudiants
        // Pour l'instant, on va supposer qu'un étudiant est sélectionné dans la tableEtudiants
        // ou ajouter un JComboBox<Etudiant> ici.
        // Pour simplifier, on va lier l'ajout de note à un étudiant sélectionné dans la tableEtudiants.
        // Donc, ce formulaire sera plus simple.
        formNotesPanel.add(new JLabel("Matière:"));
        cmbMatieres = new JComboBox<>(); // Sera rempli avec les matières
        formNotesPanel.add(cmbMatieres);

        formNotesPanel.add(new JLabel("Note:"));
        txtValeurNote = new JTextField();
        formNotesPanel.add(txtValeurNote);

        // Panneau de boutons pour les notes
        JPanel notesButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAjouterNote = new JButton("Ajouter Note");
        btnSupprimerNote = new JButton("Supprimer Note"); // Supprimer une note sélectionnée dans tableNotes
        notesButtonPanel.add(btnAjouterNote);
        notesButtonPanel.add(btnSupprimerNote);

        // Tableau pour afficher les notes de l'étudiant sélectionné
        tableModelNotes = new DefaultTableModel(new Object[]{"ID Note", "Matière", "Note"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableNotes = new JTable(tableModelNotes);
        JScrollPane scrollPaneNotes = new JScrollPane(tableNotes);
        
        // Label pour indiquer quel étudiant est sélectionné
        JLabel lblEtudiantSelectionne = new JLabel("Notes pour l'étudiant sélectionné: (Aucun)");
        // Ce label devra être mis à jour lorsque l'utilisateur sélectionne un étudiant dans l'onglet "Gestion des étudiants"

        JPanel topPanelNotes = new JPanel(new BorderLayout());
        topPanelNotes.add(lblEtudiantSelectionne, BorderLayout.NORTH);
        topPanelNotes.add(formNotesPanel, BorderLayout.CENTER);
        topPanelNotes.add(notesButtonPanel, BorderLayout.SOUTH);

        panel.add(topPanelNotes, BorderLayout.NORTH);
        panel.add(scrollPaneNotes, BorderLayout.CENTER);

        return panel;
    }

    private JPanel creerPanelResultats() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 245));

        // Panneau d'information en haut
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBackground(new Color(240, 240, 245));
        JLabel lblInfo = new JLabel("Résultats: Moyenne minimale pour admission = 10/20");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(lblInfo);

        // Panneau pour les étudiants admis
        panelAdmis = new JPanel(new BorderLayout());
        panelAdmis.setBorder(BorderFactory.createTitledBorder("Étudiants Admis"));
        tableModelAdmis = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénom", "Classe", "Moyenne"}, 0) {
             @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableAdmis = new JTable(tableModelAdmis);
        // Personnaliser l'apparence de la table des admis
        tableAdmis.setSelectionBackground(new Color(200, 255, 200)); // Vert clair pour les admis
        tableAdmis.setGridColor(new Color(180, 180, 180));
        tableAdmis.setRowHeight(25);
        JScrollPane scrollPaneAdmis = new JScrollPane(tableAdmis);
        scrollPaneAdmis.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelAdmis.add(scrollPaneAdmis, BorderLayout.CENTER);
        
        // Ajouter un label pour afficher le nombre d'étudiants admis
        JLabel lblNombreAdmis = new JLabel("Nombre d'étudiants admis: 0");
        lblNombreAdmis.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        panelAdmis.add(lblNombreAdmis, BorderLayout.SOUTH);

        // Panneau pour les étudiants recalés
        panelRecales = new JPanel(new BorderLayout());
        panelRecales.setBorder(BorderFactory.createTitledBorder("Étudiants Recalés"));
        tableModelRecales = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénom", "Classe", "Moyenne"}, 0) {
             @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableRecales = new JTable(tableModelRecales);
        // Personnaliser l'apparence de la table des recalés
        tableRecales.setSelectionBackground(new Color(255, 200, 200)); // Rouge clair pour les recalés
        tableRecales.setGridColor(new Color(180, 180, 180));
        tableRecales.setRowHeight(25);
        JScrollPane scrollPaneRecales = new JScrollPane(tableRecales);
        scrollPaneRecales.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelRecales.add(scrollPaneRecales, BorderLayout.CENTER);
        
        // Ajouter un label pour afficher le nombre d'étudiants recalés
        JLabel lblNombreRecales = new JLabel("Nombre d'étudiants recalés: 0");
        lblNombreRecales.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        panelRecales.add(lblNombreRecales, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelAdmis, panelRecales);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        
        // Panneau de boutons en bas
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnActualiserResultats = new JButton("Actualiser les Résultats");
        btnActualiserResultats.setIcon(UIManager.getIcon("FileView.refreshIcon"));
        btnActualiserResultats.addActionListener(e -> {
            chargerResultats();
            // Mettre à jour les compteurs
            lblNombreAdmis.setText("Nombre d'étudiants admis: " + tableModelAdmis.getRowCount());
            lblNombreRecales.setText("Nombre d'étudiants recalés: " + tableModelRecales.getRowCount());
        });
        
        JButton btnExporterResultats = new JButton("Exporter les Résultats");
        btnExporterResultats.addActionListener(e -> exporterResultats());
        
        buttonPanel.add(btnActualiserResultats);
        buttonPanel.add(btnExporterResultats);

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    
    // Méthode pour exporter les résultats (à implémenter selon vos besoins)
    private void exporterResultats() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer les résultats");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers texte (*.txt)", "txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            // Ajouter l'extension .txt si elle n'est pas présente
            if (!fichier.getName().toLowerCase().endsWith(".txt")) {
                fichier = new File(fichier.getAbsolutePath() + ".txt");
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(fichier))) {
                writer.println("RÉSULTATS DES ÉTUDIANTS\n");
                
                writer.println("ÉTUDIANTS ADMIS (" + tableModelAdmis.getRowCount() + "):");
                writer.println("----------------------------------");
                for (int i = 0; i < tableModelAdmis.getRowCount(); i++) {
                    writer.println(tableModelAdmis.getValueAt(i, 1) + " " + 
                                  tableModelAdmis.getValueAt(i, 2) + " - " + 
                                  tableModelAdmis.getValueAt(i, 3) + " - Moyenne: " + 
                                  tableModelAdmis.getValueAt(i, 4));
                }
                
                writer.println("\nÉTUDIANTS RECALÉS (" + tableModelRecales.getRowCount() + "):");
                writer.println("----------------------------------");
                for (int i = 0; i < tableModelRecales.getRowCount(); i++) {
                    writer.println(tableModelRecales.getValueAt(i, 1) + " " + 
                                  tableModelRecales.getValueAt(i, 2) + " - " + 
                                  tableModelRecales.getValueAt(i, 3) + " - Moyenne: " + 
                                  tableModelRecales.getValueAt(i, 4));
                }
                
                JOptionPane.showMessageDialog(this, "Résultats exportés avec succès dans " + fichier.getName(), 
                                           "Exportation réussie", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation: " + ex.getMessage(), 
                                           "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void chargerEtudiants() {
        tableModelEtudiants.setRowCount(0); // Clear existing data
        List<Etudiant> etudiants = etudiantDAO.listerTous();
        for (Etudiant etudiant : etudiants) {
            tableModelEtudiants.addRow(new Object[]{
                etudiant.getId(),
                etudiant.getNom(),
                etudiant.getPrenom(),
                etudiant.getNumeroEtudiant(),
                etudiant.getClasse()
            });
        }
    }

    private void chargerMatieres() {
        cmbMatieres.removeAllItems();
        List<Matiere> matieres = matiereDAO.listerToutes();
        for (Matiere matiere : matieres) {
            cmbMatieres.addItem(matiere); // Assurez-vous que Matiere.toString() est bien défini pour l'affichage
        }
    }
    
    private void chargerNotesPourEtudiant(int etudiantId) {
        tableModelNotes.setRowCount(0);
        List<Note> notes = noteDAO.listerNotesParEtudiant(etudiantId);
        for (Note note : notes) {
            tableModelNotes.addRow(new Object[]{
                note.getId(),
                note.getMatiere() != null ? note.getMatiere().getNom() : "N/A", // Gérer le cas où la matière n'est pas chargée
                note.getValeur()
            });
        }
    }

    private void chargerResultats() {
        tableModelAdmis.setRowCount(0);
        tableModelRecales.setRowCount(0);
        List<Etudiant> etudiants = etudiantDAO.listerTous();

        for (Etudiant etudiant : etudiants) {
            List<Note> notes = noteDAO.listerNotesParEtudiant(etudiant.getId());
            if (notes.isEmpty()) {
                // Optionnel: gérer les étudiants sans notes (peut-être les mettre dans recalés par défaut ou ignorer)
                tableModelRecales.addRow(new Object[]{etudiant.getId(), etudiant.getNom(), etudiant.getPrenom(), 
                                                    etudiant.getClasse(), "N/A (aucune note)"});
                continue;
            }

            double sommeNotesPonderees = 0;
            double sommeCoefficients = 0;
            boolean notesCompletes = true; // Pour vérifier si toutes les matières ont des notes

            // Idéalement, il faudrait une liste de toutes les matières obligatoires pour calculer la moyenne.
            // Pour simplifier, on calcule la moyenne sur les notes entrées.
            for (Note note : notes) {
                if (note.getMatiere() != null) {
                    sommeNotesPonderees += note.getValeur() * note.getMatiere().getCoefficient();
                    sommeCoefficients += note.getMatiere().getCoefficient();
                } else {
                    notesCompletes = false; // Une matière n'a pas pu être chargée
                    break;
                }
            }
            
            if (!notesCompletes || sommeCoefficients == 0) {
                 tableModelRecales.addRow(new Object[]{etudiant.getId(), etudiant.getNom(), etudiant.getPrenom(), 
                                                     etudiant.getClasse(), "Erreur calcul"});
                 continue;
            }

            double moyenne = sommeNotesPonderees / sommeCoefficients;
            // Définir un seuil d'admission, par exemple 10
            if (moyenne >= 10) {
                tableModelAdmis.addRow(new Object[]{etudiant.getId(), etudiant.getNom(), etudiant.getPrenom(), 
                                                 etudiant.getClasse(), String.format("%.2f", moyenne)});
            } else {
                tableModelRecales.addRow(new Object[]{etudiant.getId(), etudiant.getNom(), etudiant.getPrenom(), 
                                                   etudiant.getClasse(), String.format("%.2f", moyenne)});
            }
        }
    }

    private void ajouterEcouteurs() {
        btnAjouter.addActionListener(e -> ajouterEtudiant());
        btnModifier.addActionListener(e -> modifierEtudiant());
        btnSupprimer.addActionListener(e -> supprimerEtudiant());
        btnRechercher.addActionListener(e -> rechercherEtudiants());
        btnRetour.addActionListener(e -> dispose());

        btnAjouterNote.addActionListener(e -> ajouterNote());
        btnSupprimerNote.addActionListener(e -> supprimerNote());

        // Écouteur pour la sélection dans la table des étudiants pour charger ses notes
        tableEtudiants.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tableEtudiants.getSelectedRow() != -1) {
                int selectedRow = tableEtudiants.getSelectedRow();
                int etudiantId = (int) tableModelEtudiants.getValueAt(selectedRow, 0);
                String nomEtudiant = (String) tableModelEtudiants.getValueAt(selectedRow, 1);
                String prenomEtudiant = (String) tableModelEtudiants.getValueAt(selectedRow, 2);
                
                // Mettre à jour le label dans l'onglet des notes
                JLabel lblEtudiantSelectionne = (JLabel) ((BorderLayout)((JPanel)tabbedPane.getComponentAt(1)).getLayout()).getLayoutComponent(BorderLayout.NORTH);
                if (lblEtudiantSelectionne != null) { // Vérifier si le composant est bien celui attendu
                     Component firstComponentOfTopPanel = ((JPanel)((BorderLayout)panelAdmis.getParent().getLayout()).getLayoutComponent(BorderLayout.NORTH)).getComponent(0);

                    if (tabbedPane.getComponentAt(1) instanceof JPanel) {
                        JPanel panelGestionNotes = (JPanel) tabbedPane.getComponentAt(1);
                        // Supposons que le label est le premier composant du panneau Nord du panelGestionNotes
                        // Cela dépend de la structure exacte de creerPanelGestionNotes
                        // Pour une solution plus robuste, donnez un nom de membre au label.
                        // Pour l'instant, on va essayer de le trouver par une recherche de composant si nécessaire.
                        // Pour l'exemple, on va supposer que vous avez une référence au label.
                        // JLabel lblEtudiantSelectionneNotes = findLabelInPanel(panelGestionNotes, "Notes pour l'étudiant sélectionné:");
                        // if(lblEtudiantSelectionneNotes != null) lblEtudiantSelectionneNotes.setText("Notes pour: " + nomEtudiant + " " + prenomEtudiant);
                    }
                }
                chargerNotesPourEtudiant(etudiantId);
            }
        });
    }

    private void ajouterEtudiant() {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String numeroEtudiantStr = txtNumeroEtudiant.getText().trim();
        String classe = txtClasse.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || numeroEtudiantStr.isEmpty() || classe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Etudiant etudiant = new Etudiant();
        etudiant.setId(0);
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom); 
        etudiant.setNumeroEtudiant(numeroEtudiantStr);
        etudiant.setClasse(classe);
        if (etudiantDAO.ajouter(etudiant)) {
            JOptionPane.showMessageDialog(this, "Étudiant ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerEtudiants();
            viderChampsEtudiant();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'étudiant.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierEtudiant() {
        int selectedRow = tableEtudiants.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant à modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModelEtudiants.getValueAt(selectedRow, 0);
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String numeroEtudiantStr = txtNumeroEtudiant.getText().trim();
        String classe = txtClasse.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || numeroEtudiantStr.isEmpty() || classe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Etudiant etudiant = new Etudiant();
        etudiant.setId(id);
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setNumeroEtudiant(numeroEtudiantStr);
        etudiant.setClasse(classe);
        if (etudiantDAO.modifier(etudiant)) {
            JOptionPane.showMessageDialog(this, "Étudiant modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            chargerEtudiants();
            viderChampsEtudiant();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'étudiant.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerEtudiant() {
        int selectedRow = tableEtudiants.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModelEtudiants.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet étudiant et toutes ses notes associées?", "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            // D'abord supprimer les notes associées (si la base de données n'a pas de ON DELETE CASCADE)
            // List<Note> notesASupprimer = noteDAO.listerNotesParEtudiant(id);
            // for (Note note : notesASupprimer) {
            //     noteDAO.supprimer(note.getId());
            // } // Cette logique est mieux gérée par la BDD ou une méthode DAO dédiée.

            if (etudiantDAO.supprimer(id)) { // Assurez-vous que supprimer dans EtudiantDAO gère aussi les notes ou que la BDD le fait.
                JOptionPane.showMessageDialog(this, "Étudiant supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerEtudiants();
                tableModelNotes.setRowCount(0); // Vider les notes affichées si l'étudiant supprimé était sélectionné
                viderChampsEtudiant();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'étudiant.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void rechercherEtudiants() {
        String termeRecherche = txtRecherche.getText().trim();
        if (termeRecherche.isEmpty()) {
            chargerEtudiants(); // Afficher tous les étudiants si la recherche est vide
            return;
        }
        List<Etudiant> etudiantsFiltres = etudiantDAO.rechercher(termeRecherche); // Vous devez implémenter cette méthode dans EtudiantDAO
        tableModelEtudiants.setRowCount(0);
        for (Etudiant etudiant : etudiantsFiltres) {
            tableModelEtudiants.addRow(new Object[]{
                etudiant.getId(),
                etudiant.getNom(),
                etudiant.getPrenom(),
                etudiant.getNumeroEtudiant(),
                etudiant.getClasse()
            });
        }
    }

    private void ajouterNote() {
        int selectedEtudiantRow = tableEtudiants.getSelectedRow();
        if (selectedEtudiantRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant dans l'onglet 'Gestion des étudiants'.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (cmbMatieres.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une matière.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String valeurNoteStr = txtValeurNote.getText().trim();
        if (valeurNoteStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une valeur pour la note.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            double valeurNote = Double.parseDouble(valeurNoteStr);
            if (valeurNote < 0 || valeurNote > 20) { // Supposons une échelle de 0 à 20
                JOptionPane.showMessageDialog(this, "La valeur de la note doit être entre 0 et 20.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int etudiantId = (int) tableModelEtudiants.getValueAt(selectedEtudiantRow, 0);
            Matiere matiereSelectionnee = (Matiere) cmbMatieres.getSelectedItem();
            
            Note note = new Note();
            note.setEtudiantId(etudiantId);
            note.setMatiereId(matiereSelectionnee.getId());
            note.setValeur(valeurNote);

            if (noteDAO.ajouter(note)) {
                JOptionPane.showMessageDialog(this, "Note ajoutée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                chargerNotesPourEtudiant(etudiantId); // Recharger les notes pour l'étudiant actuel
                txtValeurNote.setText(""); // Vider le champ de la note
                chargerResultats(); // Mettre à jour l'onglet résultats
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la note.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La valeur de la note doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerNote() {
        int selectedNoteRow = tableNotes.getSelectedRow();
        if (selectedNoteRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une note à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int noteId = (int) tableModelNotes.getValueAt(selectedNoteRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette note?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            if (noteDAO.supprimer(noteId)) {
                JOptionPane.showMessageDialog(this, "Note supprimée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                // Recharger les notes de l'étudiant actuellement sélectionné
                int selectedEtudiantRow = tableEtudiants.getSelectedRow();
                if (selectedEtudiantRow != -1) {
                    int etudiantId = (int) tableModelEtudiants.getValueAt(selectedEtudiantRow, 0);
                    chargerNotesPourEtudiant(etudiantId);
                } else {
                    tableModelNotes.setRowCount(0); // Si aucun étudiant n'est sélectionné, vider la table des notes
                }
                chargerResultats(); // Mettre à jour l'onglet résultats
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la note.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viderChampsEtudiant() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtNumeroEtudiant.setText("");
        txtClasse.setText("");
        txtRecherche.setText("");
        tableEtudiants.clearSelection();
    }

    // Vous devrez peut-être ajouter une méthode toString() à votre classe Matiere
    // pour qu'elle s'affiche correctement dans le JComboBox.
    // Exemple dans la classe Matiere:
    // @Override
    // public String toString() {
    //     return nom; // Ou ce que vous voulez afficher
    // }

    public static void main(String[] args) {
        // Pour tester cette fenêtre individuellement
        SwingUtilities.invokeLater(() -> {
            // Assurez-vous que la connexion à la base de données est établie avant de lancer l'UI
            // DatabaseConnection.getConnection(); // Si ce n'est pas déjà géré
            new GestionEtudiants().setVisible(true);
        });
    }
}