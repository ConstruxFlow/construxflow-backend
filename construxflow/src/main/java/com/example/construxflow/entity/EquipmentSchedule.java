package com.example.construxflow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "equipment_schedule")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EquipmentSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // the equipment being scheduled
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    private String siteName; // e.g., "Site A – Block 2"

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status; // SCHEDULED/ACTIVE/COMPLETED/CANCELLED

    @Column(length = 2000)
    private String notes;
}
