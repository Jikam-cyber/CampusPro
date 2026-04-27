package com.campuspro.controller;

import com.campuspro.model.*;
import com.campuspro.service.DataService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private DataService dataService;

    @GetMapping("/")
    public String root() { return "redirect:/login"; }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", "Identifiants incorrects.");
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String role,
                          @RequestParam String username,
                          @RequestParam String password,
                          HttpSession session) {
        switch (role) {
            case "admin" -> {
                if (dataService.loginAdmin(username, password)) {
                    session.setAttribute("role", "ADMIN");
                    session.setAttribute("userId", "admin");
                    return "redirect:/admin/dashboard";
                }
            }
            case "professor" -> {
                Optional<Professor> prof = dataService.loginProfessor(username, password);
                if (prof.isPresent()) {
                    session.setAttribute("role", "PROFESSOR");
                    session.setAttribute("userId", prof.get().getId());
                    return "redirect:/professor/dashboard";
                }
            }
            case "student" -> {
                Optional<Student> stu = dataService.loginStudent(username, password);
                if (stu.isPresent()) {
                    session.setAttribute("role", "STUDENT");
                    session.setAttribute("userId", stu.get().getId());
                    return "redirect:/student/dashboard";
                }
            }
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
