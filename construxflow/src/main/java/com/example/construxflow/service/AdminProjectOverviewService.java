// src/main/java/com/example/construxflow/service/ProjectOverviewService.java
package com.example.construxflow.service;

import com.example.construxflow.dto.AdminProjectOverviewDTO;
import com.example.construxflow.repository.AdminProjectOverviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProjectOverviewService {

    private final AdminProjectOverviewRepository repo;

    public List<AdminProjectOverviewDTO> getOverview() {
        return repo.findProjectOverview();
    }
}