package com.example.tech_store.controller;

import com.example.tech_store.DTO.request.AddressRequestDTO;
import com.example.tech_store.DTO.request.CartRequestDTO;
import com.example.tech_store.DTO.response.*;
import com.example.tech_store.constants.ApiConstants;
import com.example.tech_store.services.AddressService;
import com.example.tech_store.services.CartService;
import com.example.tech_store.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.Endpoints.USERS)
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> getUser(@PathVariable UUID id) {
        Optional<UserProfileResponseDTO> user = userService.getUserProfile(id);
        ApiResponseDTO<UserProfileResponseDTO> response = user.map(u -> ApiResponseDTO.<UserProfileResponseDTO>builder()
                        .timestamp(new Date())
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("Get user profile successfully")
                        .data(u)
                        .build())
                .orElse(ApiResponseDTO.<UserProfileResponseDTO>builder()
                        .timestamp(new Date())
                        .success(false)
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .build());
        return user.isPresent() ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<ApiResponseDTO<PagedDataDTO<AddressResponseDTO>>> getUserAddresses(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AddressResponseDTO> addressPage = addressService.getUserAddresses(id, pageable);
        PagedDataDTO<AddressResponseDTO> pagedResponse = new PagedDataDTO<>(addressPage);

        ApiResponseDTO<PagedDataDTO<AddressResponseDTO>> response = ApiResponseDTO.<PagedDataDTO<AddressResponseDTO>>builder()
                .timestamp(new Date())
                .success(true)
                .status(HttpStatus.OK.value())
                .message("User addresses retrieved successfully")
                .data(pagedResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<ApiResponseDTO<AddressResponseDTO>> addUserAddress(@PathVariable UUID id, @RequestBody AddressRequestDTO request) {
        AddressResponseDTO address = addressService.addAddress(id, request);
        ApiResponseDTO<AddressResponseDTO> response = ApiResponseDTO.<AddressResponseDTO>builder()
                .timestamp(new Date())
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Add user address successfully")
                .data(address)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/addresses/{address_id}")
    public ResponseEntity<ApiResponseDTO<AddressResponseDTO>> updateUserAddress(@PathVariable UUID id,
                                                                                @PathVariable UUID address_id,
                                                                                @RequestBody AddressRequestDTO request) {
        AddressResponseDTO updatedAddress = addressService.updateAddress(id, address_id, request);
        ApiResponseDTO<AddressResponseDTO> response = ApiResponseDTO.<AddressResponseDTO>builder()
                .timestamp(new Date())
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Update user address successfully")
                .data(updatedAddress)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/addresses/{address_id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteUserAddress(@PathVariable UUID id,
                                                                  @PathVariable UUID address_id) {
        addressService.deleteAddress(id, address_id);
        ApiResponseDTO<Void> response = ApiResponseDTO.<Void>builder()
                .timestamp(new Date())
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Delete user address successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/cart")
    public ResponseEntity<ApiResponseDTO<UserCartResponseDTO>> getUserCart(@PathVariable UUID id) {
        Optional<UserCartResponseDTO> userCart = cartService.getUserCart(id);

        return getApiResponseDTOResponseEntity(userCart);
    }

    @PutMapping("/{id}/cart")
    public ResponseEntity<ApiResponseDTO<UserCartResponseDTO>> updateUserCart(@PathVariable UUID id, @RequestBody CartRequestDTO cartRequestDTO) {
        Optional<UserCartResponseDTO> userCart = cartService.updateUserCart(id, cartRequestDTO);

        return getApiResponseDTOResponseEntity(userCart);
    }

    private ResponseEntity<ApiResponseDTO<UserCartResponseDTO>> getApiResponseDTOResponseEntity(Optional<UserCartResponseDTO> userCart) {
        ApiResponseDTO<UserCartResponseDTO> response = userCart.map(u -> ApiResponseDTO.<UserCartResponseDTO>builder()
                        .timestamp(new Date())
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("Get user cart successfully")
                        .data(u)
                        .build())
                .orElseGet(() -> ApiResponseDTO.<UserCartResponseDTO>builder()
                        .timestamp(new Date())
                        .success(false)
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("User not found")
                        .build());

        return ResponseEntity.status(response.getStatus()).body(response);
    }


}