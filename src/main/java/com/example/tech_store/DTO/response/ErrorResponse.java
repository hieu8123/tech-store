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
@Schema(description = "Standard error response structure")
public class ErrorResponse {
    @Schema(description = "Error time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date timestamp;

    @Schema(description = "HTTP status code")
    private int status;

    @Schema(description = "Request path")
    private String path;

    @Schema(description = "Error type")
    private String error;

    @Schema(description = "Detailed error message")
    private String message;
}
