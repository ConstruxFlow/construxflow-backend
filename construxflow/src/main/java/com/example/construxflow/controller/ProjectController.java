package com.example.construxflow.controller;

import com.example.construxflow.dto.*;
import com.example.construxflow.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDTO> createProject(@ModelAttribute ProjectRequestDTO request) {
        try {
            ProjectResponseDTO response = projectService.createProject(request);
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

    @GetMapping("/materials/request-list")
    public ResponseEntity<List<MaterialRequestListDTO>> getMaterialRequestList() {
        try {
            List<MaterialRequestListDTO> response = projectService.getMaterialRequestList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
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
}