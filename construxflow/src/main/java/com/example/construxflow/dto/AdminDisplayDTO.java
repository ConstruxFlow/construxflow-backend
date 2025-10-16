package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDisplayDTO {
    private long totalUsers;
    private long activeUsers; // ONTASK users
    private long availableUsers; // Users with AVAILABLE status
    private long unavailableUsers; // Users with UNAVAILABLE status
}