package com.example.construxflow.dto;

import lombok.Data;
import java.util.List;

@Data
public class TeamMemberResponseDTO {
    private String empId;
    private String name;
    private String nic;
    private String email;
    private String phone;
    private String gender;
    private String department;
    private String experience;
    private String joinDate;
    private List<String> specializations;
    private String availabilityStatus;
}
