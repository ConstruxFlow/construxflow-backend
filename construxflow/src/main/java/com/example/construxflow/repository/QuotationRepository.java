package com.example.construxflow.repository;

import com.example.construxflow.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation,Long> {

    // Alternative using custom query (if needed)
    @Query("SELECT q FROM Quotation q WHERE q.quotationRequest.id = :quotationRequestId")
    List<Quotation> findByQuotationRequestIdCustom(@Param("quotationRequestId") Long quotationRequestId);
}
