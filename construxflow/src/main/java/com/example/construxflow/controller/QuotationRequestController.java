package com.example.construxflow.controller;

import com.example.construxflow.api_response.ApiResponse;
import com.example.construxflow.dto.QuotationRequestResponseDTO;
import com.example.construxflow.entity.Quotation_request;
import com.example.construxflow.service.QuotationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotationrequest")
@CrossOrigin(origins = "http://localhost:3000")
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
}
