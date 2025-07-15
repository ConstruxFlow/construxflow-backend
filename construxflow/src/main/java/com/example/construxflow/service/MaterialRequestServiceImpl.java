package com.example.construxflow.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.construxflow.dto.MaterialRequestCreateDTO;
import com.example.construxflow.entity.Material_request;
import com.example.construxflow.entity.Materials;
import com.example.construxflow.entity.Project;
import com.example.construxflow.entity.Requested_material;
import com.example.construxflow.repository.MaterialRequestRepository;
import com.example.construxflow.repository.MaterialsRepository;
import com.example.construxflow.repository.ProjectRepository;
import com.example.construxflow.repository.RequestedMaterialRepository;

@Service
public class MaterialRequestServiceImpl implements MaterialRequestService {

    @Autowired
    private MaterialRequestRepository materialRequestRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MaterialsRepository materialsRepository;
    @Autowired
    private RequestedMaterialRepository requestedMaterialRepository;

    @Override
    public Material_request createMaterialRequest(MaterialRequestCreateDTO dto) {
        try {
            System.out.println("Creating material request for project: " + dto.getProject() + ", phase: " + dto.getPhase());
            
            Material_request request = new Material_request();
            request.setStatus("PENDING");
            // Store phase name and notes in additional_info
            String info = "Phase: " + dto.getPhase();
            if (dto.getNotes() != null && !dto.getNotes().isEmpty()) {
                info += "; Notes: " + dto.getNotes();
            }
            request.setAdditional_info(info);
            request.setPriority(dto.getPriority());
            request.setRequest_date(Date.valueOf(dto.getRequestDate()));

            // Look up Project by name
            Project project = projectRepository.findAll().stream()
                .filter(p -> p.getProjectName().equalsIgnoreCase(dto.getProject()))
                .findFirst().orElse(null);
            
            if (project == null) {
                System.out.println("Project not found: " + dto.getProject());
                throw new RuntimeException("Project not found: " + dto.getProject());
            }
            
            request.setProject(project);
            System.out.println("Project found: " + project.getProjectName());

            // Save the main request first
            Material_request savedRequest = materialRequestRepository.save(request);
            System.out.println("Material request saved with ID: " + savedRequest.getRequest_id());

            // Create Requested_material entries for each material
            List<Requested_material> requestedMaterials = new ArrayList<>();
            for (var materialItem : dto.getMaterials()) {
                System.out.println("Processing material: " + materialItem.getMaterialName());
                
                // Look up Material by name
                Materials material = materialsRepository.findAll().stream()
                    .filter(m -> m.getMaterialName().equalsIgnoreCase(materialItem.getMaterialName()))
                    .findFirst().orElse(null);

                if (material != null) {
                    Requested_material reqMat = new Requested_material();
                    reqMat.setMaterial(material);
                    reqMat.setQuantity(materialItem.getQuantity());
                    reqMat.setMaterial_request(savedRequest);
                    requestedMaterialRepository.save(reqMat);
                    requestedMaterials.add(reqMat);
                    System.out.println("Requested material saved for: " + material.getMaterialName());
                } else {
                    System.out.println("Material not found: " + materialItem.getMaterialName() + " - skipping");
                }
            }

            // Return a simple response instead of the full entity to avoid circular references
            Material_request simpleResponse = new Material_request();
            simpleResponse.setRequest_id(savedRequest.getRequest_id());
            simpleResponse.setStatus(savedRequest.getStatus());
            simpleResponse.setAdditional_info(savedRequest.getAdditional_info());
            simpleResponse.setPriority(savedRequest.getPriority());
            simpleResponse.setRequest_date(savedRequest.getRequest_date());
            
            return simpleResponse;
            
        } catch (Exception e) {
            System.err.println("Error creating material request: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
} 