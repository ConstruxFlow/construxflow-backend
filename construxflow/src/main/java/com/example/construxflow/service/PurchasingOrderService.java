package com.example.construxflow.service;

import com.example.construxflow.entity.PurchasingOrder;
import com.example.construxflow.dto.PurchasingOrderResponseDTO;
import com.example.construxflow.repository.PurchasingOrderRepository;
import com.example.construxflow.mappers.PurchasingOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
@Transactional
public class PurchasingOrderService {

    @Autowired
    private PurchasingOrderRepository purchasingOrderRepository;

    @Autowired
    private PurchasingOrderMapper purchasingOrderMapper;

    // Create purchasing order
    public PurchasingOrderResponseDTO createPurchasingOrder(PurchasingOrder purchasingOrder) {
        // Set created date
        purchasingOrder.setCreatedDate(LocalDateTime.now());

        // Set bidirectional relationships
        if (purchasingOrder.getMaterials() != null) {
            purchasingOrder.getMaterials().forEach(material -> {
                material.setPurchasingorder(purchasingOrder);
            });
        }

        if (purchasingOrder.getDeliveries() != null) {
            purchasingOrder.getDeliveries().forEach(delivery -> {
                delivery.setPurchasingorder(purchasingOrder);
            });
        }

        if (purchasingOrder.getDocs() != null) {
            purchasingOrder.getDocs().forEach(doc -> {
                doc.setPurchasingorder(purchasingOrder);
            });
        }

        if (purchasingOrder.getOrder_payment() != null) {
            purchasingOrder.getOrder_payment().setPurchasingorder(purchasingOrder);
            purchasingOrder.getOrder_payment().setCreated_date(LocalDateTime.now());
        }

        // Save entity
        PurchasingOrder savedEntity = purchasingOrderRepository.save(purchasingOrder);

        // Force loading of lazy collections
        forceLoadCollections(savedEntity);

        // Convert to DTO and return
        return purchasingOrderMapper.toResponseDTO(savedEntity);
    }

    // Find purchasing order by ID
    public PurchasingOrderResponseDTO findPurchasingOrderById(Long id) {
        Optional<PurchasingOrder> purchasingOrderOpt = purchasingOrderRepository.findByIdWithAllRelations(id);

        if (purchasingOrderOpt.isPresent()) {
            PurchasingOrder purchasingOrder = purchasingOrderOpt.get();

            // Force loading of lazy collections
            forceLoadCollections(purchasingOrder);

            return purchasingOrderMapper.toResponseDTO(purchasingOrder);
        } else {
            throw new RuntimeException("Purchasing Order not found with ID: " + id);
        }
    }

    // Find all purchasing orders
    public List<PurchasingOrderResponseDTO> findAllPurchasingOrders() {
        List<PurchasingOrder> purchasingOrders = purchasingOrderRepository.findAll();

        return purchasingOrders.stream()
                .map(purchasingOrder -> {
                    forceLoadCollections(purchasingOrder);
                    return purchasingOrderMapper.toResponseDTO(purchasingOrder);
                })
                .toList();
    }

    // Find purchasing orders by status
    public List<PurchasingOrderResponseDTO> findPurchasingOrdersByStatus(String status) {
        List<PurchasingOrder> purchasingOrders = purchasingOrderRepository.findByStatus(status);

        return purchasingOrders.stream()
                .map(purchasingOrder -> {
                    forceLoadCollections(purchasingOrder);
                    return purchasingOrderMapper.toResponseDTO(purchasingOrder);
                })
                .toList();
    }

    // Find purchasing orders by supplier
    public List<PurchasingOrderResponseDTO> findPurchasingOrdersBySupplier(String supplierId) {
        List<PurchasingOrder> purchasingOrders = purchasingOrderRepository.findBySupplier(supplierId);

        return purchasingOrders.stream()
                .map(purchasingOrder -> {
                    forceLoadCollections(purchasingOrder);
                    return purchasingOrderMapper.toResponseDTO(purchasingOrder);
                })
                .toList();
    }

    // Find purchasing order by PO number
    public PurchasingOrderResponseDTO findPurchasingOrderByPonumber(String ponumber) {
        Optional<PurchasingOrder> purchasingOrderOpt = purchasingOrderRepository.findByPonumber(ponumber);

        if (purchasingOrderOpt.isPresent()) {
            PurchasingOrder purchasingOrder = purchasingOrderOpt.get();
            forceLoadCollections(purchasingOrder);
            return purchasingOrderMapper.toResponseDTO(purchasingOrder);
        } else {
            throw new RuntimeException("Purchasing Order not found with PO Number: " + ponumber);
        }
    }

