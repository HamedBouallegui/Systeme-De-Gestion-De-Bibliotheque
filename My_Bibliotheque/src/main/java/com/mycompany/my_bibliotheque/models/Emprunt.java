package com.mycompany.my_bibliotheque.models;

import java.time.LocalDate;

public class Emprunt {
    private int id;
    private int livreId;
    private int utilisateurId;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    
    // Objets associés
    private Livre livre;
    private Utilisateur utilisateur;
    
    public Emprunt() {
        this.dateEmprunt = LocalDate.now();
        this.dateRetourPrevue = LocalDate.now().plusDays(14); // Par défaut, 2 semaines
    }
    
    public Emprunt(int livreId, int utilisateurId) {
        this.livreId = livreId;
        this.utilisateurId = utilisateurId;
        this.dateEmprunt = LocalDate.now();
        this.dateRetourPrevue = LocalDate.now().plusDays(14); // Par défaut, 2 semaines
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLivreId() {
        return livreId;
    }

    public void setLivreId(int livreId) {
        this.livreId = livreId;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public boolean estRetourne() {
        return dateRetourEffective != null;
    }

    public boolean estEnRetard() {
        if (dateRetourEffective != null) {
            return dateRetourEffective.isAfter(dateRetourPrevue);
        }
        return LocalDate.now().isAfter(dateRetourPrevue);
    }
}