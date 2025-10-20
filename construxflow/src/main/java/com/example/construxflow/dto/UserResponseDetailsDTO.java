package com.example.construxflow.dto;

import com.example.construxflow.entity.User_Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDetailsDTO {
    private Long userId;
    private String firebaseUid;
    private String userName;
    private String email;
    private Long phoneNumber1;
    private Long phoneNumber2;
    private String address;
    private User_Role userRole;
    private String managerId;
    private String supplierId;
    private LocalDateTime created_at;
}
