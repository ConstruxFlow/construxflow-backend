package com.example.construxflow.mappers;

import com.example.construxflow.dto.*;
import com.example.construxflow.entity.*;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

@Component
public class QuotationMapper {

    // Convert Entity to DTO
    public QuotationResponseDTO toDto(Quotation quotation) {
        if (quotation == null) {
            return null;
        }

        QuotationResponseDTO dto = new QuotationResponseDTO();

        dto.setId(quotation.getId());
        dto.setQuotationRequestId(quotation.getQuotationRequest() != null ? quotation.getQuotationRequest().getId() : null);
        dto.setSupplierId(quotation.getSupplier() != null ? quotation.getSupplier().getSupplierId() : null);
        dto.setCreatedAt(quotation.getCreatedAt());
        dto.setAdvancedPayment(quotation.getAdvancedPayment());
        dto.setPaymentTerms(quotation.getPaymentTerms());
        dto.setNotes(quotation.getNotes());
        dto.setTotalAmount(quotation.getTotalAmount());
        dto.setStatus(quotation.getStatus());
//        dto.setQuotationRequestId(quotation.getQuotationRequest() != null ? quotation.getQuotationRequest().getId() : null);

        // Map Quotation Items
        if (quotation.getItems() != null) {
            List<QuotationItemDTO> itemDTOs = quotation.getItems().stream()
                    .map(item -> {
                        QuotationItemDTO itemDTO = new QuotationItemDTO();
                        itemDTO.setId(item.getId());
                        if (item.getMaterial() != null) {
                            MaterialDTO materialDTO = new MaterialDTO();
                            materialDTO.setMaterialId(item.getMaterial().getMaterial_id());
                            materialDTO.setMaterialName(item.getMaterial().getMaterialName());
                            materialDTO.setMaterialType(item.getMaterial().getMaterialType());
                            materialDTO.setUnitOfMeasurement(item.getMaterial().getUnitOfMeasurement());
                            itemDTO.setMaterial(materialDTO);
                        }
                        itemDTO.setQuantity(item.getQuantity());
                        itemDTO.setUnit(item.getUnit());
                        itemDTO.setUnitPrice(item.getUnitPrice());
                        itemDTO.setTotalPrice(item.getTotalPrice());
                        return itemDTO;
                    })
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        } else {
            dto.setItems(new ArrayList<>());
        }

        // Map Delivery Infos
        if (quotation.getDeliveryInfos() != null) {
            List<QuotationDeliveryInfoDTO> deliveryDTOs = quotation.getDeliveryInfos().stream()
                    .map(delivery -> {
                        QuotationDeliveryInfoDTO deliveryDTO = new QuotationDeliveryInfoDTO();
                        deliveryDTO.setId(delivery.getId());
                        deliveryDTO.setDeliveryDate(delivery.getDeliveryDate());
                        deliveryDTO.setLocation(delivery.getLocation());
                        deliveryDTO.setShippingCost(delivery.getShippingCost());
                        return deliveryDTO;
                    })
                    .collect(Collectors.toList());
            dto.setDeliveryInfos(deliveryDTOs);
        } else {
            dto.setDeliveryInfos(new ArrayList<>());
        }

        // Map Attachments
        if (quotation.getAttachments() != null) {
            List<QuotationAttachmentDTO> attachmentDTOs = quotation.getAttachments().stream()
                    .map(att -> {
                        QuotationAttachmentDTO attachmentDTO = new QuotationAttachmentDTO();
                        attachmentDTO.setId(att.getId());
                        attachmentDTO.setFileName(att.getFileName());
                        attachmentDTO.setFileType(att.getFileType());
                        attachmentDTO.setFileUrl(att.getFileUrl());
                        return attachmentDTO;
                    })
                    .collect(Collectors.toList());
            dto.setAttachments(attachmentDTOs);
        } else {
            dto.setAttachments(new ArrayList<>());
        }

        return dto;
    }

    // Convert DTO to Entity
    public Quotation toEntity(QuotationResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        Quotation quotation = new Quotation();

        quotation.setId(dto.getId());
        // Note: Setting quotationRequest and supplier entities should be handled outside or by service layer
        // Here, only IDs are available, so these references need to be fetched and set externally

        quotation.setCreatedAt(LocalDateTime.now());
        quotation.setAdvancedPayment(dto.getAdvancedPayment());
        quotation.setPaymentTerms(dto.getPaymentTerms());
        quotation.setNotes(dto.getNotes());
        quotation.setTotalAmount(dto.getTotalAmount());
        quotation.setStatus(dto.getStatus());
//        quotation.setQuotationRequest();
//        quotation.set

        if (dto.getSupplierId() != null) {
            Supplier supplier = new Supplier();
            supplier.setSupplier_id(dto.getSupplierId());
            quotation.setSupplier(supplier);
        } else {
            quotation.setSupplier(null);
        }

        // Map Quotation Items
        if (dto.getItems() != null) {
            List<QuotationItem> items = dto.getItems().stream()
                    .map(itemDTO -> {
                        QuotationItem item = new QuotationItem();
                        item.setId(itemDTO.getId());
                        if (itemDTO.getMaterial() != null) {
                            Materials material = new Materials();
                            material.setMaterial_id(itemDTO.getMaterial().getMaterialId());

                            item.setMaterial(material);
                        }
                        item.setQuantity(itemDTO.getQuantity());
                        item.setUnit(itemDTO.getUnit());
                        item.setUnitPrice(itemDTO.getUnitPrice());
                        item.setTotalPrice(itemDTO.getTotalPrice());
                        item.setQuotation(quotation); // set back-reference
                        return item;
                    })
                    .collect(Collectors.toList());
            quotation.setItems(items);
        } else {
            quotation.setItems(new ArrayList<>());
        }

        // Map Delivery Infos
        if (dto.getDeliveryInfos() != null) {
            List<QuotationDeliveryInfo> deliveryInfos = dto.getDeliveryInfos().stream()
                    .map(deliveryDTO -> {
                        QuotationDeliveryInfo delivery = new QuotationDeliveryInfo();
                        delivery.setId(deliveryDTO.getId());
                        delivery.setDeliveryDate(deliveryDTO.getDeliveryDate());
                        delivery.setLocation(deliveryDTO.getLocation());
                        delivery.setShippingCost(deliveryDTO.getShippingCost());
                        delivery.setQuotation(quotation); // set back-reference
                        return delivery;
                    })
                    .collect(Collectors.toList());
            quotation.setDeliveryInfos(deliveryInfos);
        } else {
            quotation.setDeliveryInfos(new ArrayList<>());
        }

        // Map Attachments
        if (dto.getAttachments() != null) {
            List<QuotationAttachment> attachments = dto.getAttachments().stream()
                    .map(attDTO -> {
                        QuotationAttachment attachment = new QuotationAttachment();
                        attachment.setId(attDTO.getId());
                        attachment.setFileName(attDTO.getFileName());
                        attachment.setFileType(attDTO.getFileType());
                        attachment.setFileUrl(attDTO.getFileUrl());
                        attachment.setQuotation(quotation); // set back-reference
                        return attachment;
                    })
                    .collect(Collectors.toList());
            quotation.setAttachments(attachments);
        } else {
            quotation.setAttachments(new ArrayList<>());
        }

        return quotation;
    }
}

