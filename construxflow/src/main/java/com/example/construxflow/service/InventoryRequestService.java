package com.example.construxflow.service;

import com.example.construxflow.dto.InventoryRequestCreateDTO;
import com.example.construxflow.dto.InventoryRequestResponseDTO;
import com.example.construxflow.entity.InventoryRequest;
import com.example.construxflow.entity.RequestStatus;
import com.example.construxflow.repository.InventoryRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryRequestService {

    private final InventoryRequestRepository repository;

    // Create new request
    public InventoryRequestResponseDTO create(InventoryRequestCreateDTO dto) {
        InventoryRequest entity = InventoryRequest.builder()
                .siteName(dto.getSiteName())
                .requestedBy(dto.getRequestedBy())
                .requestedAt(dto.getRequestedAt())
                .itemName(dto.getItemName())
                .itemCategory(dto.getItemCategory())
                .quantity(dto.getQuantity())
                .priority(dto.getPriority())
                .status(RequestStatus.PENDING) // default
                .needFrom(dto.getNeedFrom())
                .needTo(dto.getNeedTo())
                .notes(dto.getNotes())
                .equipmentId(dto.getEquipmentId())
                .build();

        InventoryRequest saved = repository.save(entity);
        return toResponse(saved);
    }

    // List all requests
    public List<InventoryRequestResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    // Get one
    public InventoryRequestResponseDTO findById(Long id) {
        InventoryRequest entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));
        return toResponse(entity);
    }

    // Approve
    public InventoryRequestResponseDTO approve(Long id) {
        InventoryRequest entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));
        entity.setStatus(RequestStatus.APPROVED);
        return toResponse(repository.save(entity));
    }

    // Reject
    public InventoryRequestResponseDTO reject(Long id) {
        InventoryRequest entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));
        entity.setStatus(RequestStatus.REJECTED);
        return toResponse(repository.save(entity));
    }

    // Mark Partial (optional)
    public InventoryRequestResponseDTO markPartial(Long id) {
        InventoryRequest entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));
        entity.setStatus(RequestStatus.PARTIAL);
        return toResponse(repository.save(entity));
    }

    private InventoryRequestResponseDTO toResponse(InventoryRequest e) {
        return InventoryRequestResponseDTO.builder()
                .id(e.getId())
                .siteName(e.getSiteName())
                .requestedBy(e.getRequestedBy())
                .requestedAt(e.getRequestedAt())
                .itemName(e.getItemName())
                .itemCategory(e.getItemCategory())
                .quantity(e.getQuantity())
                .priority(e.getPriority())
                .status(e.getStatus())
                .needFrom(e.getNeedFrom())
                .needTo(e.getNeedTo())
                .notes(e.getNotes())
                .equipmentId(e.getEquipmentId())
                .build();
    }
}
