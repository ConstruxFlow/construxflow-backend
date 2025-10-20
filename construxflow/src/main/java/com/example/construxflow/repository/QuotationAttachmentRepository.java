package com.example.construxflow.repository;

import com.example.construxflow.entity.QuotationAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationAttachmentRepository extends JpaRepository<QuotationAttachment, Long> {
}

