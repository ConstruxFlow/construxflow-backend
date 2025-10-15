package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String siteName;            // "Site A – Tower 2"
    private String requestedBy;         // "Kasun (Site Manager)"
    private String requestedAt;         // "2025-08-27 10:15" (keep string for now, or use LocalDateTime)

    private String itemName;            // "Excavator CAT 320"
    private String itemCategory;        // "Machinery" | "Materials"
    private Integer quantity;           // 1, 120, etc.

    @Enumerated(EnumType.STRING)
    private RequestPriority priority;   // HIGH | MEDIUM | LOW

    @Enumerated(EnumType.STRING)
    private RequestStatus status;       // PENDING | APPROVED | REJECTED | PARTIAL

    private String needFrom;            // "2025-09-01"
    private String needTo;              // "2025-09-05"

    @Column(length = 2000)
    private String notes;

    // Optional: tie to equipment catalog ID you already have
    private Long equipmentId;           // may be null for material-only requests
}
