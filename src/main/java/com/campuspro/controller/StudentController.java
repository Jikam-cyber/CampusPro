package com.campuspro.controller;

import com.campuspro.service.DataService;
import com.campuspro.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired private DataService dataService;
    @Autowired private PdfService pdfService;

    private String checkAuth(HttpSession session) {
        if (!"STUDENT".equals(session.getAttribute("role"))) return "redirect:/login";
        return null;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String redirect = checkAuth(session);
        if (redirect != null) return redirect;
        String id = (String) session.getAttribute("userId");
        dataService.getStudentById(id).ifPresent(s -> model.addAttribute("student", s));
        Map<String, Object> bd = dataService.getBulletinData(id);
        model.addAttribute("bd", bd);
        model.addAttribute("matieres", dataService.getAllMatieres());
        return "student/dashboard";
    }

    @GetMapping("/bulletin")
    public String bulletin(HttpSession session, Model model) {
        String redirect = checkAuth(session);
        if (redirect != null) return redirect;
        String id = (String) session.getAttribute("userId");
        Map<String, Object> bd = dataService.getBulletinData(id);
        model.addAllAttributes(bd);
        return "student/bulletin";
    }

    @GetMapping("/bulletin/pdf")
    public void downloadPdf(HttpSession session, HttpServletResponse response) throws Exception {
        if (!"STUDENT".equals(session.getAttribute("role"))) {
            response.sendRedirect("/login");
            return;
        }
        String id = (String) session.getAttribute("userId");
        byte[] pdf = pdfService.generateBulletin(id);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bulletin_" + id + ".pdf");
        response.getOutputStream().write(pdf);
    }
}
