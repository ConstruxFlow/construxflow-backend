package com.example.construxflow.service;

import com.example.construxflow.dto.EquipmentDTO;
import com.example.construxflow.dto.EquipmentListItemDTO;
import com.example.construxflow.dto.EquipmentStatsDTO;
import com.example.construxflow.dto.PagedResponse;
import com.example.construxflow.entity.Equipment;
import com.example.construxflow.entity.EquipmentStatus;
import com.example.construxflow.repository.EquipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    // ---------- NEW: list for scheduling UI (kept as you wrote; DB fetch + optional in-memory status filter) ----------
    public List<EquipmentListItemDTO> listForScheduling(String search, EquipmentStatus status) {
        List<Equipment> base = (search != null && !search.isBlank())
                ? equipmentRepository.findByNameContainingIgnoreCase(search)  // Make sure this exists in your repository
                : equipmentRepository.findAll();

        if (status != null) {
            base = base.stream().filter(e -> e.getStatus() == status).toList();
        }

        return base.stream().map(this::toListItemDTO).toList();
    }

    private EquipmentListItemDTO toListItemDTO(Equipment e) {
        String display = switch (e.getStatus()) {
            case AVAILABLE -> "Available";
            case UNDER_MAINTENANCE -> "Under Maintenance";
            case ON_A_SITE -> "In Use";
        };

        String buttonText;
        String btnClass;
        switch (e.getStatus()) {
            case AVAILABLE -> {
                buttonText = "Schedule";
                btnClass = "bg-web_yellow hover:bg-web_yellow/80 text-main_dark";
            }
            case ON_A_SITE -> {
                buttonText = "View Schedule";
                btnClass = "bg-deep_green hover:bg-deep_green/80 text-white";
            }
            default -> {
                buttonText = "View Details";
                btnClass = "bg-light_gray hover:bg-light_gray/80 text-main_dark";
            }
        }

        return EquipmentListItemDTO.builder()
                .id(e.getId())
                .name(safe(e.getName()))
                .status(e.getStatus())
                .displayStatus(display)
                .location(safe(e.getLocation()))
                .lastMaintenance(safe(e.getLastMaintenance()))
                .utilization(calcUtilization(e)) // placeholder
                .buttonText(buttonText)
                .buttonColorClass(btnClass)
                .build();
    }

    private String calcUtilization(Equipment e) {
        // Placeholder until you wire real usage metrics
        if (e.getStatus() == EquipmentStatus.AVAILABLE) return "0%";
        if (e.getStatus() == EquipmentStatus.ON_A_SITE) return "85%";
        return "N/A";
    }

    private String safe(String s) { return (s == null || s.isBlank()) ? "N/A" : s; }

    // ---------- your existing CRUD (kept exactly; added @Transactional for correctness) ----------
    @Transactional
    public Equipment addEquipment(EquipmentDTO dto) {
        Equipment equipment = Equipment.builder()
                .type(dto.getType())
                .name(dto.getName())
                .category(dto.getCategory())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .serialNumber(dto.getSerialNumber())
                .quantity(dto.getQuantity())
                .condition(dto.getCondition())
                .purchaseDate(dto.getPurchaseDate())
                .purchaseSource(dto.getPurchaseSource())
                .purchaseCost(dto.getPurchaseCost())
                .location(dto.getLocation())
                .status(dto.getStatus())
                .nextMaintenance(dto.getNextMaintenance())
                .lastMaintenance(dto.getLastMaintenance())
                .notes(dto.getNotes())
                .build();

        return equipmentRepository.save(equipment);
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id).orElse(null);
    }

    @Transactional
    public Equipment updateEquipment(Long id, EquipmentDTO dto) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found: " + id));

        existing.setType(dto.getType());
        existing.setName(dto.getName());
        existing.setCategory(dto.getCategory());
        existing.setBrand(dto.getBrand());
        existing.setModel(dto.getModel());
        existing.setSerialNumber(dto.getSerialNumber());
        existing.setQuantity(dto.getQuantity());
        existing.setCondition(dto.getCondition());
        existing.setPurchaseDate(dto.getPurchaseDate());
        existing.setPurchaseSource(dto.getPurchaseSource());
        existing.setPurchaseCost(dto.getPurchaseCost());
        existing.setLocation(dto.getLocation());
        existing.setStatus(dto.getStatus());
        existing.setNextMaintenance(dto.getNextMaintenance());
        existing.setLastMaintenance(dto.getLastMaintenance());
        existing.setNotes(dto.getNotes());

        return equipmentRepository.save(existing);
    }

    @Transactional
    public Equipment updateStatus(Long id, EquipmentStatus status) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found: " + id));
        existing.setStatus(status);
        return equipmentRepository.save(existing);
    }

    @Transactional
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    // ---------- search + pagination (kept as you wrote; returns entities) ----------
    public PagedResponse<Equipment> search(
            String search, String statusUi, int page, int size, String sortBy, String sortDir) {

        if (!List.of("name", "status", "location", "id", "brand", "model").contains(sortBy)) {
            sortBy = "name";
        }
        Sort.Direction dir = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        Specification<Equipment> spec = buildSpec(search, statusUi);
        Page<Equipment> result = equipmentRepository.findAll(spec, pageable);

        return PagedResponse.<Equipment>builder()
                .content(result.getContent())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .sortBy(sortBy)
                .sortDir(dir.name().toLowerCase())
                .build();
    }

    // ---------- stats (kept; powers KPI cards) ----------
    public EquipmentStatsDTO stats() {
        long total = equipmentRepository.count();
        long available = equipmentRepository.countByStatus(EquipmentStatus.AVAILABLE);
        long onASite = equipmentRepository.countByStatus(EquipmentStatus.ON_A_SITE);
        long underMaint = equipmentRepository.countByStatus(EquipmentStatus.UNDER_MAINTENANCE);

        return EquipmentStatsDTO.builder()
                .total(total)
                .available(available)
                .onASite(onASite)
                .underMaintenance(underMaint)
                .build();
    }

    // ---------- NEW: paged list tailored for the UI (DTOs instead of entities) ----------
    public PagedResponse<EquipmentListItemDTO> searchListItems(
            String search, String statusUi, int page, int size, String sortBy, String sortDir) {

        if (!List.of("name", "status", "location", "id", "brand", "model").contains(sortBy)) {
            sortBy = "name";
        }
        Sort.Direction dir = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

        Specification<Equipment> spec = buildSpec(search, statusUi);
        Page<Equipment> result = equipmentRepository.findAll(spec, pageable);

        List<EquipmentListItemDTO> rows = result.getContent().stream()
                .map(this::toListItemDTO)
                .toList();

        return PagedResponse.<EquipmentListItemDTO>builder()
                .content(rows)
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .sortBy(sortBy)
                .sortDir(dir.name().toLowerCase())
                .build();
    }

    // ---------- helpers ----------
    private Specification<Equipment> buildSpec(String search, String statusUi) {
        return (root, query, cb) -> {
            List<Predicate> ands = new ArrayList<>();

            if (statusUi != null && !statusUi.isBlank() && !"All".equalsIgnoreCase(statusUi)) {
                EquipmentStatus status = mapUiStatusToEnum(statusUi);
                if (status != null) {
                    ands.add(cb.equal(root.get("status"), status));
                }
            }

            if (search != null && !search.isBlank()) {
                String like = "%" + search.toLowerCase() + "%";
                Predicate byName = cb.like(cb.lower(root.get("name")), like);
                Predicate byLoc  = cb.like(cb.lower(root.get("location")), like);
                Predicate byBr   = cb.like(cb.lower(root.get("brand")), like);
                Predicate byMd   = cb.like(cb.lower(root.get("model")), like);
                Predicate bySn   = cb.like(cb.lower(root.get("serialNumber")), like);
                ands.add(cb.or(byName, byLoc, byBr, byMd, bySn));
            }

            return ands.isEmpty() ? cb.conjunction() : cb.and(ands.toArray(new Predicate[0]));
        };
    }

    private EquipmentStatus mapUiStatusToEnum(String ui) {
        if (ui == null) return null;
        String v = ui.trim().toLowerCase();
        return switch (v) {
            case "available" -> EquipmentStatus.AVAILABLE;
            case "under maintenance" -> EquipmentStatus.UNDER_MAINTENANCE;
            case "in use", "on a site", "on_a_site", "on-site" -> EquipmentStatus.ON_A_SITE;
            default -> null; // ignore unknown
        };
    }

    // ---------- Optional helpers if you need strict 404s elsewhere ----------
    private EntityNotFoundException notFound(Long id) {
        return new EntityNotFoundException("Equipment not found: " + id);
    }

    @Transactional
    public Equipment updateEquipmentStock(Long id, Integer newQuantity) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with id: " + id));

        equipment.setQuantity(newQuantity);
        return equipmentRepository.save(equipment);
    }

    // Search equipment by name
    public List<Equipment> searchEquipmentByName(String name) {
        return equipmentRepository.findByNameContainingIgnoreCase(name);
    }
}
