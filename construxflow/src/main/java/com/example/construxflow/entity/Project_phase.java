package com.example.construxflow.entity;

import java.util.List;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_phases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project_phase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long phase_id;

    private String phase_name;
    private String start_date;
    private String end_date;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private Project project;

    @OneToMany(mappedBy = "project_phase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Phase_material> phaseMaterials;

}
