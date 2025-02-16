package com.example.tech_store.services;

import com.example.tech_store.DTO.response.ProductResponseDTO;
import com.example.tech_store.model.Product;
import com.example.tech_store.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponseDTO::fromProduct)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponseDTO> getProductById(UUID id) {
        return productRepository.findById(id).map(ProductResponseDTO::fromProduct);
    }

    @Transactional
    public ProductResponseDTO createProduct(Product product) {
        return ProductResponseDTO.fromProduct(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDTO updateProduct(UUID id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    existingProduct.setOldPrice(updatedProduct.getOldPrice());
                    existingProduct.setImage(updatedProduct.getImage());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setSpecification(updatedProduct.getSpecification());
                    existingProduct.setBuyTurn(updatedProduct.getBuyTurn());
                    existingProduct.setQuantity(updatedProduct.getQuantity());
                    existingProduct.setBrand(updatedProduct.getBrand());
                    existingProduct.setCategory(updatedProduct.getCategory());
                    return ProductResponseDTO.fromProduct(productRepository.save(existingProduct));
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public Page<ProductResponseDTO> filterProducts(String name, Integer minPrice, Integer maxPrice, UUID brandId, UUID categoryId, Pageable pageable) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (minPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (brandId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("brand").get("id"), brandId));
            }
            if (categoryId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            return predicate;
        };

        return productRepository.findAll(spec, pageable).map(ProductResponseDTO::fromProduct);
    }
}
