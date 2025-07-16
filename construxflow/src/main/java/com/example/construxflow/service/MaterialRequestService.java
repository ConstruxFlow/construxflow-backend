package com.example.construxflow.service;

import com.example.construxflow.dto.MaterialRequestCreateDTO;
import com.example.construxflow.entity.Material_request;

public interface MaterialRequestService {
    Material_request createMaterialRequest(MaterialRequestCreateDTO dto);
} 