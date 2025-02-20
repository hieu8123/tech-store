package com.example.tech_store.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequestDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private String avatar;
}
