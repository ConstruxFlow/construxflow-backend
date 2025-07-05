package com.example.construxflow.service;

import com.example.construxflow.dto.UserRequestDetailsDTO;
import com.example.construxflow.dto.UserResponseDetailsDTO;
import com.example.construxflow.entity.*;
import com.example.construxflow.repository.ManagerRepository;
import com.example.construxflow.repository.SupplierRepository;
import com.example.construxflow.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @Transactional
    public UserResponseDetailsDTO createUser(UserRequestDetailsDTO dto) throws FirebaseAuthException {
        // Create Firebase user
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .setDisplayName(dto.getUser_name());

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        String firebaseUid = userRecord.getUid();

        // Get user role from DTO
        User_Role userRole = dto.getUserRole();

        // Set Firebase custom claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userRole != null ? userRole.name() : "DEFAULT_ROLE");
        FirebaseAuth.getInstance().setCustomUserClaims(firebaseUid, claims);

        // Create user entity
        UserDetails user = new UserDetails();
        user.setFirebaseUid(firebaseUid);
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setUser_name(dto.getUser_name());
        user.setUserRole(userRole);
        user.setPhone_number1(dto.getPhone_number1());
        user.setPhone_number2(dto.getPhone_number2());
        user.setAddress(dto.getAddress());
        user.setCreatedAt(LocalDateTime.now());

        // Handle Manager relationship
        if (dto.getManagerId() != null && !dto.getManagerId().isEmpty()) {
            Manager manager = managerRepository.findById(dto.getManagerId()).orElse(null);

            if (manager == null) {
                // Create specific manager subclass based on user role
                manager = createManagerByRole(userRole, dto.getManagerId(), dto.getUser_name());
                manager = managerRepository.save(manager);
            }

            // Establish bidirectional relationship
            manager.setUserDetails(user);
            user.setManager(manager);
        }

        // Handle Supplier relationship
        if (dto.getSupplierId() != null && !dto.getSupplierId().isEmpty()) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId()).orElse(null);

            if (supplier == null) {
                supplier = new Supplier();
                supplier.setSupplier_id(dto.getSupplierId());
                supplier.setName(dto.getUser_name());
                supplier.setCreatedAt(LocalDateTime.now());
                supplier = supplierRepository.save(supplier);
            }

            // Establish bidirectional relationship
            user.setSupplier(supplier);
            supplier.setUserDetails(user);
        }

        // Save user with relationships
        UserDetails savedUser = userRepository.save(user);

        // Build response DTO
        return UserResponseDetailsDTO.builder()
                .userId(savedUser.getUser_id())
                .firebaseUid(savedUser.getFirebaseUid())
                .userName(savedUser.getUser_name())
                .email(savedUser.getEmail())
                .phoneNumber1(savedUser.getPhone_number1())
                .phoneNumber2(savedUser.getPhone_number2())
                .address(savedUser.getAddress())
                .userRole(savedUser.getUserRole())
                .managerId(savedUser.getManager() != null ? savedUser.getManager().getManager_id() : null)
                .supplierId(savedUser.getSupplier() != null ? savedUser.getSupplier().getSupplier_id() : null)
                .build();
    }

    // Helper method to create specific Manager subclass
    private Manager createManagerByRole(User_Role role, String managerId, String managerName) {
        if (role == null) {
            throw new IllegalArgumentException("User role must be specified for manager creation");
        }

        switch (role) {
            case Site_Manager:
                Site_manager siteManager = new Site_manager();
                siteManager.setManager_id(managerId);
                siteManager.setManager_name(managerName);
                return siteManager;

            case Inventory_Manager:
                Inventory_manager inventoryManager = new Inventory_manager();
                inventoryManager.setManager_id(managerId);
                inventoryManager.setManager_name(managerName);
                return inventoryManager;

            case Purchasing_Manager:
                Purchasing_manager purchasingManager = new Purchasing_manager();
                purchasingManager.setManager_id(managerId);
                purchasingManager.setManager_name(managerName);
                return purchasingManager;

            case Finance_Officer:
                Finance_officer financeOfficer = new Finance_officer();
                financeOfficer.setManager_id(managerId);
                financeOfficer.setManager_name(managerName);
                return financeOfficer;

            case Maintenance_Head:
                Maintenance_head maintenanceHead = new Maintenance_head();
                maintenanceHead.setManager_id(managerId);
                maintenanceHead.setManager_name(managerName);
                return maintenanceHead;

            default:
                throw new IllegalArgumentException("Unsupported manager role: " + role);
        }
    }
}
