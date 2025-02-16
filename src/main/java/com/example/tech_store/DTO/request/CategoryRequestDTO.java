package com.example.tech_store.DTO.request;

import com.example.tech_store.model.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {
    @NotBlank(message = "Category name is blank")
    private String name;

    @NotBlank(message = "Category image is blank")
    private String image;

    public Category toCategory(){
        return Category.builder()
                .name(name)
                .image(image)
                .build();
    }
}
