package com.example.tech_store.DTO.request;


import com.example.tech_store.model.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
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

    public Product toProduct() {
        return Product.builder()
                .name(name)
                .price(price)
                .oldPrice(oldPrice)
                .image(String.join(",", imageList))
                .description(description)
                .specification(specification)
                .buyTurn(buyTurn)
                .quantity(quantity)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
