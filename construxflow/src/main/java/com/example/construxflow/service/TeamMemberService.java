package com.example.construxflow.service;

import com.example.construxflow.dto.TeamMemberRequestDTO;
import com.example.construxflow.dto.TeamMemberResponseDTO;
import com.example.construxflow.entity.Team_Member;
import com.example.construxflow.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamMemberService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    public TeamMemberResponseDTO addTeamMember(TeamMemberRequestDTO requestDTO) {

        String empId = "EMP-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        Team_Member teamMember = new Team_Member();
        teamMember.setEmpId(empId);
        teamMember.setName(requestDTO.getName());
        teamMember.setEmail(requestDTO.getEmail());
        teamMember.setPhone(requestDTO.getPhone());
        teamMember.setDepartment(requestDTO.getDepartment());
        teamMember.setExperience(requestDTO.getExperience());
        teamMember.setNic(requestDTO.getNic());
        teamMember.setJoinDate(requestDTO.getJoinDate());
        teamMember.setGender(requestDTO.getGender());
        teamMember.setSpecializations(requestDTO.getSpecializations());
        // Enum conversion for availabilityStatus
        if (requestDTO.getAvailabilityStatus() != null) {
            teamMember.setAvailabilityStatus(
                    Team_Member.AvailabilityStatus.valueOf(requestDTO.getAvailabilityStatus().toUpperCase())
            );
        }

        // Save to DB
        Team_Member saved = teamMemberRepository.save(teamMember);

        // Map entity to response DTO
        TeamMemberResponseDTO responseDTO = new TeamMemberResponseDTO();
        responseDTO.setEmpId(saved.getEmpId());
        responseDTO.setName(saved.getName());
        responseDTO.setEmail(saved.getEmail());
        responseDTO.setPhone(saved.getPhone());
        responseDTO.setDepartment(saved.getDepartment());
        responseDTO.setExperience(saved.getExperience());
        responseDTO.setNic(saved.getNic());
        responseDTO.setJoinDate(saved.getJoinDate());
        responseDTO.setGender(saved.getGender());
        responseDTO.setSpecializations(saved.getSpecializations());
        responseDTO.setAvailabilityStatus(saved.getAvailabilityStatus() != null
                ? saved.getAvailabilityStatus().name()
                : null);

        return responseDTO;
    }

    public List<TeamMemberResponseDTO> getAllTeamMembers() {
        List<Team_Member> teamMembers = teamMemberRepository.findAll();

        return teamMembers.stream().map(member -> {
            TeamMemberResponseDTO dto = new TeamMemberResponseDTO();
            dto.setEmpId(member.getEmpId());
            dto.setName(member.getName());
            dto.setNic(member.getNic());
            dto.setEmail(member.getEmail());
            dto.setPhone(member.getPhone());
            dto.setGender(member.getGender());
            dto.setDepartment(member.getDepartment());
            dto.setExperience(member.getExperience());
            dto.setJoinDate(member.getJoinDate());
            dto.setSpecializations(member.getSpecializations());
            dto.setAvailabilityStatus(member.getAvailabilityStatus() != null
                    ? member.getAvailabilityStatus().name()
                    : null);
            return dto;
        }).collect(Collectors.toList());
    }

    public Optional<TeamMemberResponseDTO> getTeamMember(String empId) {
        return teamMemberRepository.findById(empId)
                .map(this::mapToResponseDTO);
    }

    public TeamMemberResponseDTO updateTeamMemberStatus(String empId, String newStatus) {
        Team_Member teamMember = teamMemberRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Team member not found with empId: " + empId));

        // Convert string status to enum
        try {
            Team_Member.AvailabilityStatus status = Team_Member.AvailabilityStatus.valueOf(newStatus.toUpperCase());
            teamMember.setAvailabilityStatus(status);

            Team_Member updatedMember = teamMemberRepository.save(teamMember);
            return mapToResponseDTO(updatedMember);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + newStatus);
        }
    }

    public TeamMemberResponseDTO mapToResponseDTO(Team_Member teamMember) {
        TeamMemberResponseDTO dto = new TeamMemberResponseDTO();
        dto.setEmpId(teamMember.getEmpId());
        dto.setName(teamMember.getName());
        dto.setNic(teamMember.getNic());
        dto.setEmail(teamMember.getEmail());
        dto.setPhone(teamMember.getPhone());
        dto.setGender(teamMember.getGender());
        dto.setSpecializations(teamMember.getSpecializations());
        dto.setDepartment(teamMember.getDepartment());
        dto.setExperience(teamMember.getExperience());
        dto.setJoinDate(teamMember.getJoinDate());
        dto.setAvailabilityStatus(
                teamMember.getAvailabilityStatus() != null
                        ? teamMember.getAvailabilityStatus().name()
                        : null
        );

        return dto;
    }
}
