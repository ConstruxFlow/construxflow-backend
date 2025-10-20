package com.example.construxflow.service;

import com.example.construxflow.dto.NextEquipmentScheduleRequestDTO;
import com.example.construxflow.dto.NextEquipmentScheduleResponseDTO;
import com.example.construxflow.entity.Next_Equipment_Schedule;
import com.example.construxflow.repository.NextEquipmentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NextEquipmentScheduleService {

    @Autowired
    private NextEquipmentScheduleRepository nextEquipmentScheduleRepository;

    public List<NextEquipmentScheduleResponseDTO> scheduleNextEquipment(NextEquipmentScheduleRequestDTO requestDTO) {
        List<NextEquipmentScheduleResponseDTO> responseList = new ArrayList<>();

        for (String technicianId : requestDTO.getTechnicianIds()) {
            String nextScheduleId = "NSCH-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

            Next_Equipment_Schedule schedule = new Next_Equipment_Schedule();
            schedule.setNextScheduleId(nextScheduleId);
            schedule.setAssignId(requestDTO.getAssignId());
            schedule.setEquipmentId(requestDTO.getEquipmentId());
            schedule.setEquipmentScheduleId(requestDTO.getEquipmentScheduleId());
            schedule.setNextMaintenanceType(requestDTO.getNextMaintenanceType());
            schedule.setNextDate(requestDTO.getNextDate());
            schedule.setEstimateDuration(requestDTO.getEstimateDuration());
            schedule.setPriority(requestDTO.getPriority());
            schedule.setTechnicianId(technicianId);
            schedule.setLastMaintenanceDate(requestDTO.getLastMaintenanceDate());

            Next_Equipment_Schedule saved = nextEquipmentScheduleRepository.save(schedule);

            NextEquipmentScheduleResponseDTO dto = new NextEquipmentScheduleResponseDTO();
            dto.setNextScheduleId(saved.getNextScheduleId());
            dto.setAssignId(saved.getAssignId());
            dto.setEquipmentId(saved.getEquipmentId());
            dto.setEquipmentScheduleId(saved.getEquipmentScheduleId());
            dto.setNextMaintenanceType(saved.getNextMaintenanceType());
            dto.setNextDate(saved.getNextDate());
            dto.setEstimateDuration(saved.getEstimateDuration());
            dto.setPriority(saved.getPriority());
            dto.setTechnicianId(saved.getTechnicianId());

            responseList.add(dto);
        }

        return responseList;
    }


    public List<NextEquipmentScheduleResponseDTO> getNextScheduleDetailsByAssignId(String assignId) {
        List<Next_Equipment_Schedule> nextSchedules = nextEquipmentScheduleRepository.findByAssignId(assignId);

        return nextSchedules.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NextEquipmentScheduleResponseDTO> getAllNextScheduleDetails() {
        List<Next_Equipment_Schedule> nextSchedules = nextEquipmentScheduleRepository.findAll();
        LocalDate today = LocalDate.now();

        // Group all schedules by equipment ID (including those with null dates)
        Map<String, List<Next_Equipment_Schedule>> schedulesByEquipment = nextSchedules.stream()
                .filter(schedule -> schedule.getEquipmentId() != null && !schedule.getEquipmentId().trim().isEmpty())
                .collect(Collectors.groupingBy(Next_Equipment_Schedule::getEquipmentId));

        List<NextEquipmentScheduleResponseDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<Next_Equipment_Schedule>> entry : schedulesByEquipment.entrySet()) {
            String equipmentId = entry.getKey();
            List<Next_Equipment_Schedule> equipmentSchedules = entry.getValue();

            // Separate schedules with valid dates from those without dates
            List<Next_Equipment_Schedule> schedulesWithDates = equipmentSchedules.stream()
                    .filter(schedule -> schedule.getNextDate() != null &&
                                      !schedule.getNextDate().trim().isEmpty() &&
                                      isValidDate(schedule.getNextDate()))
                    .collect(Collectors.toList());

            List<Next_Equipment_Schedule> schedulesWithoutDates = equipmentSchedules.stream()
                    .filter(schedule -> schedule.getNextDate() == null ||
                                      schedule.getNextDate().trim().isEmpty() ||
                                      !isValidDate(schedule.getNextDate()))
                    .collect(Collectors.toList());

            Next_Equipment_Schedule selectedSchedule = null;

            if (!schedulesWithDates.isEmpty()) {
                // Apply date logic for schedules with valid dates
                List<Next_Equipment_Schedule> futureSchedules = schedulesWithDates.stream()
                        .filter(schedule -> isFutureDate(schedule.getNextDate(), today))
                        .collect(Collectors.toList());

                if (!futureSchedules.isEmpty()) {
                    // Get the greatest future date
                    selectedSchedule = futureSchedules.stream()
                            .max(Comparator.comparing(schedule -> parseDate(schedule.getNextDate())))
                            .orElse(null);
                } else {
                    // Get the most recent past date
                    selectedSchedule = schedulesWithDates.stream()
                            .max(Comparator.comparing(schedule -> parseDate(schedule.getNextDate())))
                            .orElse(null);
                }
            } else if (!schedulesWithoutDates.isEmpty()) {
                // If no schedules with dates, pick any schedule for this equipment (e.g., the first one)
                selectedSchedule = schedulesWithoutDates.get(0);
            }

            if (selectedSchedule != null) {
                result.add(mapToResponseDTO(selectedSchedule));
            }
        }

        return result;
    }

    private boolean isFutureDate(String dateString, LocalDate today) {
        try {
            LocalDate date = parseDate(dateString);
            return date.isAfter(today);
        } catch (DateTimeParseException e) {
            return false; // If date parsing fails, exclude the record
        }
    }

    private LocalDate parseDate(String dateString) {
        // Try different date formats commonly used
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                // Continue to next formatter
            }
        }
        throw new DateTimeParseException("Unable to parse date: " + dateString, dateString, 0);
    }


    private boolean isValidDate(String dateString) {
        try {
            parseDate(dateString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public NextEquipmentScheduleResponseDTO mapToResponseDTO(Next_Equipment_Schedule nextEquipmentSchedule) {
        return NextEquipmentScheduleResponseDTO.builder()
                .nextScheduleId(nextEquipmentSchedule.getNextScheduleId())
                .assignId(nextEquipmentSchedule.getAssignId())
                .equipmentId(nextEquipmentSchedule.getEquipmentId())
                .equipmentScheduleId(nextEquipmentSchedule.getEquipmentScheduleId())
                .nextMaintenanceType(nextEquipmentSchedule.getNextMaintenanceType())
                .nextDate(nextEquipmentSchedule.getNextDate())
                .estimateDuration(nextEquipmentSchedule.getEstimateDuration())
                .priority(nextEquipmentSchedule.getPriority())
                .technicianId(nextEquipmentSchedule.getTechnicianId())
                .lastMaintenanceDate(nextEquipmentSchedule.getLastMaintenanceDate())
                .build();
    }
}
