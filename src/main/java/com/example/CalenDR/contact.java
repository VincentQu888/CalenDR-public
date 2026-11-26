package com.example.CalenDR;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class contact {

    @PostMapping("/email")
	private String send(HttpServletRequest request, Model model){
        String from = request.getParameter("email-form");
        String to = "[redacted]";
        String body = "from: " + from + "\n\n" + request.getParameter("contact-form");
        String subject = request.getParameter("subject-form");
		emailSender.send(from, to, body, subject);

        return "redirect:contact";
	}
}
