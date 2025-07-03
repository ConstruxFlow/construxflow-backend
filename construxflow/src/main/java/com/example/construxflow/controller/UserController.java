package com.example.construxflow.controller;

import com.example.construxflow.dto.UserRequestDetailsDTO;
import com.example.construxflow.dto.UserResponseDetailsDTO;
import com.example.construxflow.entity.UserDetails;
import com.example.construxflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

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
}
