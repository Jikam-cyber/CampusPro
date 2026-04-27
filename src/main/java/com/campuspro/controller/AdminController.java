package com.campuspro.controller;

import com.campuspro.model.*;
import com.campuspro.service.DataService;
import com.campuspro.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private DataService dataService;
    @Autowired private PdfService pdfService;

    private String checkAuth(HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("role"))) return "redirect:/login";
        return null;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String redirect = checkAuth(session);
        if (redirect != null) return redirect;
        model.addAttribute("stats", dataService.getDashboardStats());
        model.addAttribute("classes", dataService.getAllClasses());
        return "admin/dashboard";
    }

    // === STUDENTS ===
    @GetMapping("/students")
    public String students(HttpSession session, Model model) {
        String redirect = checkAuth(session);
        if (redirect != null) return redirect;
        model.addAttribute("students", dataService.getAllStudents());
        model.addAttribute("classes", dataService.getAllClasses());
        return "admin/students";
    }

    @PostMapping("/students/add")
    public String addStudent(@ModelAttribute Student student, HttpSession session) {
        if (checkAuth(session) != null) return "redirect:/login";
        dataService.addStudent(student);
        return "redirect:/admin/students";
    }

    @PostMapping("/students/update")
    public String updateStudent(@ModelAttribute Student student, HttpSession session) {
        if (checkAuth(session) != null) return "redirect:/login";
        dataService.updateStudent(student);
        return "redirect:/admin/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable String id, HttpSession session) {
        if (checkAuth(session) != null) return "redirect:/login";
        dataService.deleteStudent(id);
        return "redirect:/admin/students";
    }

    // === PROFESSORS ===
    @GetMapping("/professors")
    public String professors(HttpSession session, Model model) {
        String redirect = checkAuth(session);
        if (redirect != null) return redirect;
        model.addAttribute("professors", dataService.getAllProfessors());
        model.addAttribute("matieres", dataService.getAllMatieres());
        model.addAttribute("classes", dataService.getAllClasses());
        return "admin/professors";
    }

    @PostMapping("/professors/add")
    public String addProfessor(@RequestParam String nom, @RequestParam String prenom,
                                @RequestParam String email, HttpSession session) {
        if (checkAuth(session) != null) return "redirect:/login";
        Professor p = new Professor();
        p.setNom(nom); p.setPrenom(prenom); p.setEmail(email);
        dataService.addProfessor(p);
        return "redirect:/admin/professors";
    }

    @GetMapping("/professors/delete/{id}")
    public String deleteProfessor(@PathVariable String id, HttpSession session) {
        if (checkAuth(session) != null) return "redirect:/login";
        dataService.deleteProfessor(id);
        return "redirect:/admin/professors";
    }

    // === MATIERES ===
    @GetMapping("/matieres")
    public String matieres(HttpSession session, Model model) {
        String redirect = checkAuth(session);
        if (redirect != null) return redirect;
        model.addAttribute("matieres", dataService.getAllMatieres());
        return "admin/matieres";
    }

    @PostMapping("/matieres/add")
    public String addMatiere(@RequestParam String nom, @RequestParam String code,
                              @RequestParam int coefficient, HttpSession session) {
        if (checkAuth(session) != null) return "redirect:/login";
        Matiere m = new Matiere(); m.setNom(nom); m.setCode(code); m.setCoefficient(coefficient);
        dataService.addMatiere(m);
        return "redirect:/admin/matieres";
    }

    @GetMapping("/matieres/delete/{id}")
    public String deleteMatiere(@PathVariable String id, HttpSession session) {
        if (checkAuth(session) != null) return "redirect:/login";
        dataService.deleteMatiere(id);
        return "redirect:/admin/matieres";
    }

    // === CLASSES ===
    @GetMapping("/classes")
    public String classes(HttpSession session, Model model) {
        String redirect = checkAuth(session);
        if (redirect != null) return redirect;
        model.addAttribute("classes", dataService.getAllClasses());
        return "admin/classes";
    }

    // === BULLETIN PDF ===
    @GetMapping("/bulletin/{studentId}/pdf")
    public void pdfBulletin(@PathVariable String studentId, HttpSession session,
                             HttpServletResponse response) throws Exception {
        if (checkAuth(session) != null) { response.sendRedirect("/login"); return; }
        byte[] pdf = pdfService.generateBulletin(studentId);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bulletin_" + studentId + ".pdf");
        response.getOutputStream().write(pdf);
    }
}
