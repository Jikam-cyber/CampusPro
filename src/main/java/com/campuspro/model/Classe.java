package com.campuspro.model;

public class Classe {
    private String id;
    private String nom;
    private String niveau;
    private String salle;
    private int capacite;

    public Classe() {}

    public Classe(String id, String nom, String niveau, String salle, int capacite) {
        this.id = id;
        this.nom = nom;
        this.niveau = niveau;
        this.salle = salle;
        this.capacite = capacite;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
    public String getSalle() { return salle; }
    public void setSalle(String salle) { this.salle = salle; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
}
