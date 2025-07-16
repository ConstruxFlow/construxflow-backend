package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order_payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long payment_id;

    private BigDecimal amount;
    private BigDecimal paid_amount;
    private BigDecimal remaining_amount;
    private String payment_type;
    private  String Status;
    private String reference_number;
    private String notes;
    private String bank_details;

    private LocalDateTime payment_date;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime created_date;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id",referencedColumnName = "po_id")
    private PurchasingOrder purchasingorder;
}
