package com.example.tech_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products",
        indexes = {
                @Index(name = "idx_name", columnList = "name"),
                @Index(name = "idx_price", columnList = "price"),
                @Index(name = "idx_old_price", columnList = "oldPrice"),
                @Index(name = "idx_brand", columnList = "brand_id"),
                @Index(name = "idx_category", columnList = "category_id"),
                @Index(name = "idx_created_at", columnList = "createdAt")
        })
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    private Integer oldPrice;
    private String image;
    private String description;
    private String specification;
    private Integer buyTurn = 0;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}

