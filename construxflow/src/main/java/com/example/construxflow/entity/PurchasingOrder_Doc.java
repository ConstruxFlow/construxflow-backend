package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "purchasingorder_doc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasingOrder_Doc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchasingOrder_docId;
    private String documentName;
    private String documentType;
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id",referencedColumnName = "po_id")
    private PurchasingOrder purchasingorder;
}
