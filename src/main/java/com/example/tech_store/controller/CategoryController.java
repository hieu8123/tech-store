package com.example.tech_store.controller;

import com.example.tech_store.DTO.request.CategoryRequestDTO;
import com.example.tech_store.DTO.response.ApiResponseDTO;
import com.example.tech_store.DTO.response.CategoryResponseDTO;
import com.example.tech_store.DTO.response.PagedResponseDTO;
import com.example.tech_store.constants.ApiConstants;
import com.example.tech_store.enums.CategorySortField;
import com.example.tech_store.enums.SortDirection;
import com.example.tech_store.services.CategoryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.Endpoints.CATEGORIES)
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<CategoryResponseDTO>>> getAllCategories(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by",
                    schema = @Schema(
                            allowableValues = {"NAME", "CREATEDAT", "ID"},
                            defaultValue = "CREATEDAT"
                    ))
            @RequestParam(defaultValue = "CREATEDAT") String sortBy,
            @Parameter(description = "Sort direction",
                    schema = @Schema(
                            allowableValues = {"ASC", "DESC"},
                            defaultValue = "ASC"
                    ))
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        CategorySortField sortFieldEnum = CategorySortField.fromValue(sortBy);
        SortDirection sortDirectionEnum = SortDirection.fromValue(sortDirection);
        String sortField = sortFieldEnum.getValue();

        Sort.Direction direction = sortDirectionEnum == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(offset / size, size, sort);
        Page<CategoryResponseDTO> categoryPage = categoryService.getAllCategories(pageable);

        PagedResponseDTO<CategoryResponseDTO> responseDTO = PagedResponseDTO.<CategoryResponseDTO>builder()
                .content(categoryPage.getContent())
                .page(categoryPage.getNumber())
                .size(categoryPage.getSize())
                .totalPages(categoryPage.getTotalPages())
                .totalElements(categoryPage.getTotalElements())
                .build();

        return ResponseEntity.ok(
                ApiResponseDTO.<PagedResponseDTO<CategoryResponseDTO>>builder()
                        .timestamp(new Date())
                        .success(true)
                        .status(200)
                        .message("Categories fetched successfully")
                        .data(responseDTO)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> getCategoryById(@PathVariable UUID id) {
        Optional<CategoryResponseDTO> category = categoryService.getCategoryById(id);
        return category.map(categoryResponseDTO -> ResponseEntity.ok(
                ApiResponseDTO.<CategoryResponseDTO>builder()
                        .timestamp(new Date())
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("Category fetched successfully")
                        .data(categoryResponseDTO)
                        .build()
        )).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.<CategoryResponseDTO>builder()
                        .timestamp(new Date())
                        .success(false)
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Category not found")
                        .data(null)
                        .build()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> createCategory(
            @RequestBody CategoryRequestDTO createCategoryRequestDTO) {
        CategoryResponseDTO createdCategory = categoryService.createCategory(createCategoryRequestDTO.toCategory());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.<CategoryResponseDTO>builder()
                        .timestamp(new Date())
                        .success(true)
                        .status(HttpStatus.CREATED.value())
                        .message("Category created successfully")
                        .data(createdCategory)
                        .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CategoryResponseDTO>> updateCategory(
            @PathVariable UUID id,
            @RequestBody CategoryRequestDTO updateCategoryRequestDTO) {
        Optional<CategoryResponseDTO> updatedCategory = categoryService.updateCategory(id, updateCategoryRequestDTO.toCategory());

        return updatedCategory.map(categoryResponseDTO -> ResponseEntity.ok(
                ApiResponseDTO.<CategoryResponseDTO>builder()
                        .timestamp(new Date())
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("Category updated successfully")
                        .data(categoryResponseDTO)
                        .build()
        )).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.<CategoryResponseDTO>builder()
                        .timestamp(new Date())
                        .success(false)
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Category not found")
                        .data(null)
                        .build()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                ApiResponseDTO.<Void>builder()
                        .timestamp(new Date())
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("Category deleted successfully")
                        .data(null)
                        .build()
        );
    }
}
