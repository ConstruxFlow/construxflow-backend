package com.example.construxflow.service;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {
    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    public String generateEmailVerificationLink(String email) throws Exception {
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.builder()
                .setUrl("http://localhost:3000/verify-email?verified=1") // Your frontend page
                .setHandleCodeInApp(false)
                .build();

        return FirebaseAuth.getInstance().generateEmailVerificationLink(email, actionCodeSettings);
    }


}
