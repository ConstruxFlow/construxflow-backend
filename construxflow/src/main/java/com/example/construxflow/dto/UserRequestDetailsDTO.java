package com.example.construxflow.dto;

import com.example.construxflow.entity.User_Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDetailsDTO {
    private String user_name;
    private String email;
    private Long phone_number1;
    private Long phone_number2;
    private String address;
    private String password;
    private User_Role userRole;
    private String managerId;
    private String supplierId;
}
