package com.example.construxflow.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supplier_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Supplier {
    @Id
    private String supplier_id;

    private String name;
    private String company_name;
    private String Business_Registration_Number;
    private String Delivery_Capabilities;
    private String status;
    private String bank_name;
    private String bank_account_name;
    private String bank_account_number;
    private String on_time_delivery_rate;
    private String quotation_acceptance_rate;
    private Long past_orders_completed;
    private Long avg_delay_days;
    private Long rating_by_site_manager;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Supplier_material> materials;

    @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Supplier_doc> documents;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserDetails userDetails;

    public String getSupplierId() {
        return  supplier_id;
    }
}
