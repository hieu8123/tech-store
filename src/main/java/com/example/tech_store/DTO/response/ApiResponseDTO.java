package com.example.tech_store.DTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> {
    @Schema(description = "Time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date timestamp;

    @Schema(description = "Is request success")
    private boolean success;

    @Schema(description = "HTTP status code")
    private int status;

    @Schema(description = "Detailed message")
    private String message;

    @Schema(description = "Response data")
    private T data;
}
