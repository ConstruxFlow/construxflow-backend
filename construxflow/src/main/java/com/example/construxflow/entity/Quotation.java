package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quotation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quotation_request_id", referencedColumnName = "id")
    private Quotation_request quotationRequest;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    private Supplier supplier;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private BigDecimal advancedPayment;
    private String paymentTerms;
    private String notes;
    private BigDecimal totalAmount;
    private String status;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL)
    private List<QuotationItem> items;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL)
    private List<QuotationDeliveryInfo> deliveryInfos;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL)
    private List<QuotationAttachment> attachments;

}
