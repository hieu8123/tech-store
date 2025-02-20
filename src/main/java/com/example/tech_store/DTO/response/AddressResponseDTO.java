package com.example.tech_store.DTO.response;

import com.example.tech_store.model.Address;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponseDTO {
    private UUID addressId;
    private String address;
    private String city;
    private String postalCode;
    private Boolean isPrimary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    public static AddressResponseDTO fromAddress(Address address) {
        return AddressResponseDTO.builder()
                .addressId(address.getId())
                .address(address.getAddress())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .isPrimary(address.getIsPrimary())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }
}
