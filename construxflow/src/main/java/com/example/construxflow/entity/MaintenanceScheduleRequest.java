package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_schedule_request")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaintenanceScheduleRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    private String equipmentName;
    private String equipmentType;

    private LocalDateTime requestedAt;

    @Enumerated(EnumType.STRING)
    private MaintenanceRequestStatus status;

    private String reason;
    private String notes;

    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();
        if (status == null) {
            status = MaintenanceRequestStatus.PENDING;
        }
    }
}