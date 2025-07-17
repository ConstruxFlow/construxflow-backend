package com.example.construxflow.dto;

import lombok.Data;
import java.util.List;

@Data
public class TeamMemberRequestDTO {
    private String name;
    private String nic;
    private String email;
    private String phone;
    private String gender;
    private String department;
    private String experience; // or int, depending on your entity
    private String joinDate;   // Prefer LocalDate for strict typing
    private List<String> specializations;
    private String availabilityStatus; // "AVAILABLE" or "UNAVAILABLE"
}