    // Update purchasing order status
    public PurchasingOrderResponseDTO updatePurchasingOrderStatus(Long id, String newStatus) {
        Optional<PurchasingOrder> purchasingOrderOpt = purchasingOrderRepository.findById(id);

        if (purchasingOrderOpt.isPresent()) {
            PurchasingOrder purchasingOrder = purchasingOrderOpt.get();
            purchasingOrder.setStatus(newStatus);

            PurchasingOrder updatedPurchasingOrder = purchasingOrderRepository.save(purchasingOrder);

            forceLoadCollections(updatedPurchasingOrder);

            return purchasingOrderMapper.toResponseDTO(updatedPurchasingOrder);
        } else {
            throw new RuntimeException("Purchasing Order not found with ID: " + id);
        }
    }

    // Delete purchasing order
    public void deletePurchasingOrder(Long id) {
        if (purchasingOrderRepository.existsById(id)) {
            purchasingOrderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Purchasing Order not found with ID: " + id);
        }
    }

    // Update entire purchasing order
    public PurchasingOrderResponseDTO updatePurchasingOrder(Long id, PurchasingOrder updatedPurchasingOrder) {
        Optional<PurchasingOrder> existingPurchasingOrderOpt = purchasingOrderRepository.findById(id);

        if (existingPurchasingOrderOpt.isPresent()) {
            PurchasingOrder existingPurchasingOrder = existingPurchasingOrderOpt.get();

            // Update basic fields
            existingPurchasingOrder.setPonumber(updatedPurchasingOrder.getPonumber());
            existingPurchasingOrder.setOrder_date(updatedPurchasingOrder.getOrder_date());
            existingPurchasingOrder.setStatus(updatedPurchasingOrder.getStatus());
            existingPurchasingOrder.setAdditional_info(updatedPurchasingOrder.getAdditional_info());
            existingPurchasingOrder.setSubTotal(updatedPurchasingOrder.getSubTotal());
            existingPurchasingOrder.setItems(updatedPurchasingOrder.getItems());
            existingPurchasingOrder.setSupplier(updatedPurchasingOrder.getSupplier());

            // Clear existing collections
            if (existingPurchasingOrder.getMaterials() != null) {
                existingPurchasingOrder.getMaterials().clear();
            }
            if (existingPurchasingOrder.getDeliveries() != null) {
                existingPurchasingOrder.getDeliveries().clear();
            }
            if (existingPurchasingOrder.getDocs() != null) {
                existingPurchasingOrder.getDocs().clear();
            }

            // Add new collections with proper relationships
            if (updatedPurchasingOrder.getMaterials() != null) {
                updatedPurchasingOrder.getMaterials().forEach(material -> {
                    material.setPurchasingorder(existingPurchasingOrder);
                    existingPurchasingOrder.getMaterials().add(material);
                });
            }

            if (updatedPurchasingOrder.getDeliveries() != null) {
                updatedPurchasingOrder.getDeliveries().forEach(delivery -> {
                    delivery.setPurchasingorder(existingPurchasingOrder);
                    existingPurchasingOrder.getDeliveries().add(delivery);
                });
            }

            if (updatedPurchasingOrder.getDocs() != null) {
                updatedPurchasingOrder.getDocs().forEach(doc -> {
                    doc.setPurchasingorder(existingPurchasingOrder);
                    existingPurchasingOrder.getDocs().add(doc);
                });
            }

            if (updatedPurchasingOrder.getOrder_payment() != null) {
                updatedPurchasingOrder.getOrder_payment().setPurchasingorder(existingPurchasingOrder);
                updatedPurchasingOrder.getOrder_payment().setUpdatedDate(LocalDateTime.now());
                existingPurchasingOrder.setOrder_payment(updatedPurchasingOrder.getOrder_payment());
            }

            PurchasingOrder savedPurchasingOrder = purchasingOrderRepository.save(existingPurchasingOrder);

            forceLoadCollections(savedPurchasingOrder);

            return purchasingOrderMapper.toResponseDTO(savedPurchasingOrder);
        } else {
            throw new RuntimeException("Purchasing Order not found with ID: " + id);
        }
    }

    // Helper method to force loading of lazy collections
    private void forceLoadCollections(PurchasingOrder purchasingOrder) {
        if (purchasingOrder.getMaterials() != null) {
            purchasingOrder.getMaterials().forEach(pom -> {
                if (pom.getMaterial() != null) {
                    pom.getMaterial().getMaterialName();
                    pom.getMaterial().getMaterialType();
                    pom.getMaterial().getUnitOfMeasurement();
                }
            });
        }

        if (purchasingOrder.getDeliveries() != null) {
            purchasingOrder.getDeliveries().size();
        }

        if (purchasingOrder.getDocs() != null) {
            purchasingOrder.getDocs().size();
        }

        if (purchasingOrder.getOrder_payment() != null) {
            purchasingOrder.getOrder_payment().getAmount();
        }

        if (purchasingOrder.getSupplier() != null) {
            purchasingOrder.getSupplier().getName();
            purchasingOrder.getSupplier().getCompany_name();
        }
    }
}