package com.example.construxflow.repository;

import com.example.construxflow.entity.QuotationDeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationDeliveryInfoRepository extends JpaRepository<QuotationDeliveryInfo, Long> {
}

