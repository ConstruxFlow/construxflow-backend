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
public class Next_Equipment_Schedule {

    @Id
    private String nextScheduleId;
    private String assignId;
    private String equipmentId;
    private String equipmentScheduleId;
    private String nextMaintenanceType;
    private String nextDate;
    private String estimateDuration;
    private String priority;
    private String technicianId;
    private String lastMaintenanceDate;

}
