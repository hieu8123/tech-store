package com.example.tech_store.controller;

import com.example.tech_store.DTO.request.ProductRequestDTO;
import com.example.tech_store.DTO.response.ApiResponseDTO;
import com.example.tech_store.DTO.response.PagedResponseDTO;
import com.example.tech_store.DTO.response.ProductResponseDTO;
import com.example.tech_store.enums.CategorySortField;
import com.example.tech_store.enums.ProductSortField;
import com.example.tech_store.enums.SortDirection;
import com.example.tech_store.services.ProductService;
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
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponseDTO> productPage = productService.filterProducts(null, null, null, null, null, pageable);

        PagedResponseDTO<ProductResponseDTO> pagedResponse = PagedResponseDTO.<ProductResponseDTO>builder()
                .content(productPage.getContent())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .build();

        ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>> response = ApiResponseDTO.<PagedResponseDTO<ProductResponseDTO>>builder()
                .timestamp(new Date())
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Product list retrieved successfully")
                .data(pagedResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> getProductById(@PathVariable UUID id) {
        Optional<ProductResponseDTO> product = productService.getProductById(id);

        ApiResponseDTO<ProductResponseDTO> response = product.map(p -> ApiResponseDTO.<ProductResponseDTO>builder()
                        .timestamp(new Date())
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("Product retrieved successfully")
                        .data(p)
                        .build())
                .orElse(ApiResponseDTO.<ProductResponseDTO>builder()
                        .timestamp(new Date())
                        .success(false)
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Product not found")
                        .build());

        return product.isPresent() ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> createProduct(@RequestBody ProductRequestDTO productDTO) {
        ProductResponseDTO responseDTO = productService.createProduct(productDTO.toProduct());

        ApiResponseDTO<ProductResponseDTO> response = ApiResponseDTO.<ProductResponseDTO>builder()
                .timestamp(new Date())
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Product created successfully")
                .data(responseDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> updateProduct(@PathVariable UUID id, @RequestBody ProductRequestDTO productDTO) {
        try {

            ProductResponseDTO responseDTO = productService.updateProduct(id, productDTO.toProduct());

            ApiResponseDTO<ProductResponseDTO> response = ApiResponseDTO.<ProductResponseDTO>builder()
                    .timestamp(new Date())
                    .success(true)
                    .status(HttpStatus.OK.value())
                    .message("Product updated successfully")
                    .data(responseDTO)
                    .build();

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponseDTO<ProductResponseDTO> errorResponse = ApiResponseDTO.<ProductResponseDTO>builder()
                    .timestamp(new Date())
                    .success(false)
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Product not found")
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteProduct(@PathVariable UUID id) {
        try {
            productService.deleteProduct(id);

            ApiResponseDTO<Void> response = ApiResponseDTO.<Void>builder()
                    .timestamp(new Date())
                    .success(true)
                    .status(HttpStatus.OK.value())
                    .message("Product deleted successfully")
                    .build();

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponseDTO<Void> errorResponse = ApiResponseDTO.<Void>builder()
                    .timestamp(new Date())
                    .success(false)
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Product not found")
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>>> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) UUID brandId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by",
                    schema = @Schema(
                            allowableValues = {"NAME", "CREATEDAT", "ID", "PRICE"},
                            defaultValue = "CREATEDAT"
                    ))
            @RequestParam(defaultValue = "CREATEDAT") String sortBy,
            @Parameter(description = "Sort direction",
                    schema = @Schema(
                            allowableValues = {"ASC", "DESC"},
                            defaultValue = "ASC"
                    ))
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        ProductSortField sortFieldEnum = ProductSortField.fromValue(sortBy);
        SortDirection sortDirectionEnum = SortDirection.fromValue(sortDirection);
        String sortField = sortFieldEnum.getValue();

        Sort.Direction direction = sortDirectionEnum == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(offset / size, size, sort);
        Page<ProductResponseDTO> productPage = productService.filterProducts(name, minPrice, maxPrice, brandId, categoryId, pageable);

        PagedResponseDTO<ProductResponseDTO> pagedResponse = PagedResponseDTO.<ProductResponseDTO>builder()
                .content(productPage.getContent())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .build();

        ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>> response = ApiResponseDTO.<PagedResponseDTO<ProductResponseDTO>>builder()
                .timestamp(new Date())
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Filtered product list retrieved successfully")
                .data(pagedResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
