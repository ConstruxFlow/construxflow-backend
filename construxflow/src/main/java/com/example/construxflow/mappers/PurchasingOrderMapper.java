package com.example.construxflow.mappers;

import com.example.construxflow.entity.*;
import com.example.construxflow.dto.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchasingOrderMapper {

    public PurchasingOrderResponseDTO toResponseDTO(PurchasingOrder entity) {
        if (entity == null) return null;

        PurchasingOrderResponseDTO dto = new PurchasingOrderResponseDTO();
        dto.setPoId(entity.getPo_id());
        dto.setPonumber(entity.getPonumber());
        dto.setOrderDate(entity.getOrder_date());
        dto.setStatus(entity.getStatus());
        dto.setAdditionalInfo(entity.getAdditional_info());
        dto.setSubTotal(entity.getSubTotal());
        dto.setItems(entity.getItems());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setMaterial_req_id(entity.getMaterial_req_id());
        dto.setProjectId(entity.getProjectId());

        // Map supplier
        if (entity.getSupplier() != null) {
            dto.setSupplier(toSupplierDetailsDTO(entity.getSupplier()));
        }

        // Map materials
        if (entity.getMaterials() != null) {
            dto.setMaterials(
                    entity.getMaterials().stream()
                            .map(this::toPurchasingOrderMaterialDTO)
                            .collect(Collectors.toList())
            );
        }

        // Map deliveries
        if (entity.getDeliveries() != null) {
            dto.setDeliveries(
                    entity.getDeliveries().stream()
                            .map(this::toPurchasingOrderDeliveryDTO)
                            .collect(Collectors.toList())
            );
        }

        // Map documents
        if (entity.getDocs() != null) {
            dto.setDocs(
                    entity.getDocs().stream()
                            .map(this::toPurchasingOrderDocDTO)
                            .collect(Collectors.toList())
            );
        }

        // Map order payment
        if (entity.getOrder_payment() != null) {
            dto.setOrderPayment(toOrderPaymentDTO(entity.getOrder_payment()));
        }

        return dto;
    }

    private PurchasingOrderMaterialDTO toPurchasingOrderMaterialDTO(PurchasingOrder_materials entity) {
        PurchasingOrderMaterialDTO dto = new PurchasingOrderMaterialDTO();
        dto.setPurchasingOrderMaterialId(entity.getPurchasingOrderMaterialId());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setCost(entity.getCost());

        if (entity.getMaterial() != null) {
            MaterialDTO materialDTO = new MaterialDTO();
            materialDTO.setMaterialId(entity.getMaterial().getMaterial_id());
            materialDTO.setMaterialName(entity.getMaterial().getMaterialName());
            materialDTO.setMaterialType(entity.getMaterial().getMaterialType());
            materialDTO.setUnitOfMeasurement(entity.getMaterial().getUnitOfMeasurement());
            dto.setMaterial(materialDTO);
        }

        return dto;
    }

    private PurchasingOrderDeliveryDTO toPurchasingOrderDeliveryDTO(PurchasingOrder_Delivery entity) {
        PurchasingOrderDeliveryDTO dto = new PurchasingOrderDeliveryDTO();
        dto.setId(entity.getId());
        dto.setRequiredDate(entity.getRequiredDate());
        dto.setLocation(entity.getLocation());
        dto.setShippingCost(entity.getShippingCost());
        return dto;
    }

    private PurchasingOrderDocDTO toPurchasingOrderDocDTO(PurchasingOrder_Doc entity) {
        PurchasingOrderDocDTO dto = new PurchasingOrderDocDTO();
        dto.setPurchasingOrderDocId(entity.getPurchasingOrder_docId());
        dto.setDocumentName(entity.getDocumentName());
        dto.setDocumentType(entity.getDocumentType());
        dto.setFilePath(entity.getFilePath());
        return dto;
    }

    private OrderPaymentDTO toOrderPaymentDTO(Order_payment entity) {
        OrderPaymentDTO dto = new OrderPaymentDTO();
        dto.setPaymentId(entity.getPayment_id());
        dto.setAmount(entity.getAmount());
        dto.setPaidAmount(entity.getPaid_amount());
        dto.setRemainingAmount(entity.getRemaining_amount());
        dto.setPaymentType(entity.getPayment_type());
        dto.setStatus(entity.getStatus());
        dto.setReferenceNumber(entity.getReference_number());
        dto.setNotes(entity.getNotes());
        dto.setBankDetails(entity.getBank_details());
        dto.setPaymentDate(entity.getPayment_date());
        dto.setCreatedDate(entity.getCreated_date());
        dto.setUpdatedDate(entity.getUpdatedDate());
        return dto;
    }

    private SupplierDetailsDTO toSupplierDetailsDTO(Supplier entity) {
        return SupplierDetailsDTO.builder()
                .supplier_id(entity.getSupplier_id())
                .name(entity.getName())
                .company_name(entity.getCompany_name())
                .Business_Registration_Number(entity.getBusiness_Registration_Number())
                .Delivery_Capabilities(entity.getDelivery_Capabilities())
                .status(entity.getStatus())
                .bank_name(entity.getBank_name())
                .bank_account_name(entity.getBank_account_name())
                .bank_account_number(entity.getBank_account_number())
//                .email(entity.getUserDetails().getEmail())
//                .phone_number1(entity.getUserDetails().getPhone_number1())
//                .phone_number2(entity.getUserDetails().getPhone_number2())
//                .address(entity.(userDetails != null ? userDetails.getAddress() : null))
                .on_time_delivery_rate(entity.getOn_time_delivery_rate())
                .quotation_acceptance_rate(entity.getQuotation_acceptance_rate())
                .past_orders_completed(entity.getPast_orders_completed())
                .avg_delay_days(entity.getAvg_delay_days())
                .rating_by_site_manager(entity.getRating_by_site_manager())
                .build();
    }
}
