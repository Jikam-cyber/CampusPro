package com.campuspro.model;

public class Matiere {
    private String id;
    private String nom;
    private String code;
    private int coefficient;

    public Matiere() {}

    public Matiere(String id, String nom, String code, int coefficient) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.coefficient = coefficient;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public int getCoefficient() { return coefficient; }
    public void setCoefficient(int coefficient) { this.coefficient = coefficient; }
}
