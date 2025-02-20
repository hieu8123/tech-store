package com.example.tech_store.services;

import com.example.tech_store.DTO.request.AddressRequestDTO;
import com.example.tech_store.DTO.response.AddressResponseDTO;
import com.example.tech_store.exception.ForbiddenException;
import com.example.tech_store.model.Address;
import com.example.tech_store.model.User;
import com.example.tech_store.repository.AddressRepository;
import com.example.tech_store.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public AddressResponseDTO addAddress(UUID userId, AddressRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        Address newAddress = Address.builder()
                .user(user)
                .address(request.getAddress())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .isPrimary(request.getIsPrimary())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        if (request.getIsPrimary()) {
            user.getAddresses().forEach(addr -> addr.setIsPrimary(false));
        }

        addressRepository.save(newAddress);
        return AddressResponseDTO.fromAddress(newAddress);
    }

    @Transactional
    public AddressResponseDTO updateAddress(UUID userId, UUID addressId, AddressRequestDTO request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to update this address");
        }

        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        address.setUpdatedAt(new Date());

        if (request.getIsPrimary()) {
            List<Address> userAddresses = address.getUser().getAddresses();
            userAddresses.forEach(addr -> addr.setIsPrimary(false));
            address.setIsPrimary(true);
        }

        addressRepository.save(address);
        return AddressResponseDTO.fromAddress(address);
    }

    @Transactional
    public void deleteAddress(UUID userId, UUID addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to delete this address");
        }

        addressRepository.delete(address);
    }

    public Page<AddressResponseDTO> getUserAddresses(UUID userId, Pageable pageable) {
        Page<Address> addressPage = addressRepository.findByUserId(userId, pageable);
        return addressPage.map(AddressResponseDTO::fromAddress);
    }
}
