package com.example.construxflow.controller;

import com.example.construxflow.dto.UserRequestDetailsDTO;
import com.example.construxflow.dto.UserResponseDetailsDTO;
import com.example.construxflow.entity.UserDetails;
import com.example.construxflow.service.EmailService;
import com.example.construxflow.service.FirebaseService;
import com.example.construxflow.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDetailsDTO userDto){
        if (userDto.getUserRole() == null) {
            return ResponseEntity.badRequest().body("User role is required");
        }
        try{
            UserResponseDetailsDTO savedUser = userService.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authHeader){
        try{
            String idtoken = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = firebaseService.verifyToken(idtoken);
            String firebaseUid = decodedToken.getUid();

            UserRecord userRecord = FirebaseAuth.getInstance().getUser(firebaseUid);
            if (!userRecord.isEmailVerified()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Please verify your email before logging in.");
            }

            Optional<UserResponseDetailsDTO> userOpt = userService.getUserByFirebaseUid(firebaseUid);
            if(userOpt.isPresent()){
                UserResponseDetailsDTO user = userOpt.get();
                String role = (String) decodedToken.getClaims().get("role");

                Set<String> allowedRoles = Set.of(
                        "ADMIN",
                        "SITE_MANAGER",
                        "INVENTORY_MANAGER",
                        "FINANCE_OFFICER",
                        "MAINTENANCE_HEAD",
                        "SUPPLIER",
                        "PURCHASING_MANAGER"
                );
                if (role != null && allowedRoles.contains(role.toUpperCase())) {
                    // Wrap the user DTO in a "user" property
                    Map<String, Object> response = new HashMap<>();
                    response.put("user", user);
                    response.put("role", user.getUserRole() != null ? user.getUserRole().name() : role);
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user role");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestParam String email) {
        try {
            String verificationLink = firebaseService.generateEmailVerificationLink(email);
            emailService.sendEmail(email, "Verify your email",
                    "Please verify your email by clicking this link: " + verificationLink);
            return ResponseEntity.ok("Verification email sent.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not send verification email.");
        }
    }


}
