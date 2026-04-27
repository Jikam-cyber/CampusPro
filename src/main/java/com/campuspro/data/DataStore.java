package com.campuspro.data;

import com.campuspro.model.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataStore {

    private final List<Student> students = new ArrayList<>();
    private final List<Professor> professors = new ArrayList<>();
    private final List<Matiere> matieres = new ArrayList<>();
    private final List<Classe> classes = new ArrayList<>();

    // Admin credentials
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin123";

    public DataStore() {
        initMatieres();
        initClasses();
        initProfessors();
        initStudents();
    }

    private void initMatieres() {
        matieres.add(new Matiere("M1", "Mathématiques", "MATH", 4));
        matieres.add(new Matiere("M2", "Informatique", "INFO", 4));
        matieres.add(new Matiere("M3", "Physique", "PHYS", 3));
        matieres.add(new Matiere("M4", "Anglais", "ANGL", 2));
        matieres.add(new Matiere("M5", "Algorithmes", "ALGO", 3));
    }

    private void initClasses() {
        classes.add(new Classe("C1", "L1 Info A", "Licence 1", "Salle 101", 30));
        classes.add(new Classe("C2", "L1 Info B", "Licence 1", "Salle 102", 30));
        classes.add(new Classe("C3", "L2 Info", "Licence 2", "Salle 201", 25));
        classes.add(new Classe("C4", "L3 Info", "Licence 3", "Salle 301", 20));
        classes.add(new Classe("C5", "M1 Info", "Master 1", "Salle 401", 15));
    }

    private void initProfessors() {
        professors.add(new Professor("P001", "Martin", "Jean", "jean.martin@campus.fr", "prof123",
                Arrays.asList("M1", "M5"), Arrays.asList("C1", "C2")));
        professors.add(new Professor("P002", "Dubois", "Marie", "marie.dubois@campus.fr", "prof123",
                Arrays.asList("M2"), Arrays.asList("C3", "C4")));
        professors.add(new Professor("P003", "Bernard", "Pierre", "pierre.bernard@campus.fr", "prof123",
                Arrays.asList("M3"), Arrays.asList("C1", "C3")));
        professors.add(new Professor("P004", "Leroy", "Sophie", "sophie.leroy@campus.fr", "prof123",
                Arrays.asList("M4"), Arrays.asList("C2", "C5")));
        professors.add(new Professor("P005", "Moreau", "Luc", "luc.moreau@campus.fr", "prof123",
                Arrays.asList("M5"), Arrays.asList("C4", "C5")));
        professors.add(new Professor("P006", "Simon", "Claire", "claire.simon@campus.fr", "prof123",
                Arrays.asList("M1", "M2"), Arrays.asList("C1")));
        professors.add(new Professor("P007", "Laurent", "Marc", "marc.laurent@campus.fr", "prof123",
                Arrays.asList("M2", "M3"), Arrays.asList("C2")));
        professors.add(new Professor("P008", "Petit", "Anne", "anne.petit@campus.fr", "prof123",
                Arrays.asList("M4", "M5"), Arrays.asList("C3")));
        professors.add(new Professor("P009", "Thomas", "David", "david.thomas@campus.fr", "prof123",
                Arrays.asList("M1", "M3"), Arrays.asList("C4")));
        professors.add(new Professor("P010", "Robert", "Isabelle", "isabelle.robert@campus.fr", "prof123",
                Arrays.asList("M2", "M4"), Arrays.asList("C5")));
    }

    private void initStudents() {
        String[][] data = {
            {"ETU001","Diallo","Amadou","C1"},
            {"ETU002","Koné","Fatou","C1"},
            {"ETU003","Traoré","Ibrahim","C1"},
            {"ETU004","Coulibaly","Aïssatou","C2"},
            {"ETU005","Sylla","Moussa","C2"},
            {"ETU006","Bah","Mariama","C2"},
            {"ETU007","Camara","Oumar","C3"},
            {"ETU008","Sow","Kadiatou","C3"},
            {"ETU009","Barry","Mamadou","C3"},
            {"ETU010","Baldé","Fatoumata","C3"},
            {"ETU011","Keita","Seydou","C4"},
            {"ETU012","Touré","Aminata","C4"},
            {"ETU013","Sanogo","Boubacar","C4"},
            {"ETU014","Kouyaté","Hawa","C4"},
            {"ETU015","Cissé","Abdoulaye","C5"},
            {"ETU016","Diarra","Rokhaya","C5"},
            {"ETU017","Fofana","Lansana","C5"},
            {"ETU018","Doumbouya","Nènè","C1"},
            {"ETU019","Soumah","Elhadj","C2"},
            {"ETU020","Guilavogui","Yaye","C3"}
        };
        Random rnd = new Random(42);
        for (String[] d : data) {
            Student s = new Student(d[0], d[1], d[2], d[2].toLowerCase() + "." + d[1].toLowerCase() + "@student.campus.fr", d[3], "etu123");
            Map<String, Double> notes = new HashMap<>();
            for (Matiere m : matieres) {
                double note = 5.0 + Math.round(rnd.nextDouble() * 150) / 10.0;
                if (note > 20) note = 20.0;
                notes.put(m.getId(), note);
            }
            s.setNotes(notes);
            students.add(s);
        }
    }

    // --- Student CRUD ---
    public List<Student> getAllStudents() { return students; }
    public Optional<Student> findStudentById(String id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst();
    }
    public void addStudent(Student s) { students.add(s); }
    public void updateStudent(Student updated) {
        findStudentById(updated.getId()).ifPresent(s -> {
            s.setNom(updated.getNom());
            s.setPrenom(updated.getPrenom());
            s.setEmail(updated.getEmail());
            s.setClasseId(updated.getClasseId());
        });
    }
    public void deleteStudent(String id) { students.removeIf(s -> s.getId().equals(id)); }

    // --- Professor CRUD ---
    public List<Professor> getAllProfessors() { return professors; }
    public Optional<Professor> findProfessorById(String id) {
        return professors.stream().filter(p -> p.getId().equals(id)).findFirst();
    }
    public void addProfessor(Professor p) { professors.add(p); }
    public void deleteProfessor(String id) { professors.removeIf(p -> p.getId().equals(id)); }

    // --- Matiere CRUD ---
    public List<Matiere> getAllMatieres() { return matieres; }
    public Optional<Matiere> findMatiereById(String id) {
        return matieres.stream().filter(m -> m.getId().equals(id)).findFirst();
    }
    public void addMatiere(Matiere m) { matieres.add(m); }
    public void deleteMatiere(String id) { matieres.removeIf(m -> m.getId().equals(id)); }

    // --- Classe CRUD ---
    public List<Classe> getAllClasses() { return classes; }
    public Optional<Classe> findClasseById(String id) {
        return classes.stream().filter(c -> c.getId().equals(id)).findFirst();
    }
    public List<Student> getStudentsByClasse(String classeId) {
        return students.stream().filter(s -> classeId.equals(s.getClasseId())).collect(java.util.stream.Collectors.toList());
    }

    // --- Auth ---
    public Optional<Student> authenticateStudent(String id, String password) {
        return students.stream().filter(s -> s.getId().equals(id) && s.getPassword().equals(password)).findFirst();
    }
    public Optional<Professor> authenticateProfessor(String id, String password) {
        return professors.stream().filter(p -> p.getId().equals(id) && p.getPassword().equals(password)).findFirst();
    }
    public boolean authenticateAdmin(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
}
