package com.example.construxflow.service;

import com.example.construxflow.dto.SupplierRegReqDTO;
import com.example.construxflow.entity.Supplier;
import com.example.construxflow.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.construxflow.entity.User_Role;
import com.example.construxflow.repository.UserRepository;

import org.springframework.stereotype.Service;

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
    public String registerSuppler(SupplierRegReqDTO supplierRegReqDTO) throws Exception {
        try {
            // Get reference without fetching from DB
            Supplier supplier = supplierRepository.getReferenceById(supplierRegReqDTO.getSupplier_id());

            // Update the fields
            supplier.setName(supplierRegReqDTO.getName());
            supplier.setBusiness_Registration_Number(supplierRegReqDTO.getBusiness_registration_number());
            supplier.setCompany_name(supplierRegReqDTO.getCompany_name());

            // Save the updated supplier
            supplierRepository.save(supplier);

            return "Supplier updated successfully";
        } catch (EntityNotFoundException e) {
            throw new Exception("Supplier not found with ID: " + supplierRegReqDTO.getSupplier_id());
        }
    }

}
