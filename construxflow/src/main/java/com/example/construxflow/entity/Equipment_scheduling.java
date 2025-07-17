package com.example.construxflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "equipment_scheduling")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment_scheduling {

    @Id
    private String id;

    private String equipmentType;
    private String equipmentName;
    private String maintenanceType;
    private String priority;
    private Date date;
    private Time time;
    private String description;
    private String status;

    // Bidirectional relationship - one equipment can have many maintenance requests
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    private List<Request_manintenance_materials> maintenanceRequests;
}
