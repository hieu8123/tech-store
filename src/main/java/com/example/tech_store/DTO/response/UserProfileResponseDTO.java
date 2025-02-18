package com.example.tech_store.DTO.response;

import com.example.tech_store.enums.Role;
import com.example.tech_store.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponseDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AddressDTO {
        private String address;
        private String city;
        private String postalCode;
        private Boolean isPrimary;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
        private Date createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
        private Date updatedAt;
    }

    private String email;
    private String username;
    private String phoneNumber;
    private String avatar;
    private Role role;
    private List<AddressDTO> addresses;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    public static UserProfileResponseDTO fromUser(User user) {
        return UserProfileResponseDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .addresses(user.getAddresses() != null
                        ? user.getAddresses().stream()
                        .map(address -> AddressDTO.builder()
                                .address(address.getAddress())
                                .city(address.getCity())
                                .postalCode(address.getPostalCode())
                                .isPrimary(address.getIsPrimary())
                                .createdAt(address.getCreatedAt())
                                .updatedAt(address.getUpdatedAt())
                                .build())
                        .collect(Collectors.toList())
                        : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
