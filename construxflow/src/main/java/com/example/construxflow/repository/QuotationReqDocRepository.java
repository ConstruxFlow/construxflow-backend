package com.example.construxflow.repository;

import com.example.construxflow.entity.Quotation_req_delivery;
import com.example.construxflow.entity.Quotation_req_doc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationReqDocRepository extends JpaRepository<Quotation_req_doc,Long> {
}
