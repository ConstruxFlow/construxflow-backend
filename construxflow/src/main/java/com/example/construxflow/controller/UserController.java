package com.example.construxflow.controller;

import com.example.construxflow.dto.UserRequestDetailsDTO;
import com.example.construxflow.dto.UserResponseDetailsDTO;
import com.example.construxflow.entity.UserDetails;
import com.example.construxflow.service.FirebaseService;
import com.example.construxflow.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FirebaseService firebaseService;

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
    public ResponseEntity<?> loginUser(@RequestHeader("Authorization") String token){
        try{
           String idToken = token.replace("Bearer ", "");
            FirebaseToken decodedToken = firebaseService.verifyToken(idToken);
            String firebaseUid = decodedToken.getUid();

            Optional<UserResponseDetailsDTO> userOpt = userService.getUserByFirebaseUid(firebaseUid);
            if(userOpt.isPresent()) {
                UserResponseDetailsDTO user = userOpt.get();
                String role = (String) decodedToken.getClaims().get("role");

                Map<String,Object> response = new HashMap<>();
                response.put("user", user);
                response.put("role", role);
                return ResponseEntity.ok(response);
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
