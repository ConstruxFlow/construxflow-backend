package com.example.construxflow.service;

import com.example.construxflow.dto.AdminDisplayDTO;
import com.example.construxflow.entity.Team_Member;
import com.example.construxflow.repository.AdminDisplayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminDisplayService {

    private final AdminDisplayRepository adminDisplayRepository;

    @Autowired
    public AdminDisplayService(AdminDisplayRepository adminDisplayRepository) {
        this.adminDisplayRepository = adminDisplayRepository;
    }

    public AdminDisplayDTO getDashboardStats() {
        long totalUsers = adminDisplayRepository.count();
        long activeUsers = adminDisplayRepository.countByAvailabilityStatus(Team_Member.AvailabilityStatus.ONTASK);
        long availableUsers = adminDisplayRepository.countByAvailabilityStatus(Team_Member.AvailabilityStatus.AVAILABLE);
        long unavailableUsers = adminDisplayRepository.countByAvailabilityStatus(Team_Member.AvailabilityStatus.UNAVAILABLE);

        return new AdminDisplayDTO(totalUsers, activeUsers, availableUsers, unavailableUsers);
    }
}