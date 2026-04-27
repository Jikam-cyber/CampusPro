package com.campuspro.model;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String classeId;
    private String password;
    // Map<matiereId, note>
    private Map<String, Double> notes = new HashMap<>();

    public Student() {}

    public Student(String id, String nom, String prenom, String email, String classeId, String password) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.classeId = classeId;
        this.password = password;
    }

    public double getMoyenneGenerale() {
        if (notes.isEmpty()) return 0.0;
        return notes.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public String getMention() {
        double moy = getMoyenneGenerale();
        if (moy >= 16) return "Très Bien";
        if (moy >= 14) return "Bien";
        if (moy >= 12) return "Assez Bien";
        if (moy >= 10) return "Passable";
        return "Insuffisant";
    }

    public String getMentionClass() {
        double moy = getMoyenneGenerale();
        if (moy >= 16) return "mention-tb";
        if (moy >= 14) return "mention-b";
        if (moy >= 12) return "mention-ab";
        if (moy >= 10) return "mention-p";
        return "mention-i";
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getClasseId() { return classeId; }
    public void setClasseId(String classeId) { this.classeId = classeId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Map<String, Double> getNotes() { return notes; }
    public void setNotes(Map<String, Double> notes) { this.notes = notes; }
    public String getNomComplet() { return prenom + " " + nom; }
}
