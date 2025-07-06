package com.example.construxflow.repository;

import com.example.construxflow.entity.Quotation_request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationReqRepository extends JpaRepository<Quotation_request,Long> {
}
