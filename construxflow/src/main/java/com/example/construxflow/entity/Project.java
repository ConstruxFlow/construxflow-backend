package com.example.construxflow.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    private String project_id;

    private String project_name;
    private String location;
    private String start_date;
    private String end_date;
    private String progress_status;

    @OneToMany(mappedBy = "project", cascade = jakarta.persistence.CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private List<Project_doc> projectDocs;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Project_phase> projectPhases;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Raw_material> rawMaterials;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Material_request> materialRequests;

}
