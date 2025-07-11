package com.example.construxflow.repository;

import com.example.construxflow.entity.Request_manintenance_materials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestMaintenanceMaterialsRepository extends JpaRepository<Request_manintenance_materials,String> {

    // Find all maintenance requests for a specific equipment
    List<Request_manintenance_materials> findByEquipmentId(String equipmentId);

    // Find by item ID
    List<Request_manintenance_materials> findByItemId(String itemId);

    // Find by urgency level
    List<Request_manintenance_materials> findByUrgency(String urgency);

    // Find by item name (case-insensitive)
    List<Request_manintenance_materials> findByItemNameContainingIgnoreCase(String itemName);

    // Find by equipment ID and urgency
    List<Request_manintenance_materials> findByEquipmentIdAndUrgency(String equipmentId, String urgency);

    // Custom query to find requests with quantity greater than specified value
    @Query("SELECT r FROM Request_manintenance_materials r WHERE r.quantity > :quantity")
    List<Request_manintenance_materials> findByQuantityGreaterThan(@Param("quantity") Number quantity);

    // Custom query to find requests by equipment type
    @Query("SELECT r FROM Request_manintenance_materials r JOIN r.equipment e WHERE e.equipmentType = :equipmentType")
    List<Request_manintenance_materials> findByEquipmentType(@Param("equipmentType") String equipmentType);

    // Custom query to find requests by equipment name
    @Query("SELECT r FROM Request_manintenance_materials r JOIN r.equipment e WHERE e.equipmentName = :equipmentName")
    List<Request_manintenance_materials> findByEquipmentName(@Param("equipmentName") String equipmentName);

    // Count requests by equipment ID
    long countByEquipmentId(String equipmentId);

    // Check if maintenance request exists for specific equipment and item
    boolean existsByEquipmentIdAndItemId(String equipmentId, String itemId);

    @Query("SELECT r.id FROM Request_manintenance_materials r WHERE r.id LIKE 'MAT-REQ-%' ORDER BY r.id DESC LIMIT 1")
    String findLastMaterialRequestId();

    @Query("SELECT r.itemId FROM Request_manintenance_materials r WHERE r.itemId LIKE 'ITEM-%' ORDER BY r.itemId DESC LIMIT 1")
    String findLastItemId();

}
