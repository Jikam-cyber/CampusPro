package com.campuspro.model;

import java.util.List;
import java.util.ArrayList;

public class Professor {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private List<String> matiereIds = new ArrayList<>();
    private List<String> classeIds = new ArrayList<>();

    public Professor() {}

    public Professor(String id, String nom, String prenom, String email, String password,
                     List<String> matiereIds, List<String> classeIds) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.matiereIds = matiereIds;
        this.classeIds = classeIds;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<String> getMatiereIds() { return matiereIds; }
    public void setMatiereIds(List<String> matiereIds) { this.matiereIds = matiereIds; }
    public List<String> getClasseIds() { return classeIds; }
    public void setClasseIds(List<String> classeIds) { this.classeIds = classeIds; }
    public String getNomComplet() { return prenom + " " + nom; }
}
