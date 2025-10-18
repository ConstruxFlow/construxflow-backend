package com.example.construxflow.repository;

import com.example.construxflow.entity.InventoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRequestRepository extends JpaRepository<InventoryRequest, Long> {
}
