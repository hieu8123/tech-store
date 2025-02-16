package com.example.tech_store.DTO.response;

import com.example.tech_store.model.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private UUID id;
    private String name;
    private Integer price;
    private Integer oldPrice;
    private String[] imageList;
    private String description;
    private String specification;
    private Integer buyTurn = 0;
    private Integer quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    public static ProductResponseDTO fromProduct(Product product) {
        return ProductResponseDTO
                .builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .oldPrice(product.getOldPrice())
                .imageList(product.getImage().split(","))
                .description(product.getDescription())
                .specification(product.getSpecification())
                .buyTurn(product.getBuyTurn())
                .quantity(product.getQuantity())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
