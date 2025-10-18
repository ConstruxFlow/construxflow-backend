package com.example.construxflow.controller;

import com.example.construxflow.service.SmsService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/sms")
public class SmsController {

    private SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send-sms")
    public ResponseEntity<?> sendSms(@RequestBody SmsRequest request) {
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
            return ResponseEntity.badRequest().body("Phone number is required");
        }
        try {
            String sid = smsService.sendSms(request.getPhoneNumber(), request.getMessage());
            return ResponseEntity.ok("SMS sent successfully with SID: " + sid);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send SMS: " + e.getMessage());
        }
    }

    @Data
    public static class SmsRequest {
        private String phoneNumber;
        private String message;
    }
}
