package com.manas.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record RegisterRequest(
        @NotNull(message = "Name shouldn't be null!")
        String fullName,
        byte[] image,
        @NotNull(message = "Email shouldn't be null!")
        @Email(message = "Invalid email!")
        String email,
        String password
) {
}
