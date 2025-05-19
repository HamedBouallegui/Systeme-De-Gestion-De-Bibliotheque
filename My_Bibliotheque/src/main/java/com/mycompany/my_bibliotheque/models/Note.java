package com.mycompany.my_bibliotheque.models;

public class Note {
    private int id;
    private int etudiantId;
    private int matiereId;
    private double valeur;
    
    // Objets associés
    private Etudiant etudiant;
    private Matiere matiere;
    
    public Note() {
    }
    
    public Note(int etudiantId, int matiereId, double valeur) {
        this.etudiantId = etudiantId;
        this.matiereId = matiereId;
        this.valeur = valeur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(int etudiantId) {
        this.etudiantId = etudiantId;
    }

    public int getMatiereId() {
        return matiereId;
    }

    public void setMatiereId(int matiereId) {
        this.matiereId = matiereId;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
}