
package com.example.construxflow.repository;

import com.example.construxflow.entity.InventoryRequest;
import com.example.construxflow.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRequestRepository extends JpaRepository<InventoryRequest, Long> {
    Long countByStatus(RequestStatus status);
    List<InventoryRequest> findTop5ByOrderByIdDesc();
}

