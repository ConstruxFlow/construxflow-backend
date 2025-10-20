package com.example.construxflow.controller;

import com.example.construxflow.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Endpoint to send a simple email (adjust if needed for HTML email)
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String content) {

        try {
            emailService.sendEmail(to, subject, content);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

    // Optional: Endpoint to send HTML email
    @PostMapping("/send-html")
    public ResponseEntity<String> sendHtmlEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String htmlContent) {

        try {
            emailService.sendHtmlEmail(to, subject, htmlContent);
            return ResponseEntity.ok("HTML Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send HTML email: " + e.getMessage());
        }
    }
}
