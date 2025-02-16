package com.example.tech_store.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PagedResponseDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
}

