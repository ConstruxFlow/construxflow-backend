package com.example.construxflow.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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

    @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Supplier_material> materials;

    @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Supplier_doc> documents;

    @OneToOne(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserDetails userDetails;
}
