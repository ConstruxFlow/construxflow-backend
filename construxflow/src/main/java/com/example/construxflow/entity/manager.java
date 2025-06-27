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
@Table(name = "manager_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class manager {
    
    @Id
    private String manager_id;

    private String manager_name;

    @OneToOne(mappedBy = "manager",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private UserDetails userDetails;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Material_request> materialRequests;
}
