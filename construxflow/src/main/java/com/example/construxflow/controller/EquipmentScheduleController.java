package com.example.construxflow.controller;

import com.example.construxflow.dto.EquipmentScheduleDTO;
import com.example.construxflow.dto.ScheduleFormDataDTO;
import com.example.construxflow.entity.EquipmentSchedule;
import com.example.construxflow.service.EquipmentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/equipment-schedule")
@RequiredArgsConstructor
public class EquipmentScheduleController {

    private final EquipmentScheduleService scheduleService;

    @GetMapping("/form-data/{equipmentId}")
    public ResponseEntity<ScheduleFormDataDTO> getScheduleFormData(@PathVariable Long equipmentId) {
        try {
            ScheduleFormDataDTO formData = scheduleService.getScheduleFormData(equipmentId);
            return ResponseEntity.ok(formData);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<EquipmentSchedule> createSchedule(@RequestBody EquipmentScheduleDTO scheduleDTO) {
        try {
            EquipmentSchedule schedule = scheduleService.createSchedule(scheduleDTO);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<EquipmentSchedule>> getEquipmentSchedules(@PathVariable Long equipmentId) {
        List<EquipmentSchedule> schedules = scheduleService.getSchedulesByEquipmentId(equipmentId);
        return ResponseEntity.ok(schedules);
    }
}