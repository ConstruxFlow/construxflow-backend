package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "request_maintenance_materials")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request_manintenance_materials {

    @Id
    private String id;

    // Join to Equipment_scheduling using equipmentId
    @ManyToOne
    @JoinColumn(name = "equipmentId", referencedColumnName = "id")
    private Equipment_scheduling equipment;

    private String itemId;
    private String itemName;
    @Column(name = "quantity")
    private Double quantity; // or Integer if you only need whole numbers
    private String measurement;
    private String justification;
    private String urgency;

    // ✅ ADD THIS: Status for individual material requests
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, FULFILLED

    // ✅ Add inventory update tracking
    private Boolean inventoryUpdated = false;
    private String inventoryUpdateNotes;
}
