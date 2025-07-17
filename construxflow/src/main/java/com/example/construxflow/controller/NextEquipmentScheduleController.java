package com.example.construxflow.controller;

import com.example.construxflow.dto.NextEquipmentScheduleRequestDTO;
import com.example.construxflow.dto.NextEquipmentScheduleResponseDTO;
import com.example.construxflow.service.NextEquipmentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/nextschedule")
@CrossOrigin(origins = "http://localhost:3000")
public class NextEquipmentScheduleController {

    @Autowired
    private NextEquipmentScheduleService nextEquipmentScheduleService;

    @PostMapping("/setnextschedule")
    public ResponseEntity<?> schedule(@RequestBody NextEquipmentScheduleRequestDTO requestDTO) {
        try{
            NextEquipmentScheduleResponseDTO responseDTO = nextEquipmentScheduleService.scheduleNextEquipment(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<NextEquipmentScheduleResponseDTO> getNextScheduleByAssignId(@RequestParam String assignId) {
        Optional<NextEquipmentScheduleResponseDTO> nextEquipmentResponsedto = nextEquipmentScheduleService.getNextScheduleDetailsByAssignId(assignId);
        return nextEquipmentResponsedto.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
