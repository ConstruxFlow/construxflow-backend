package com.example.construxflow.controller;

import com.example.construxflow.api_response.ApiResponse;
import com.example.construxflow.dto.QuotationRequestResponseDTO;
import com.example.construxflow.entity.Quotation_request;
import com.example.construxflow.service.QuotationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quotationrequest")
@CrossOrigin(origins = "http://localhost:")
public class QuotationRequestController {

    @Autowired
    private QuotationRequestService quotationRequestService;

    @PostMapping("/create")
    public ApiResponse<?> createQuotationRequestService(@RequestBody Quotation_request quotation) {
        try{
            QuotationRequestResponseDTO response=quotationRequestService.createQuotation_request(quotation);
            return ApiResponse.success("Quotation Create Successfully",response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllQuotations() {
        try {
            List<QuotationRequestResponseDTO> response = quotationRequestService.findAllQuotations();
            return ResponseEntity.ok(ApiResponse.success("Quotations retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve quotations", e.getMessage()));
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ApiResponse<?>> getQuotationById(@PathVariable Long id) {
        try {
            QuotationRequestResponseDTO response = quotationRequestService.findQuotationById(id);
            return ResponseEntity.ok(ApiResponse.success("Quotation retrieved successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Quotation not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve quotation", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> updateQuotationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            String newStatus = statusUpdate.get("status");
            if (newStatus == null || newStatus.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Status is required", "Status field cannot be empty"));
            }

            QuotationRequestResponseDTO response = quotationRequestService.updateQuotationStatus(id, newStatus);
            return ResponseEntity.ok(ApiResponse.success("Quotation status updated successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Quotation not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update quotation status", e.getMessage()));
        }
    }

    //  Delete quotation
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteQuotation(@PathVariable Long id) {
        try {
            quotationRequestService.deleteQuotation(id);
            return ResponseEntity.ok(ApiResponse.success("Quotation deleted successfully", "Quotation with ID " + id + " has been deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Quotation not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete quotation", e.getMessage()));
        }
    }

    // Update entire quotation
    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse<?>> updateQuotation(
            @PathVariable Long id,
            @RequestBody Quotation_request quotation) {
        try {
            QuotationRequestResponseDTO response = quotationRequestService.updateQuotation(id, quotation);
            return ResponseEntity.ok(ApiResponse.success("Quotation updated successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Quotation not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update quotation", e.getMessage()));
        }
    }
}
