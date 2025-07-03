package com.example.construxflow.service;


import com.example.construxflow.dto.SupplierRegReqDTO;

public interface SupplierService {
    
    public String getLatestSupplierId() throws Exception;

    public String registerSuppler(SupplierRegReqDTO supplierRegReqDTO) throws Exception;

}
