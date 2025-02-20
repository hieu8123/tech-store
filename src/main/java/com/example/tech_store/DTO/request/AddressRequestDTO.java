package com.example.tech_store.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequestDTO {
    private String address;
    private String city;
    private String postalCode;
    private Boolean isPrimary;
}
