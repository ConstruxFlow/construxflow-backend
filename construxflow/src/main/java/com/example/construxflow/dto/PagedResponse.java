package com.example.construxflow.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PagedResponse<T> {
    private List<T> content;
    private int page;           // 0-based
    private int size;
    private long totalElements;
    private int totalPages;
    private String sortBy;
    private String sortDir;
}
