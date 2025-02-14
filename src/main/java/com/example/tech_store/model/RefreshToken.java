package com.example.tech_store.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Tự động sinh UUID
    private UUID id;  // Thay Long thành UUID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Giữ nguyên liên kết với User (vì User giờ dùng UUID)

    @Column(unique = true)
    private String token;

    public RefreshToken(User user, String token) {
        this.user = user;
        this.token = token;
    }
}
