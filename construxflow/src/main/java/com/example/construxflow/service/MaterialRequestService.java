package com.example.construxflow.service;

import com.example.construxflow.dto.MaterialRequestCreateDTO;
import com.example.construxflow.entity.Material_request;

import java.util.List;
import java.util.Optional;

public interface MaterialRequestService {
    Material_request createMaterialRequest(MaterialRequestCreateDTO dto);

    List<Material_request> getAllMaterialRequests();
    Optional<Material_request> getMaterialRequestById(Long id);
    Material_request updateMaterialRequestStatus(Long id, String status);
} 