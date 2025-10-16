package com.example.construxflow.controller;

import com.example.construxflow.dto.TeamMemberRequestDTO;
import com.example.construxflow.dto.TeamMemberResponseDTO;
import com.example.construxflow.service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/team")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamMemberController {

    @Autowired
    private TeamMemberService teamMemberService;

    @PostMapping("/addteam")
    public ResponseEntity<?> addTeamMember(@RequestBody TeamMemberRequestDTO requestDTO) {
        try {
            TeamMemberResponseDTO responseDTO = teamMemberService.addTeamMember(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error with added team member: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTeamMembers() {
        try {
            List<TeamMemberResponseDTO> teamMembers = teamMemberService.getAllTeamMembers();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(teamMembers);
        } catch (Exception e) {
            // Optionally, log the exception here for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving team members: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getTeamMemberById(@RequestParam String id) {
        try {
            Optional<TeamMemberResponseDTO> teamMember = teamMemberService.getTeamMember(id);
            if (teamMember.isPresent()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(teamMember.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Team Member Not Found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving team member: " + e.getMessage());
        }
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateTeamMemberStatus(
            @RequestParam String empId,
            @RequestParam String status) {
        try {
            TeamMemberResponseDTO updatedMember = teamMemberService.updateTeamMemberStatus(empId, status);
            return ResponseEntity.ok(updatedMember);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Team member not found with empId: " + empId);
            } else if (e.getMessage().contains("Invalid status")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid status value: " + status);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error updating team member status: " + e.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }
}
