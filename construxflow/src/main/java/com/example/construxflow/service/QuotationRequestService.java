package com.example.construxflow.service;

import com.example.construxflow.dto.QuotationRequestResponseDTO;
import com.example.construxflow.entity.Quotation_request;
import com.example.construxflow.mappers.QuotationRequestMapper;
import com.example.construxflow.repository.QuotationReqRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class QuotationRequestService {

    @Autowired
    private QuotationReqRepository quotationReqRepository;

    @Autowired
    private QuotationRequestMapper quotationRequestMapper;

    public QuotationRequestResponseDTO createQuotation_request(Quotation_request quotation_request) {

        // Establish bidirectional relationships before saving
        if (quotation_request.getQuotationReqMaterials() != null) {
            quotation_request.getQuotationReqMaterials().forEach(material -> {
                material.setQuotationRequest(quotation_request);
            });
        }

        if (quotation_request.getQuotationReqDelivery() != null) {
            quotation_request.getQuotationReqDelivery().forEach(delivery -> {
                delivery.setQuotationRequest(quotation_request);
            });
        }

        if (quotation_request.getQuotationReqDocs() != null) {
            quotation_request.getQuotationReqDocs().forEach(doc -> {
                doc.setQuotationRequest(quotation_request);
            });
        }


        Quotation_request savedEntity  = quotationReqRepository.save(quotation_request);
        // Force loading of lazy collections within transaction
        if (savedEntity.getQuotationReqMaterials() != null) {
            savedEntity.getQuotationReqMaterials().forEach(qrm -> {
                if (qrm.getMaterial() != null) {
                    // Access material properties to trigger loading
                    qrm.getMaterial().getMaterialName();
                    qrm.getMaterial().getMaterialType();
                    qrm.getMaterial().getUnitOfMeasurement();
                }
            });
        }
        // Convert to DTO and return
        return quotationRequestMapper.toResponseDTO(savedEntity);
    }
}
