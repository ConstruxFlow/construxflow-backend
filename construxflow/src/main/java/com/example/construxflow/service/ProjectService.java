package com.example.construxflow.service;

import com.example.construxflow.dto.*;
import com.example.construxflow.entity.*;
import com.example.construxflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectPhaseRepository projectPhaseRepository;

    @Autowired
    private MaterialsRepository materialsRepository;

    @Autowired
    private PhaseMaterialRepository phaseMaterialRepository;

    private final String UPLOAD_DIR = "uploads/boq/";

    public ProjectResponseDTO createProject(ProjectRequestDTO request) {
        String projectId = "PROJ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Project project = new Project();
        project.setProjectId(projectId);
        project.setProjectName(request.getProjectName());
        project.setLocation(request.getLocation());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setProgressStatus(request.getProgressStatus());

        project = projectRepository.save(project);

        if (request.getBoqFile() != null && !request.getBoqFile().isEmpty()) {
            String filePath = uploadBoqFile(request.getBoqFile(), projectId);

            Project_doc projectDoc = new Project_doc();
            projectDoc.setDoc_name(request.getBoqFile().getOriginalFilename());
            projectDoc.setDoc_path(filePath);
            projectDoc.setProject(project);

            List<Project_doc> docs = new ArrayList<>();
            docs.add(projectDoc);
            project.setProjectDocs(docs);
        }

        if (request.getPhases() != null && !request.getPhases().isEmpty()) {
            List<Project_phase> phases = new ArrayList<>();

            for (PhaseRequestDTO phaseDTO : request.getPhases()) {
                Project_phase phase = new Project_phase();
                phase.setPhase_name(phaseDTO.getPhaseName());
                phase.setStart_date(phaseDTO.getStartDate());
                phase.setEnd_date(phaseDTO.getEndDate());
                phase.setStatus(phaseDTO.getStatus());
                phase.setProject(project);

                phase = projectPhaseRepository.save(phase);

                if (phaseDTO.getMaterials() != null && !phaseDTO.getMaterials().isEmpty()) {
                    List<Phase_material> phaseMaterials = new ArrayList<>();

                    for (PhaseMaterialRequestDTO materialDTO : phaseDTO.getMaterials()) {
                        Materials material = getOrCreateMaterial(materialDTO);

                        Phase_material phaseMaterial = new Phase_material();
                        phaseMaterial.setMaterial(material);
                        phaseMaterial.setQuantity(materialDTO.getQuantity());
                        phaseMaterial.setProject_phase(phase);

                        phaseMaterials.add(phaseMaterial);
                    }

                    phase.setPhaseMaterials(phaseMaterials);
                }

                phases.add(phase);
            }

            project.setProjectPhases(phases);
        }

        project = projectRepository.save(project);

        return convertToResponseDTO(project);
    }

    private Materials getOrCreateMaterial(PhaseMaterialRequestDTO materialDTO) {
        Materials material;

        if (materialDTO.getMaterialId() != null) {
            material = materialsRepository.findById(materialDTO.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));
        } else {
            material = materialsRepository.findByMaterialNameAndMaterialType(
                            materialDTO.getMaterialName(), materialDTO.getMaterialType())
                    .orElseGet(() -> {
                        Materials newMaterial = new Materials();
                        newMaterial.setMaterialName(materialDTO.getMaterialName());
                        newMaterial.setMaterialType(materialDTO.getMaterialType());
                        newMaterial.setUnitOfMeasurement(materialDTO.getUnitOfMeasurement());
                        return materialsRepository.save(newMaterial);
                    });
        }

        return material;
    }

    private String uploadBoqFile(MultipartFile file, String projectId) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = projectId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload BOQ file", e);
        }
    }

    public List<MaterialRequestListDTO> getMaterialRequestList() {
        List<Phase_material> phaseMaterials = phaseMaterialRepository.findAllPhaseMaterials();

        return phaseMaterials.stream()
                .map(this::convertToMaterialRequestListDTO)
                .collect(Collectors.toList());
    }

    public List<MaterialRequestListDTO> getMaterialRequestListByProject(String projectId) {
        List<Phase_material> phaseMaterials = phaseMaterialRepository.findByProjectId(projectId);

        return phaseMaterials.stream()
                .map(this::convertToMaterialRequestListDTO)
                .collect(Collectors.toList());
    }

    private MaterialRequestListDTO convertToMaterialRequestListDTO(Phase_material phaseMaterial) {
        MaterialRequestListDTO dto = new MaterialRequestListDTO();
        dto.setMaterialId("#MAT-" + String.format("%03d", phaseMaterial.getPhase_material_id()));
        dto.setProjectName(phaseMaterial.getProject_phase().getProject().getProjectName());
        dto.setPhaseName(phaseMaterial.getProject_phase().getPhase_name());
        dto.setMaterialName(phaseMaterial.getMaterial().getMaterialName());
        dto.setQuantity(phaseMaterial.getQuantity());
        dto.setUnitOfMeasurement(phaseMaterial.getMaterial().getUnitOfMeasurement());
        dto.setStatus("NOT_REQUESTED");

        return dto;
    }

    public ProjectResponseDTO getProject(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return convertToResponseDTO(project);
    }

    public List<ProjectResponseDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private ProjectResponseDTO convertToResponseDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getProjectName());
        dto.setLocation(project.getLocation());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setProgressStatus(project.getProgressStatus());

        if (project.getProjectPhases() != null) {
            List<PhaseResponseDTO> phaseDTOs = project.getProjectPhases().stream()
                    .map(this::convertPhaseToResponseDTO)
                    .collect(Collectors.toList());
            dto.setPhases(phaseDTOs);
        }

        if (project.getProjectDocs() != null) {
            List<String> docPaths = project.getProjectDocs().stream()
                    .map(Project_doc::getDoc_path)
                    .collect(Collectors.toList());
            dto.setDocumentPaths(docPaths);
        }

        return dto;
    }

    private PhaseResponseDTO convertPhaseToResponseDTO(Project_phase phase) {
        PhaseResponseDTO dto = new PhaseResponseDTO();
        dto.setPhaseId(phase.getPhase_id());
        dto.setPhaseName(phase.getPhase_name());
        dto.setStartDate(phase.getStart_date());
        dto.setEndDate(phase.getEnd_date());
        dto.setStatus(phase.getStatus());

        if (phase.getPhaseMaterials() != null) {
            List<PhaseMaterialResponseDTO> materialDTOs = phase.getPhaseMaterials().stream()
                    .map(this::convertPhaseMaterialToResponseDTO)
                    .collect(Collectors.toList());
            dto.setMaterials(materialDTOs);
        }

        return dto;
    }

    private PhaseMaterialResponseDTO convertPhaseMaterialToResponseDTO(Phase_material phaseMaterial) {
        PhaseMaterialResponseDTO dto = new PhaseMaterialResponseDTO();
        dto.setPhaseMaterialId(phaseMaterial.getPhase_material_id());
        dto.setMaterialId(phaseMaterial.getMaterial().getMaterial_id());
        dto.setMaterialName(phaseMaterial.getMaterial().getMaterialName());
        dto.setMaterialType(phaseMaterial.getMaterial().getMaterialType());
        dto.setUnitOfMeasurement(phaseMaterial.getMaterial().getUnitOfMeasurement());
        dto.setQuantity(phaseMaterial.getQuantity());

        return dto;
    }
}
