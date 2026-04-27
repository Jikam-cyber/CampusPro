package com.campuspro.controller;

import com.campuspro.model.*;
import com.campuspro.service.DataService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired private DataService dataService;

    private String checkAuth(HttpSession session) {
        if (!"PROFESSOR".equals(session.getAttribute("role"))) return "redirect:/login";
        return null;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String redirect = checkAuth(session);
        if (redirect != null) return redirect;
        String id = (String) session.getAttribute("userId");
        Optional<Professor> prof = dataService.getProfessorById(id);
        prof.ifPresent(p -> {
            model.addAttribute("professor", p);
            List<Classe> classes = p.getClasseIds().stream()
                    .map(cid -> dataService.getClasseById(cid).orElse(null))
                    .filter(c -> c != null).toList();
            model.addAttribute("classes", classes);
            List<Matiere> matieres = p.getMatiereIds().stream()
                    .map(mid -> dataService.getMatiereById(mid).orElse(null))
                    .filter(m -> m != null).toList();
            model.addAttribute("matieres", matieres);
        });
        return "professor/dashboard";
    }

    @GetMapping("/classe/{classeId}")
    public String voirClasse(@PathVariable String classeId, HttpSession session, Model model) {
        String redirect = checkAuth(session);
        if (redirect != null) return redirect;
        String profId = (String) session.getAttribute("userId");
        dataService.getProfessorById(profId).ifPresent(p -> model.addAttribute("professor", p));
        dataService.getClasseById(classeId).ifPresent(c -> model.addAttribute("classe", c));
        List<Student> students = dataService.getStudentsByClasse(classeId);
        model.addAttribute("students", students);
        model.addAttribute("matieres", dataService.getAllMatieres());
        model.addAttribute("classeId", classeId);
        return "professor/classe";
    }

    @PostMapping("/note/save")
    public String saveNote(@RequestParam String studentId,
                           @RequestParam String matiereId,
                           @RequestParam double note,
                           @RequestParam String classeId,
                           HttpSession session) {
        String redirect = checkAuth(session);
        if (redirect != null) return redirect;
        if (note >= 0 && note <= 20) {
            dataService.saveNote(studentId, matiereId, note);
        }
        return "redirect:/professor/classe/" + classeId;
    }
}
