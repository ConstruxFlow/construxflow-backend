package com.example.construxflow.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

            // Set new fields for phase and project
            request.setPhase_name(dto.getPhase());
            request.setProject_name(dto.getProject());

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
                    reqMat.setRate(materialItem.getUnitPrice());
                    reqMat.setMaterial_request(savedRequest);
                    reqMat.setStatus("Pending"); // Set status to Pending
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
            // Set new fields in response
            simpleResponse.setPhase_name(savedRequest.getPhase_name());
            simpleResponse.setProject_name(savedRequest.getProject_name());
            return simpleResponse;
            
        } catch (Exception e) {
            System.err.println("Error creating material request: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Material_request> getAllMaterialRequests() {
        try {
            System.out.println("Fetching all material requests");
            List<Material_request> requests = materialRequestRepository.findAll();
            System.out.println("Found " + requests.size() + " material requests");
            return requests;
        } catch (Exception e) {
            System.err.println("Error fetching all material requests: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch material requests", e);
        }
    }

    @Override
    public Optional<Material_request> getMaterialRequestById(Long id) {
        try {
            System.out.println("Fetching material request with ID: " + id);
            Optional<Material_request> request = materialRequestRepository.findById(id);
            if (request.isPresent()) {
                System.out.println("Material request found: " + request.get().getRequest_id());
            } else {
                System.out.println("Material request not found with ID: " + id);
            }
            return request;
        } catch (Exception e) {
            System.err.println("Error fetching material request by ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch material request", e);
        }
    }

    @Override
    public Material_request updateMaterialRequestStatus(Long id, String status) {
        try {

            Optional<Material_request> optionalRequest = materialRequestRepository.findById(id);

            if (optionalRequest.isEmpty()) {
                throw new RuntimeException("Material request not found with ID: " + id);
            }

            Material_request request = optionalRequest.get();
            String oldStatus = request.getStatus();

            // Update only the status
            request.setStatus(status);

            // Save the updated request
            Material_request updatedRequest = materialRequestRepository.save(request);

            return updatedRequest;

        } catch (Exception e) {
            System.err.println("Error updating material request status: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


} 