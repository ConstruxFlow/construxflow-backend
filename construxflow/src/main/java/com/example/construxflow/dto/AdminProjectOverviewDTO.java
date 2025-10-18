package com.example.construxflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminProjectOverviewDTO {
    private String projectId;
    private String projectName;
    private String startDate;
    private String endDate;
    private String location;

    // Included to feed the "Status" column (maps from Project.progressStatus)
    private String progressStatus;
}