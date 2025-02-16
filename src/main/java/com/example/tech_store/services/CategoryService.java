package com.example.tech_store.services;

import com.example.tech_store.DTO.response.CategoryResponseDTO;
import com.example.tech_store.model.Category;
import com.example.tech_store.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<CategoryResponseDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(CategoryResponseDTO::fromCategory);
    }

    public Optional<CategoryResponseDTO> getCategoryById(UUID id) {
        return categoryRepository.findById(id).map(CategoryResponseDTO::fromCategory);
    }

    @Transactional
    public CategoryResponseDTO createCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponseDTO.fromCategory(savedCategory);
    }

    @Transactional
    public Optional<CategoryResponseDTO> updateCategory(UUID id, Category categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoryDetails.getName());
            category.setImage(categoryDetails.getImage());
            Category updatedCategory = categoryRepository.save(category);
            return CategoryResponseDTO.fromCategory(updatedCategory);
        });
    }

    @Transactional
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }
}
