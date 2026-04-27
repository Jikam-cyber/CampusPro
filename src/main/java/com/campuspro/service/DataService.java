package com.campuspro.service;

import com.campuspro.data.DataStore;
import com.campuspro.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataService {

    @Autowired
    private DataStore dataStore;

    // === AUTH ===
    public Optional<Student> loginStudent(String id, String password) {
        return dataStore.authenticateStudent(id, password);
    }
    public Optional<Professor> loginProfessor(String id, String password) {
        return dataStore.authenticateProfessor(id, password);
    }
    public boolean loginAdmin(String username, String password) {
        return dataStore.authenticateAdmin(username, password);
    }

    // === STUDENTS ===
    public List<Student> getAllStudents() { return dataStore.getAllStudents(); }
    public Optional<Student> getStudentById(String id) { return dataStore.findStudentById(id); }
    public void addStudent(Student s) {
        if (s.getId() == null || s.getId().isBlank()) {
            s.setId("ETU" + String.format("%03d", dataStore.getAllStudents().size() + 1));
        }
        if (s.getPassword() == null || s.getPassword().isBlank()) s.setPassword("etu123");
        dataStore.addStudent(s);
    }
    public void updateStudent(Student s) { dataStore.updateStudent(s); }
    public void deleteStudent(String id) { dataStore.deleteStudent(id); }
    public List<Student> getStudentsByClasse(String classeId) { return dataStore.getStudentsByClasse(classeId); }

    // === PROFESSORS ===
    public List<Professor> getAllProfessors() { return dataStore.getAllProfessors(); }
    public Optional<Professor> getProfessorById(String id) { return dataStore.findProfessorById(id); }
    public void addProfessor(Professor p) {
        if (p.getId() == null || p.getId().isBlank()) {
            p.setId("P" + String.format("%03d", dataStore.getAllProfessors().size() + 1));
        }
        if (p.getPassword() == null || p.getPassword().isBlank()) p.setPassword("prof123");
        dataStore.addProfessor(p);
    }
    public void deleteProfessor(String id) { dataStore.deleteProfessor(id); }

    // === MATIERES ===
    public List<Matiere> getAllMatieres() { return dataStore.getAllMatieres(); }
    public Optional<Matiere> getMatiereById(String id) { return dataStore.findMatiereById(id); }
    public void addMatiere(Matiere m) {
        if (m.getId() == null || m.getId().isBlank()) {
            m.setId("M" + (dataStore.getAllMatieres().size() + 1));
        }
        dataStore.addMatiere(m);
    }
    public void deleteMatiere(String id) { dataStore.deleteMatiere(id); }

    // === CLASSES ===
    public List<Classe> getAllClasses() { return dataStore.getAllClasses(); }
    public Optional<Classe> getClasseById(String id) { return dataStore.findClasseById(id); }

    // === NOTES ===
    public void saveNote(String studentId, String matiereId, double note) {
        dataStore.findStudentById(studentId).ifPresent(s -> s.getNotes().put(matiereId, note));
    }

    // === STATS ADMIN ===
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Student> all = dataStore.getAllStudents();
        stats.put("totalStudents", all.size());
        stats.put("totalProfessors", dataStore.getAllProfessors().size());
        stats.put("totalMatieres", dataStore.getAllMatieres().size());
        stats.put("totalClasses", dataStore.getAllClasses().size());

        long admis = all.stream().filter(s -> s.getMoyenneGenerale() >= 10).count();
        stats.put("admis", admis);
        stats.put("recales", all.size() - admis);

        double moyGlobale = all.stream().mapToDouble(Student::getMoyenneGenerale).average().orElse(0.0);
        stats.put("moyenneGlobale", String.format("%.2f", moyGlobale));

        Map<String, Long> parClasse = all.stream()
                .collect(Collectors.groupingBy(Student::getClasseId, Collectors.counting()));
        stats.put("parClasse", parClasse);

        return stats;
    }

    // === BULLETIN DATA ===
    public Map<String, Object> getBulletinData(String studentId) {
        Map<String, Object> data = new HashMap<>();
        Optional<Student> optStudent = dataStore.findStudentById(studentId);
        if (optStudent.isEmpty()) return data;
        Student student = optStudent.get();
        data.put("student", student);

        Optional<Classe> classe = dataStore.findClasseById(student.getClasseId());
        data.put("classe", classe.orElse(null));

        List<Map<String, Object>> lignes = new ArrayList<>();
        for (Matiere m : dataStore.getAllMatieres()) {
            Map<String, Object> ligne = new HashMap<>();
            ligne.put("matiere", m);
            Double note = student.getNotes().get(m.getId());
            ligne.put("note", note != null ? note : 0.0);
            ligne.put("noteStr", note != null ? String.format("%.1f", note) : "—");
            lignes.add(ligne);
        }
        data.put("lignes", lignes);
        data.put("moyenne", String.format("%.2f", student.getMoyenneGenerale()));
        data.put("mention", student.getMention());
        data.put("mentionClass", student.getMentionClass());
        data.put("annee", "2024-2025");
        return data;
    }
}
