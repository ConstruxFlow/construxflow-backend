package com.example.construxflow.controller;

import com.example.construxflow.dto.QuotationResponseDTO;
import com.example.construxflow.service.QuotationService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
@CrossOrigin(origins = "http://localhost:")
public class QuotationController {

    private final QuotationService quotationService;

    @Autowired
    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    // POST /create
    @PostMapping("/create")
    public ResponseEntity<QuotationResponseDTO> createQuotation(@RequestBody QuotationResponseDTO dto) {
        QuotationResponseDTO created = quotationService.createQuotation(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // GET /all
    @GetMapping("/all")
    public ResponseEntity<List<QuotationResponseDTO>> getAllQuotations() {
        List<QuotationResponseDTO> quotations = quotationService.getAllQuotations();
        return new ResponseEntity<>(quotations, HttpStatus.OK);
    }

    // GET /find/{id}
    @GetMapping("/find/{id}")
    public ResponseEntity<QuotationResponseDTO> getQuotationById(@PathVariable Long id) {
        QuotationResponseDTO dto = quotationService.getQuotationById(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // PUT /update/{id}
    @PutMapping("/update/{id}")
    public ResponseEntity<QuotationResponseDTO> updateQuotation(
            @PathVariable Long id,
            @RequestBody QuotationResponseDTO dto) {
        QuotationResponseDTO updated = quotationService.updateQuotation(id, dto);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // PATCH /{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<QuotationResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody String status) {
        QuotationResponseDTO updated = quotationService.updateStatus(id, status);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE /delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteQuotation(@PathVariable Long id) {
        boolean deleted = quotationService.deleteQuotation(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    FIND BY USING QUOTATION REQUEST ID
    @GetMapping("/byquotationreq/{quotationRequestId}")
    public ResponseEntity<List<QuotationResponseDTO>> getQuotationsByRequestIdValidated(
            @PathVariable Long quotationRequestId) {
        try {
            List<QuotationResponseDTO> quotations = quotationService.getQuotationsByRequestIdWithValidation(quotationRequestId);
            return new ResponseEntity<>(quotations, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

