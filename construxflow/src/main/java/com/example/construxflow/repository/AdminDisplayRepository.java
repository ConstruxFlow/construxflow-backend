package com.example.construxflow.repository;

import com.example.construxflow.entity.Team_Member;
import com.example.construxflow.entity.Team_Member.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDisplayRepository extends JpaRepository<Team_Member, String> {
    long countByAvailabilityStatus(AvailabilityStatus status);
}