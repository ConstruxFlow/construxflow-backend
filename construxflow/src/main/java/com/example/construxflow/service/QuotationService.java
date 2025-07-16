package com.example.construxflow.service;

import com.example.construxflow.entity.Quotation;
import com.example.construxflow.entity.QuotationItem;
import com.example.construxflow.entity.QuotationDeliveryInfo;
import com.example.construxflow.entity.QuotationAttachment;
import com.example.construxflow.repository.QuotationRepository;
import com.example.construxflow.dto.QuotationResponseDTO;
import com.example.construxflow.mappers.QuotationMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final QuotationMapper quotationMapper;

    @Autowired
    public QuotationService(QuotationRepository quotationRepository, QuotationMapper quotationMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationMapper = quotationMapper;
    }

    // Create a new quotation
    @Transactional
    public QuotationResponseDTO createQuotation(QuotationResponseDTO dto) {
        Quotation quotation = quotationMapper.toEntity(dto);
        Quotation saved = quotationRepository.save(quotation);
        return quotationMapper.toDto(saved);
    }

    // Get all quotations
    public List<QuotationResponseDTO> getAllQuotations() {
        List<Quotation> quotations = quotationRepository.findAll();
        return quotations.stream()
                .map(quotationMapper::toDto)
                .toList();
    }

    // Get a quotation by ID
    public QuotationResponseDTO getQuotationById(Long id) {
        Optional<Quotation> optionalQuotation = quotationRepository.findById(id);
        return optionalQuotation.map(quotationMapper::toDto).orElse(null);
    }

    // Update a quotation
    @Transactional
    public QuotationResponseDTO updateQuotation(Long id, QuotationResponseDTO dto) {
        Optional<Quotation> optionalQuotation = quotationRepository.findById(id);
        if (optionalQuotation.isPresent()) {
            Quotation existing = optionalQuotation.get();
            Quotation updated = quotationMapper.toEntity(dto);
            updated.setId(existing.getId());
            Quotation saved = quotationRepository.save(updated);
            return quotationMapper.toDto(saved);
        }
        return null;
    }

    // Delete a quotation
    @Transactional
    public boolean deleteQuotation(Long id) {
        if (quotationRepository.existsById(id)) {
            quotationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Update status only (PATCH)
    @Transactional
    public QuotationResponseDTO updateStatus(Long id, String status) {
        Optional<Quotation> optionalQuotation = quotationRepository.findById(id);
        if (optionalQuotation.isPresent()) {
            Quotation quotation = optionalQuotation.get();
            quotation.setStatus(status);
            Quotation saved = quotationRepository.save(quotation);
            return quotationMapper.toDto(saved);
        }
        return null;
    }
}

