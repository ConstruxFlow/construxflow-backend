package com.example.construxflow.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private FirebaseService firebaseService;

    public FirebaseToken checkAuth(HttpServletRequest request) throws FirebaseAuthException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String idToken = authHeader.replace("Bearer ", "").trim();
        return firebaseService.verifyToken(idToken);
    }
}
