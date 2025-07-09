package com.example.construxflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchasingorder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long po_id;

    private String ponumber;
    private Date order_date;
    private String status;
    private String additional_info;
    private BigDecimal subTotal;
    private BigDecimal items;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "supplier_id",referencedColumnName = "supplier_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "purchasingorder",cascade = CascadeType.ALL)
    private List<PurchasingOrder_materials> materials;

    @OneToMany(mappedBy = "purchasingorder",cascade = CascadeType.ALL)
    private List<PurchasingOrder_Delivery> deliveries;

    @OneToMany(mappedBy = "purchasingorder",cascade = CascadeType.ALL)
    private List<PurchasingOrder_Doc> docs;

    @OneToOne(mappedBy = "purchasingorder",cascade = CascadeType.ALL)
    private Order_payment order_payment;

}
