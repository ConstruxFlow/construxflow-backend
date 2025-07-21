package com.example.construxflow.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.construxflow.dto.MaterialRequestListDTO;
import com.example.construxflow.dto.PhaseMaterialResponseDTO;
import com.example.construxflow.dto.PhaseRequestDTO;
import com.example.construxflow.dto.ProjectRequestDTO;
import com.example.construxflow.dto.ProjectResponseDTO;
import com.example.construxflow.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDTO> createProject(
        @RequestParam("managerId")  String managerId,
        @RequestParam("projectName") String projectName,
        @RequestParam("location") String location,
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate,
        @RequestParam("progressStatus") String progressStatus,
        @RequestParam(value = "boqFile", required = false) MultipartFile boqFile,
        @RequestParam("phases") String phasesJson

    ) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            java.util.List<PhaseRequestDTO> phases = Arrays.asList(mapper.readValue(phasesJson, PhaseRequestDTO[].class));

            ProjectRequestDTO dto = new ProjectRequestDTO(
                    managerId,projectName, location, startDate, endDate, progressStatus, boqFile, phases
            );
            ProjectResponseDTO response = projectService.createProject(dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
        @PathVariable String projectId,
        @RequestParam("managerId")  String managerId,
        @RequestParam("projectName") String projectName,
        @RequestParam("location") String location,
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate,
        @RequestParam("progressStatus") String progressStatus,
        @RequestParam(value = "boqFile", required = false) MultipartFile boqFile,
        @RequestParam("phases") String phasesJson
    ) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            java.util.List<PhaseRequestDTO> phases = Arrays.asList(mapper.readValue(phasesJson, PhaseRequestDTO[].class));
            ProjectRequestDTO dto = new ProjectRequestDTO(
                    managerId, projectName, location, startDate, endDate, progressStatus, boqFile, phases
            );
            ProjectResponseDTO response = projectService.updateProject(projectId, dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProject(@PathVariable String projectId) {
        try {
            ProjectResponseDTO response = projectService.getProject(projectId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        try {
            List<ProjectResponseDTO> response = projectService.getAllProjects();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/debug/projects")
    public ResponseEntity<List<String>> debugProjects() {
        try {
            List<String> projectInfo = projectService.getAllProjects().stream()
                .map(p -> "ID: " + p.getProjectId() + ", Name: " + p.getProjectName())
                .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(projectInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/materials/request-list")
    public ResponseEntity<List<MaterialRequestListDTO>> getMaterialRequestList() {
        System.out.println("[DEBUG] Received GET /api/projects/materials/request-list");
        try {
            List<MaterialRequestListDTO> response = projectService.getMaterialRequestList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{projectId}/materials/request-list")
    public ResponseEntity<List<MaterialRequestListDTO>> getMaterialRequestListByProject(@PathVariable String projectId) {
        try {
            List<MaterialRequestListDTO> response = projectService.getMaterialRequestListByProject(projectId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{projectId}/phases/{phaseName}/materials")
    public ResponseEntity<List<PhaseMaterialResponseDTO>> getPhaseMaterials(
        @PathVariable String projectId,
        @PathVariable String phaseName
    ) {
        try {
            System.out.println("Received request for projectId: " + projectId + ", phaseName: " + phaseName);
            List<PhaseMaterialResponseDTO> response = projectService.getPhaseMaterials(projectId, phaseName);
            System.out.println("Found " + response.size() + " materials for phase: " + phaseName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in getPhaseMaterials: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable String projectId) {
        try {
            projectService.deleteProject(projectId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}