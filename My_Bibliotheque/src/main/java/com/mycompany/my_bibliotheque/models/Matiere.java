package com.mycompany.my_bibliotheque.models;

public class Matiere {
    private int id;
    private String nom;
    private double coefficient;
    
    public Matiere() {
        this.coefficient = 1.0;
    }
    
    public Matiere(String nom, double coefficient) {
        this.nom = nom;
        this.coefficient = coefficient;
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

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }
    
    @Override
    public String toString() {
        return nom + " (coef. " + coefficient + ")";
    }
}