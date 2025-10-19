package com.example.construxflow.controller;

import com.example.construxflow.api_response.ApiResponse;
import com.example.construxflow.entity.PurchasingOrder;
import com.example.construxflow.dto.PurchasingOrderResponseDTO;
import com.example.construxflow.service.PurchasingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchasingorder")
@CrossOrigin(origins = "http://localhost:")
public class PurchasingOrderController {

    @Autowired
    private PurchasingOrderService purchasingOrderService;

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<?>> getLatestPurchasingOrder() {
        try {
            PurchasingOrderResponseDTO response = purchasingOrderService.findLatestPurchasingOrder();
            return ResponseEntity.ok(ApiResponse.success("Latest Purchasing Order retrieved successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Purchasing Order not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve purchasing order", e.getMessage()));
        }
    }


    // Create purchasing order
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createPurchasingOrder(
            @RequestBody PurchasingOrder purchasingOrder) {
        try {
            PurchasingOrderResponseDTO response = purchasingOrderService.createPurchasingOrder(purchasingOrder);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Purchasing Order created successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create purchasing order", e.getMessage()));
        }
    }

    // Get purchasing order by ID
    @GetMapping("/find/{id}")
    public ResponseEntity<ApiResponse<?>> getPurchasingOrderById(@PathVariable Long id) {
        try {
            PurchasingOrderResponseDTO response = purchasingOrderService.findPurchasingOrderById(id);
            return ResponseEntity.ok(ApiResponse.success("Purchasing Order retrieved successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Purchasing Order not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve purchasing order", e.getMessage()));
        }
    }

    // Get purchasing order by PO number
    @GetMapping("/ponumber/{ponumber}")
    public ResponseEntity<ApiResponse<?>> getPurchasingOrderByPonumber(@PathVariable String ponumber) {
        try {
            PurchasingOrderResponseDTO response = purchasingOrderService.findPurchasingOrderByPonumber(ponumber);
            return ResponseEntity.ok(ApiResponse.success("Purchasing Order retrieved successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Purchasing Order not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve purchasing order", e.getMessage()));
        }
    }

    // Get purchasing orders by project ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<?>> getPurchasingOrdersByProjectId(@PathVariable String projectId) {
        try {
            List<PurchasingOrderResponseDTO> response = purchasingOrderService.findPurchasingOrdersByProjectId(projectId);
            return ResponseEntity.ok(ApiResponse.success("Purchasing Orders retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve purchasing orders", e.getMessage()));
        }
    }

    // Get all purchasing orders
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllPurchasingOrders() {
        try {
            List<PurchasingOrderResponseDTO> response = purchasingOrderService.findAllPurchasingOrders();
            return ResponseEntity.ok(ApiResponse.success("Purchasing Orders retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve purchasing orders", e.getMessage()));
        }
    }

    // Get delivered materials for a specific project
    @GetMapping("/project/{projectId}/delivered-materials")
    public ResponseEntity<ApiResponse<?>> getDeliveredMaterialsByProject(@PathVariable String projectId) {
        try {
            List<com.example.construxflow.dto.PurchasingOrderMaterialDTO> materials = purchasingOrderService.getDeliveredMaterialsByProject(projectId);
            return ResponseEntity.ok(ApiResponse.success("Delivered materials retrieved successfully", materials));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve delivered materials", e.getMessage()));
        }
    }

    // Get purchasing orders by status
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<?>> getPurchasingOrdersByStatus(@PathVariable String status) {
        try {
            List<PurchasingOrderResponseDTO> response = purchasingOrderService.findPurchasingOrdersByStatus(status);
            return ResponseEntity.ok(ApiResponse.success("Purchasing Orders retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve purchasing orders", e.getMessage()));
        }
    }

    // Get purchasing orders by supplier
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<ApiResponse<?>> getPurchasingOrdersBySupplier(@PathVariable String supplierId) {
        try {
            List<PurchasingOrderResponseDTO> response = purchasingOrderService.findPurchasingOrdersBySupplier(supplierId);
            return ResponseEntity.ok(ApiResponse.success("Purchasing Orders retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve purchasing orders", e.getMessage()));
        }
    }

    // Update purchasing order status
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> updatePurchasingOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            String newStatus = statusUpdate.get("status");
            if (newStatus == null || newStatus.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Status is required", "Status field cannot be empty"));
            }

            PurchasingOrderResponseDTO response = purchasingOrderService.updatePurchasingOrderStatus(id, newStatus);
            return ResponseEntity.ok(ApiResponse.success("Purchasing Order status updated successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Purchasing Order not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update purchasing order status", e.getMessage()));
        }
    }

    // Update payment details (for full payment completion)
    @PatchMapping("/{id}/payment")
    public ResponseEntity<ApiResponse<?>> updatePaymentDetails(
            @PathVariable Long id,
            @RequestBody Map<String, Object> paymentData) {
        try {
            PurchasingOrderResponseDTO response = purchasingOrderService.updatePaymentDetails(id, paymentData);
            return ResponseEntity.ok(ApiResponse.success("Payment details updated successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Purchasing Order not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update payment details", e.getMessage()));
        }
    }

    // Delete purchasing order
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deletePurchasingOrder(@PathVariable Long id) {
        try {
            purchasingOrderService.deletePurchasingOrder(id);
            return ResponseEntity.ok(ApiResponse.success("Purchasing Order deleted successfully",
                    "Purchasing Order with ID " + id + " has been deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Purchasing Order not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete purchasing order", e.getMessage()));
        }
    }

    // Update entire purchasing order
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<?>> updatePurchasingOrder(
            @PathVariable Long id,
            @RequestBody PurchasingOrder purchasingOrder) {
        try {
            PurchasingOrderResponseDTO response = purchasingOrderService.updatePurchasingOrder(id, purchasingOrder);
            return ResponseEntity.ok(ApiResponse.success("Purchasing Order updated successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Purchasing Order not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update purchasing order", e.getMessage()));
        }
    }

    // Update both purchasing order status and payment status
    @PatchMapping("/{id}/update-status")
    public ResponseEntity<ApiResponse<?>> updateStatuses(
            @PathVariable Long id,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String paymentStatus) {
        try {
            // Validate that at least one parameter is provided
            if ((orderStatus == null || orderStatus.trim().isEmpty()) &&
                    (paymentStatus == null || paymentStatus.trim().isEmpty())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("At least one status is required",
                                "Provide either orderStatus or paymentStatus parameter"));
            }

            PurchasingOrderResponseDTO response = purchasingOrderService.updateStatusesOnly(id, orderStatus, paymentStatus);
            return ResponseEntity.ok(ApiResponse.success("Statues updated successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Purchasing Order not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update statuses", e.getMessage()));
        }
    }

}
