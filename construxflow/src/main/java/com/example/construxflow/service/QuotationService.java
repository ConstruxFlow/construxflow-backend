package com.example.construxflow.service;

import com.example.construxflow.entity.*;
import com.example.construxflow.repository.*;
import com.example.construxflow.dto.QuotationResponseDTO;
import com.example.construxflow.mappers.QuotationMapper;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final QuotationItemRepository quotationItemRepository;
    private final QuotationDeliveryInfoRepository quotationDeliveryInfoRepository;
    private final QuotationAttachmentRepository quotationAttachmentRepository;
    private final QuotationMapper quotationMapper;
    private final QuotationReqRepository quotationRequestRepository;

    @Autowired
    public QuotationService(
            QuotationRepository quotationRepository,
            QuotationItemRepository quotationItemRepository,
            QuotationDeliveryInfoRepository quotationDeliveryInfoRepository,
            QuotationAttachmentRepository quotationAttachmentRepository,
            QuotationMapper quotationMapper,
            QuotationReqRepository quotationRequestRepository
    ) {
        this.quotationRepository = quotationRepository;
        this.quotationItemRepository = quotationItemRepository;
        this.quotationDeliveryInfoRepository = quotationDeliveryInfoRepository;
        this.quotationAttachmentRepository = quotationAttachmentRepository;
        this.quotationMapper = quotationMapper;
        this.quotationRequestRepository = quotationRequestRepository;
    }

    // Create a new quotation
    @Transactional
    public QuotationResponseDTO createQuotation(QuotationResponseDTO dto) {
        Quotation quotation = quotationMapper.toEntity(dto);

        Quotation_request quotationRequest = quotationRequestRepository
                .findById(dto.getQuotationRequestId())
                .orElseThrow(() -> new EntityNotFoundException("Quotation Request not found"));
        quotation.setQuotationRequest(quotationRequest);

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

    // Update entire quotation
    @Transactional
    public QuotationResponseDTO updateQuotation(Long id, QuotationResponseDTO updatedQuotation) {
        Optional<Quotation> existingQuotationOpt = quotationRepository.findById(id);

        if (existingQuotationOpt.isPresent()) {
            Quotation existingQuotation = existingQuotationOpt.get();


            Quotation_request existingQuotationRequest = existingQuotation.getQuotationRequest();
            Supplier existingSupplier = existingQuotation.getSupplier();
            LocalDateTime existingCreatedAt = existingQuotation.getCreatedAt();


//            System.out.println("=== BACKEND UPDATE - BEFORE ===");
//            System.out.println("Existing Quotation Request ID: " +
//                    (existingQuotationRequest != null ? existingQuotationRequest.getId() : "NULL"));
//            System.out.println("Existing Supplier ID: " +
//                    (existingSupplier != null ? existingSupplier.getSupplier_id() : "NULL"));
//            System.out.println("Received DTO Quotation Request ID: " + updatedQuotation.getQuotationRequestId());
//            System.out.println("Received DTO Supplier ID: " + updatedQuotation.getSupplierId());

            // Update basic fields
            existingQuotation.setAdvancedPayment(updatedQuotation.getAdvancedPayment());
            //existingQuotation.setAdvancedPaymentPercentage(updatedQuotation.getAdvancedPaymentPercentage());  // ✅ Uncommented
            existingQuotation.setPaymentTerms(updatedQuotation.getPaymentTerms());
            existingQuotation.setNotes(updatedQuotation.getNotes());
            existingQuotation.setTotalAmount(updatedQuotation.getTotalAmount());
            existingQuotation.setStatus(updatedQuotation.getStatus());


            existingQuotation.setQuotationRequest(existingQuotationRequest);
            existingQuotation.setSupplier(existingSupplier);
            existingQuotation.setCreatedAt(existingCreatedAt);

            // Delete existing items from database BEFORE clearing
            if (existingQuotation.getItems() != null && !existingQuotation.getItems().isEmpty()) {
                List<Long> itemIds = existingQuotation.getItems().stream()
                        .map(QuotationItem::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                if (!itemIds.isEmpty()) {
                    quotationItemRepository.deleteAllById(itemIds);
                }
                existingQuotation.getItems().clear();
            }

            // Delete existing delivery info from database BEFORE clearing
            if (existingQuotation.getDeliveryInfos() != null && !existingQuotation.getDeliveryInfos().isEmpty()) {
                List<Long> deliveryIds = existingQuotation.getDeliveryInfos().stream()
                        .map(QuotationDeliveryInfo::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                if (!deliveryIds.isEmpty()) {
                    quotationDeliveryInfoRepository.deleteAllById(deliveryIds);
                }
                existingQuotation.getDeliveryInfos().clear();
            }

            // Delete existing attachments from database BEFORE clearing
            if (existingQuotation.getAttachments() != null && !existingQuotation.getAttachments().isEmpty()) {
                List<Long> attachmentIds = existingQuotation.getAttachments().stream()
                        .map(QuotationAttachment::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                if (!attachmentIds.isEmpty()) {
                    quotationAttachmentRepository.deleteAllById(attachmentIds);
                }
                existingQuotation.getAttachments().clear();
            }

            // Flush to ensure deletions are committed
            quotationRepository.flush();

            // Add new items with proper relationships
            if (updatedQuotation.getItems() != null) {
                updatedQuotation.getItems().forEach(item -> {
                    QuotationItem newItem = new QuotationItem();
                    newItem.setQuotation(existingQuotation);

                    // Set material reference
                    if (item.getMaterial() != null) {
                        Materials material = new Materials();
                        material.setMaterial_id(item.getMaterial().getMaterialId());
                        newItem.setMaterial(material);
                    }

                    newItem.setQuantity(item.getQuantity());
                    newItem.setUnit(item.getUnit());
                    newItem.setUnitPrice(item.getUnitPrice());
                    newItem.setTotalPrice(item.getTotalPrice());
                    newItem.setId(null); // Ensure new ID generation

                    existingQuotation.getItems().add(newItem);
                });
            }

            // Add new delivery info with proper relationships
            if (updatedQuotation.getDeliveryInfos() != null) {
                updatedQuotation.getDeliveryInfos().forEach(delivery -> {
                    QuotationDeliveryInfo newDelivery = new QuotationDeliveryInfo();
                    newDelivery.setQuotation(existingQuotation);
                    newDelivery.setDeliveryDate(delivery.getDeliveryDate());
                    newDelivery.setLocation(delivery.getLocation());
                    newDelivery.setShippingCost(delivery.getShippingCost());
                    newDelivery.setId(null); // Ensure new ID generation

                    existingQuotation.getDeliveryInfos().add(newDelivery);
                });
            }

            // Add new attachments with proper relationships
            if (updatedQuotation.getAttachments() != null) {
                updatedQuotation.getAttachments().forEach(attachment -> {
                    QuotationAttachment newAttachment = new QuotationAttachment();
                    newAttachment.setQuotation(existingQuotation);
                    newAttachment.setFileName(attachment.getFileName());
                    newAttachment.setFileType(attachment.getFileType());
                    newAttachment.setFileUrl(attachment.getFileUrl());
                    newAttachment.setId(null); // Ensure new ID generation

                    existingQuotation.getAttachments().add(newAttachment);
                });
            }

            // Save the updated quotation
            Quotation savedQuotation = quotationRepository.save(existingQuotation);


//            System.out.println("=== BACKEND UPDATE - AFTER SAVE ===");
//            System.out.println("Saved Quotation Request ID: " +
//                    (savedQuotation.getQuotationRequest() != null ? savedQuotation.getQuotationRequest().getId() : "NULL"));
//            System.out.println("Saved Supplier ID: " +
//                    (savedQuotation.getSupplier() != null ? savedQuotation.getSupplier().getSupplier_id() : "NULL"));

            // Force loading of lazy collections
            forceLoadQuotationCollections(savedQuotation);

            return quotationMapper.toDto(savedQuotation);
        } else {
            throw new RuntimeException("Quotation not found with ID: " + id);
        }
    }

    // Helper method to force loading of lazy collections
    private void forceLoadQuotationCollections(Quotation quotation) {
        if (quotation.getItems() != null) {
            quotation.getItems().forEach(item -> {
                if (item.getMaterial() != null) {
                    item.getMaterial().getMaterialName();
                    item.getMaterial().getMaterialType();
                    item.getMaterial().getUnitOfMeasurement();
                }
            });
        }

        if (quotation.getDeliveryInfos() != null) {
            quotation.getDeliveryInfos().size();
        }

        if (quotation.getAttachments() != null) {
            quotation.getAttachments().size();
        }
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

    public List<QuotationResponseDTO> getQuotationsByRequestIdWithValidation(Long quotationRequestId) {
        // Validate if quotation request exists
        if (!quotationRequestRepository.existsById(quotationRequestId)) {
            throw new EntityNotFoundException("Quotation Request with ID " + quotationRequestId + " not found");
        }

        List<Quotation> quotations = quotationRepository.findByQuotationRequestIdCustom(quotationRequestId);
        return quotations.stream()
                .map(quotationMapper::toDto)
                .toList();
    }
}
