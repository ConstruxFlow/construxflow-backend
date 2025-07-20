package com.example.construxflow.entity;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "material_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material_request {
    
    @Id
    private Long request_id;

    private String status;
    private String additional_info;
    private String priority;

    private Date request_date;

    @OneToMany(mappedBy = "material_request", cascade = jakarta.persistence.CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private List<Requested_material> requested_materials;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private Project project;
    
    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "manager_id")
    private Manager manager;

}
