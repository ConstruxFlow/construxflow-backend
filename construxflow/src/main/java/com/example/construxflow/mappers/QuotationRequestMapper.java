package com.example.construxflow.mappers;

import com.example.construxflow.entity.*;
import com.example.construxflow.dto.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuotationRequestMapper {

    public QuotationRequestResponseDTO toResponseDTO(Quotation_request entity) {
        if (entity == null) return null;

        QuotationRequestResponseDTO dto = new QuotationRequestResponseDTO();
        dto.setId(entity.getId());
        dto.setRequesterName(entity.getRequesterName());
        dto.setRequestDate(entity.getRequest_date());
        dto.setQuotationDeadline(entity.getQuotation_deadline());
        dto.setPriorityLevel(entity.getPriority_level());
        dto.setStatus(entity.getStatus());
        dto.setAdditionalInfo(entity.getAdditional_info());
        dto.setQuotationType(entity.getQuotation_type());
        dto.setEstimatedCost(entity.getEstimated_cost());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setMaterial_req_id(entity.getMaterial_req_id());
        dto.setProjectId(entity.getProjectId());
        dto.setManagerid(entity.getManager_id());

        // Map nested collections
        if (entity.getQuotationReqMaterials() != null) {
            dto.setQuotationReqMaterials(
                    entity.getQuotationReqMaterials().stream()
                            .map(this::toMaterialDTO)
                            .collect(Collectors.toList())
            );
        }

        if (entity.getQuotationReqDelivery() != null) {
            dto.setQuotationReqDelivery(
                    entity.getQuotationReqDelivery().stream()
                            .map(this::toDeliveryDTO)
                            .collect(Collectors.toList())
            );
        }

        if (entity.getQuotationReqDocs() != null) {
            dto.setQuotationReqDocs(
                    entity.getQuotationReqDocs().stream()
                            .map(this::toDocDTO)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private QuotationReqMaterialDTO toMaterialDTO(Quotation_req_materials entity) {
        QuotationReqMaterialDTO dto = new QuotationReqMaterialDTO();
        dto.setQuotationReqMaterialId(entity.getQuotationReqMaterialId());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setEstimatedCost(entity.getEstimatedCost());


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

    private QuotationReqDeliveryDTO toDeliveryDTO(Quotation_req_delivery entity) {
        QuotationReqDeliveryDTO dto = new QuotationReqDeliveryDTO();
        dto.setQuotationReqDeliveryId(entity.getQuotationReqDeliveryId());
        dto.setLocation(entity.getLocation());
        dto.setDeliveryDate(entity.getDeliveryDate());
        dto.setQuantitySplit(entity.getQuantitySplit());
        return dto;
    }

    private QuotationReqDocDTO toDocDTO(Quotation_req_doc entity) {
        QuotationReqDocDTO dto = new QuotationReqDocDTO();
        dto.setQuotationReqId(entity.getQuotationReqId());
        dto.setDocumentName(entity.getDocumentName());
        dto.setDocumentType(entity.getDocumentType());
        dto.setFilePath(entity.getFilePath());
        return dto;
    }

//    private ManagerDTO toManagerDTO(Manager entity) {
//        ManagerDTO dto = new ManagerDTO();
//        dto.setManagerId(entity.getManager_id());
//        // dto.setManagerName(entity.getManagerName()); // Add if field exists
//        return dto;
//    }
}
