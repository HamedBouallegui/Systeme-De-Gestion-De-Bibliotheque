package com.mycompany.my_bibliotheque.models;

import java.util.ArrayList;
import java.util.List;

public class Etudiant {
    private int id;
    private String nom;
    private String prenom;
    private String numeroEtudiant;
    private String classe;
    private List<Note> notes;
    
    public Etudiant() {
        this.notes = new ArrayList<>();
    }
    
    public Etudiant(String nom, String prenom, String numeroEtudiant, String classe) {
        this.nom = nom;
        this.prenom = prenom;
        this.numeroEtudiant = numeroEtudiant;
        this.classe = classe;
        this.notes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
    
    public List<Note> getNotes() {
        return notes;
    }
    
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
    
    public void ajouterNote(Note note) {
        this.notes.add(note);
    }
    
    public double calculerMoyenne() {
        if (notes.isEmpty()) {
            return 0.0;
        }
        
        double somme = 0.0;
        double totalCoefficients = 0.0;
        
        for (Note note : notes) {
            somme += note.getValeur() * note.getMatiere().getCoefficient();
            totalCoefficients += note.getMatiere().getCoefficient();
        }
        
        return totalCoefficients > 0 ? somme / totalCoefficients : 0.0;
    }
    
    public boolean estAdmis() {
        return calculerMoyenne() >= 10.0;
    }
}