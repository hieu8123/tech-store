package com.example.tech_store.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PagedDataDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    public PagedDataDTO(Page<T> pageData) {
        this.content = pageData.getContent();
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalElements = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
    }
}

