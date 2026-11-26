package com.example.CalenDR;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class login {

    @PostMapping("/login")
    private String login(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
        // Hidden for public source release.
        throw new UnsupportedOperationException("Security implementation omitted from public source.");
    }

    @PostMapping("signUp")
    private String signUp(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
        // Hidden for public source release.
        throw new UnsupportedOperationException("Security implementation omitted from public source.");
    }

    @PostMapping("/signOut")
    private String signOut(HttpServletRequest request, HttpServletResponse response, Model model) {
        // Hidden for public source release.
        throw new UnsupportedOperationException("Security implementation omitted from public source.");
    }

    @PostMapping("resetPassword")
    private String resetpw(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
        // Hidden for public source release.
        throw new UnsupportedOperationException("Security implementation omitted from public source.");
    }
}
