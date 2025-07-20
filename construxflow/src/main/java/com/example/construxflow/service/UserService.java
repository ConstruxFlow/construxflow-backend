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
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private EmailService emailService;

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

        try {
            String verificationLink = firebaseService.generateEmailVerificationLink(dto.getEmail());
            // Create professional email content
            String emailSubject = "Welcome! Complete Your Account Setup";

            String emailBody = createProfessionalWelcomeEmail(
                    dto.getEmail(),
                    dto.getPassword(),
                    verificationLink
            );

            emailService.sendHtmlEmail(dto.getEmail(), emailSubject, emailBody);

        } catch (Exception e) {
            // Optionally log or handle email sending failure
            e.printStackTrace();
        }

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

    public Optional<UserResponseDetailsDTO> getUserByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid)
                .map(user -> UserResponseDetailsDTO.builder()
                        .userId(user.getUser_id())
                        .firebaseUid(user.getFirebaseUid())
                        .userName(user.getUser_name())
                        .email(user.getEmail())
                        .phoneNumber1(user.getPhone_number1())
                        .phoneNumber2(user.getPhone_number2())
                        .address(user.getAddress())
                        .userRole(user.getUserRole())
                        .managerId(user.getManager() != null ? user.getManager().getManager_id() : null)
                        .supplierId(user.getSupplier() != null ? user.getSupplier().getSupplier_id() : null)
                        .build()
                );
    }

    private String createProfessionalWelcomeEmail(String email, String password, String verificationLink) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background-color: #f8f9fa; padding: 20px; text-align: center; border-radius: 5px; }
                .content { padding: 20px 0; }
                .credentials-box { 
                    background-color: #e9ecef; 
                    padding: 15px; 
                    border-radius: 5px; 
                    margin: 20px 0; 
                    border-left: 4px solid #007bff;
                }
                .verify-button { 
                    display: inline-block; 
                    background-color: #007bff; 
                    color: white; 
                    padding: 12px 30px; 
                    text-decoration: none; 
                    border-radius: 5px; 
                    margin: 20px 0;
                }
                .footer { 
                    margin-top: 30px; 
                    padding-top: 20px; 
                    border-top: 1px solid #dee2e6; 
                    font-size: 12px; 
                    color: #6c757d; 
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Welcome to ConstruxFlow!</h1>
                </div>
                
                <div class="content">
                    <p>Dear User,</p>
                    
                    <p>Thank you for registering with us. Your account has been successfully created and is ready to use.</p>
                    
                    <div class="credentials-box">
                        <h3>Your Login Credentials:</h3>
                        <p><strong>Username (Email):</strong> %s</p>
                        <p><strong>Password:</strong> %s</p>
                    </div>
                    
                    <p><strong>Important:</strong> Please verify your email address to activate your account and ensure you receive important notifications.</p>
                    
                    <div style="text-align: center;">
                        <a href="%s" class="verify-button">Verify Email Address</a>
                    </div>
                    
                    <p>If the button above doesn't work, copy and paste this link into your browser:</p>
                    <p style="word-break: break-all; color: #007bff;">%s</p>
                    
                    <p><strong>Security Note:</strong> Please change your password after your first login for enhanced security.</p>
                </div>
                
                <div class="footer">
                    <p>If you didn't create this account, please ignore this email or contact our support team.</p>
                    <p>This is an automated message, please do not reply to this email.</p>
                    <p>&copy; 2025 [Your Company Name]. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(email, password, verificationLink, verificationLink);
    }


}
