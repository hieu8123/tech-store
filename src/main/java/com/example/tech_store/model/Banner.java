package com.example.tech_store.model;

import com.example.tech_store.enums.BannerStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "banners")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String image;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BannerStatus status = BannerStatus.INACTIVE;

    @OneToOne
    @JoinColumn(name = "product_id", unique = true)
    private Product product;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

