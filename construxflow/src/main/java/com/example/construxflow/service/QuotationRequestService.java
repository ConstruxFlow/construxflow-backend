package com.example.construxflow.service;

import com.example.construxflow.dto.QuotationRequestResponseDTO;
import com.example.construxflow.entity.Quotation_req_delivery;
import com.example.construxflow.entity.Quotation_req_doc;
import com.example.construxflow.entity.Quotation_req_materials;
import com.example.construxflow.entity.Quotation_request;
import com.example.construxflow.mappers.QuotationRequestMapper;
import com.example.construxflow.repository.QuotationReqDeliveryRepository;
import com.example.construxflow.repository.QuotationReqDocRepository;
import com.example.construxflow.repository.QuotationReqMaterialRepository;
import com.example.construxflow.repository.QuotationReqRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuotationRequestService {

    @Autowired
    private QuotationReqRepository quotationReqRepository;

    @Autowired
    private QuotationReqMaterialRepository quotationReqMaterialRepository;

    @Autowired
    private QuotationReqDeliveryRepository quotationReqDeliveryRepository;

    @Autowired
    private QuotationReqDocRepository quotationReqDocRepository;


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

        quotation_request.setCreatedDate(LocalDateTime.now());

        Quotation_request savedEntity  = quotationReqRepository.save(quotation_request);
//        System.out.println(quotation_request);
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

    public List<QuotationRequestResponseDTO> findAllQuotations() {
        List<Quotation_request> quotations = quotationReqRepository.findAll();

        return quotations.stream()
                .map(quotation -> {
                    // Force loading for each quotation
                    if (quotation.getQuotationReqMaterials() != null) {
                        quotation.getQuotationReqMaterials().forEach(qrm -> {
                            if (qrm.getMaterial() != null) {
                                qrm.getMaterial().getMaterialName();
                            }
                        });
                    }
                    return quotationRequestMapper.toResponseDTO(quotation);
                })
                .toList();
    }

    public QuotationRequestResponseDTO findQuotationById(Long id) {
        Optional<Quotation_request> quotationOpt = quotationReqRepository.findById(id);

        if (quotationOpt.isPresent()) {
            Quotation_request quotation = quotationOpt.get();

            // Force loading of lazy collections within transaction
            if (quotation.getQuotationReqMaterials() != null) {
                quotation.getQuotationReqMaterials().forEach(qrm -> {
                    if (qrm.getMaterial() != null) {
                        qrm.getMaterial().getMaterialName();
                        qrm.getMaterial().getMaterialType();
                        qrm.getMaterial().getUnitOfMeasurement();
                    }
                });
            }

            if (quotation.getQuotationReqDelivery() != null) {
                quotation.getQuotationReqDelivery().size(); // Trigger loading
            }

            if (quotation.getQuotationReqDocs() != null) {
                quotation.getQuotationReqDocs().size(); // Trigger loading
            }

            return quotationRequestMapper.toResponseDTO(quotation);
        } else {
            throw new RuntimeException("Quotation not found with ID: " + id);
        }
    }

    public QuotationRequestResponseDTO updateQuotationStatus(Long id, String newStatus) {
        Optional<Quotation_request> quotationOpt = quotationReqRepository.findById(id);

        if (quotationOpt.isPresent()) {
            Quotation_request quotation = quotationOpt.get();
            quotation.setStatus(newStatus);

            Quotation_request updatedQuotation = quotationReqRepository.save(quotation);

            // Force loading of lazy collections
            forceLoadCollections(updatedQuotation);

            return quotationRequestMapper.toResponseDTO(updatedQuotation);
        } else {
            throw new RuntimeException("Quotation not found with ID: " + id);
        }
    }

    //Delete quotation
    public void deleteQuotation(Long id) {
        if (quotationReqRepository.existsById(id)) {
            quotationReqRepository.deleteById(id);
        } else {
            throw new RuntimeException("Quotation not found with ID: " + id);
        }
    }

    // Update entire quotation
    @Transactional
    public QuotationRequestResponseDTO updateQuotation(Long id, Quotation_request updatedQuotation) {
        Optional<Quotation_request> existingQuotationOpt = quotationReqRepository.findById(id);

        if (existingQuotationOpt.isPresent()) {
            Quotation_request existingQuotation = existingQuotationOpt.get();

            // Update basic fields
            existingQuotation.setRequesterName(updatedQuotation.getRequesterName());
            existingQuotation.setRequest_date(updatedQuotation.getRequest_date());
            existingQuotation.setQuotation_deadline(updatedQuotation.getQuotation_deadline());
            existingQuotation.setPriority_level(updatedQuotation.getPriority_level());
            existingQuotation.setStatus(updatedQuotation.getStatus());
            existingQuotation.setAdditional_info(updatedQuotation.getAdditional_info());
            existingQuotation.setQuotation_type(updatedQuotation.getQuotation_type());
            existingQuotation.setEstimated_cost(updatedQuotation.getEstimated_cost());
            existingQuotation.setManager(updatedQuotation.getManager());

            // Delete existing materials from database BEFORE clearing
            if (existingQuotation.getQuotationReqMaterials() != null && !existingQuotation.getQuotationReqMaterials().isEmpty()) {
                List<Long> materialIds = existingQuotation.getQuotationReqMaterials().stream()
                        .map(Quotation_req_materials::getQuotationReqMaterialId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                if (!materialIds.isEmpty()) {
                    quotationReqMaterialRepository.deleteAllById(materialIds);
                }
                existingQuotation.getQuotationReqMaterials().clear();
            }

            // Delete existing delivery locations from database BEFORE clearing
            if (existingQuotation.getQuotationReqDelivery() != null && !existingQuotation.getQuotationReqDelivery().isEmpty()) {
                List<Long> deliveryIds = existingQuotation.getQuotationReqDelivery().stream()
                        .map(Quotation_req_delivery::getQuotationReqDeliveryId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                if (!deliveryIds.isEmpty()) {
                    quotationReqDeliveryRepository.deleteAllById(deliveryIds);
                }
                existingQuotation.getQuotationReqDelivery().clear();
            }

            // Delete existing documents from database BEFORE clearing
            if (existingQuotation.getQuotationReqDocs() != null && !existingQuotation.getQuotationReqDocs().isEmpty()) {
                List<Long> docIds = existingQuotation.getQuotationReqDocs().stream()
                        .map(Quotation_req_doc::getQuotationReqId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                if (!docIds.isEmpty()) {
                    quotationReqDocRepository.deleteAllById(docIds);
                }
                existingQuotation.getQuotationReqDocs().clear();
            }

            // Flush to ensure deletions are committed
            quotationReqRepository.flush();

            // Add new collections with proper relationships
            if (updatedQuotation.getQuotationReqMaterials() != null) {
                updatedQuotation.getQuotationReqMaterials().forEach(material -> {
                    material.setQuotationRequest(existingQuotation);
                    material.setQuotationReqMaterialId(null); // Ensure new ID generation
                    existingQuotation.getQuotationReqMaterials().add(material);
                });
            }

            if (updatedQuotation.getQuotationReqDelivery() != null) {
                updatedQuotation.getQuotationReqDelivery().forEach(delivery -> {
                    delivery.setQuotationRequest(existingQuotation);
                    delivery.setQuotationReqDeliveryId(null); // Ensure new ID generation
                    existingQuotation.getQuotationReqDelivery().add(delivery);
                });
            }

            if (updatedQuotation.getQuotationReqDocs() != null) {
                updatedQuotation.getQuotationReqDocs().forEach(doc -> {
                    doc.setQuotationRequest(existingQuotation);
                    doc.setQuotationReqId(null); // Ensure new ID generation
                    existingQuotation.getQuotationReqDocs().add(doc);
                });
            }

            Quotation_request savedQuotation = quotationReqRepository.save(existingQuotation);

            // Force loading of lazy collections
            forceLoadCollections(savedQuotation);

            return quotationRequestMapper.toResponseDTO(savedQuotation);
        } else {
            throw new RuntimeException("Quotation not found with ID: " + id);
        }
    }



    // Helper method to force loading of lazy collections
    private void forceLoadCollections(Quotation_request quotation) {
        if (quotation.getQuotationReqMaterials() != null) {
            quotation.getQuotationReqMaterials().forEach(qrm -> {
                if (qrm.getMaterial() != null) {
                    qrm.getMaterial().getMaterialName();
                    qrm.getMaterial().getMaterialType();
                    qrm.getMaterial().getUnitOfMeasurement();
                }
            });
        }

        if (quotation.getQuotationReqDelivery() != null) {
            quotation.getQuotationReqDelivery().size();
        }

        if (quotation.getQuotationReqDocs() != null) {
            quotation.getQuotationReqDocs().size();
        }
    }
}
