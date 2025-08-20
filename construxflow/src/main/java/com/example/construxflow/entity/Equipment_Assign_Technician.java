package com.example.construxflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipment_Assign_Technician {

    @Id
    private String assignId;
    private String equipmentId;
    private String equipmentSchedulingId;
    private String technicianId;
    private String duration;
    private String notes;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String status;
}
