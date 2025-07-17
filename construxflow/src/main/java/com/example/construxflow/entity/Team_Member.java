package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team_Member {
    @Id
    private String empId;
    private String name;
    private String nic;
    private String email;
    private String phone;
    private String gender;
    private String department;
    private String experience; // Consider changing to int if you want numeric years

    private String joinDate; // Use java.time.LocalDate for better date handling

    @ElementCollection
    private List<String> specializations; // e.g., ["Plumbing", "HVAC"]

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    public enum AvailabilityStatus {
        AVAILABLE,
        UNAVAILABLE,
        ONTASK,
    }
}
