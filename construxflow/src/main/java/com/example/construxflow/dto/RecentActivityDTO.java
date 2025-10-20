package com.example.construxflow.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecentActivityDTO {
    private String type;
    private String description;
    private String user;
    private LocalDateTime timestamp;
    private String status;
}