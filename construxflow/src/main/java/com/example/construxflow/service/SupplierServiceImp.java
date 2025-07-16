package com.example.construxflow.service;

import com.example.construxflow.dto.SupplierDetailsDTO;
import com.example.construxflow.dto.SupplierRegReqDTO;
import com.example.construxflow.dto.SupplierRegResDTO;
import com.example.construxflow.entity.Supplier;
import com.example.construxflow.entity.UserDetails;
import com.example.construxflow.repository.SupplierRepository;
import com.google.api.gax.rpc.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.construxflow.entity.User_Role;
import com.example.construxflow.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImp implements SupplierService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public String getLatestSupplierId() throws Exception {

        return userRepository.findLatestSupplierId(User_Role.Supplier)
                .orElseThrow(() -> new Exception("No suppliers found"));
    }

    @Override
    public SupplierRegResDTO registerSuppler(SupplierRegReqDTO supplierRegReqDTO) throws Exception {
        try {
            // Get reference without fetching from DB
            Supplier supplier = supplierRepository.getReferenceById(supplierRegReqDTO.getSupplier_id());

            // Update the fields
            supplier.setName(supplierRegReqDTO.getName());
            supplier.setBusiness_Registration_Number(supplierRegReqDTO.getBusiness_registration_number());
            supplier.setCompany_name(supplierRegReqDTO.getCompany_name());

            // Save the updated supplier
            Supplier savedSupplier = supplierRepository.save(supplier);

            return SupplierRegResDTO.builder()
                    .supplier_id(savedSupplier.getSupplier_id())
                    .name(savedSupplier.getName())
                    .company_name(savedSupplier.getCompany_name())
                    .business_registration_number(savedSupplier.getBusiness_Registration_Number())
                    .build();
        } catch (EntityNotFoundException e) {
            throw new Exception("Supplier not found with ID: " + supplierRegReqDTO.getSupplier_id());
        }
    }

    @Override
    public List<SupplierDetailsDTO> getAllSupplierDetails() throws Exception {
        List<Supplier> suppliers=supplierRepository.findAll();

        return suppliers.stream()
                .map( this::convertToDTO )
                .collect(Collectors.toList());
    }

    @Override
    public Supplier getSupplierDetails(String supplierId) throws Exception {
        Optional<Supplier> supplier=supplierRepository.findById(supplierId);
        return supplier.get();
    }

    // Helper method to convert Entity to DTO
    private SupplierDetailsDTO convertToDTO(Supplier supplier) {
        UserDetails userDetails = supplier.getUserDetails();

        return SupplierDetailsDTO.builder()
                .supplier_id(supplier.getSupplier_id())
                .name(supplier.getName())
                .company_name(supplier.getCompany_name())
                .Business_Registration_Number(supplier.getBusiness_Registration_Number())
                .Delivery_Capabilities(supplier.getDelivery_Capabilities())
                .status(supplier.getStatus())
                .bank_name(supplier.getBank_name())
                .bank_account_name(supplier.getBank_account_name())
                .bank_account_number(supplier.getBank_account_number())
                .email(userDetails != null ? userDetails.getEmail() : null)
                .phone_number1(userDetails != null ? userDetails.getPhone_number1() : null)
                .phone_number2(userDetails != null ? userDetails.getPhone_number2() : null)
                .address(userDetails != null ? userDetails.getAddress() : null)
                .on_time_delivery_rate(supplier.getOn_time_delivery_rate())
                .quotation_acceptance_rate(supplier.getQuotation_acceptance_rate())
                .past_orders_completed(supplier.getPast_orders_completed())
                .avg_delay_days(supplier.getAvg_delay_days())
                .rating_by_site_manager(supplier.getRating_by_site_manager())
                .build();
    }

    @Override
    public Supplier updateSupplier(String supplierId, Supplier updatedSupplier) throws Exception {
        Optional<Supplier> optionalSupplier = supplierRepository.findById(supplierId);
        if (!optionalSupplier.isPresent()) {
            throw new Exception("Supplier not found with ID: " + supplierId);
        }
        Supplier existingSupplier = optionalSupplier.get();

        // Update fields - example for main fields, add more as needed
        existingSupplier.setCompany_name(updatedSupplier.getCompany_name());
        existingSupplier.setBusiness_Registration_Number(updatedSupplier.getBusiness_Registration_Number());
        existingSupplier.setDelivery_Capabilities(updatedSupplier.getDelivery_Capabilities());
        existingSupplier.setBank_name(updatedSupplier.getBank_name());
        existingSupplier.setBank_account_name(updatedSupplier.getBank_account_name());
        existingSupplier.setBank_account_number(updatedSupplier.getBank_account_number());

        // Update nested userDetails if provided
        if (updatedSupplier.getUserDetails() != null) {
            UserDetails existingUserDetails = existingSupplier.getUserDetails();
            UserDetails updatedUserDetails = updatedSupplier.getUserDetails();

            if (existingUserDetails != null) {
                existingUserDetails.setUser_name(updatedUserDetails.getUser_name());
                existingUserDetails.setEmail(updatedUserDetails.getEmail());
                existingUserDetails.setPhone_number1(updatedUserDetails.getPhone_number1());
                existingUserDetails.setAddress(updatedUserDetails.getAddress());
                // Save userDetails if needed depending on cascade settings
            } else {
                existingSupplier.setUserDetails(updatedUserDetails);
            }
        }

        return supplierRepository.save(existingSupplier);
    }


}
