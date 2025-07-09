package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quotation_attachment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quotation_id", referencedColumnName = "id")
    private Quotation quotation;

    private String fileName;
    private String fileType;
    private String fileUrl;
}
