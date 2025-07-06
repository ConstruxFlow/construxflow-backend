package com.example.construxflow.service;


import com.example.construxflow.dto.SupplierDetailsDTO;
import com.example.construxflow.dto.SupplierRegReqDTO;
import com.example.construxflow.dto.SupplierRegResDTO;
import com.example.construxflow.entity.Supplier;

import java.util.List;

public interface SupplierService {
    
    public String getLatestSupplierId() throws Exception;

    public SupplierRegResDTO registerSuppler(SupplierRegReqDTO supplierRegReqDTO) throws Exception;

    public List<SupplierDetailsDTO> getAllSupplierDetails() throws Exception;

    public Supplier getSupplierDetails(String supplierId) throws Exception;

}
