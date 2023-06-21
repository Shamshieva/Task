package com.manas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticationRequest(
        @NotBlank(message = "Email shouldn't be null!")
        String email,
        @NotBlank(message = "Пароль shouldn't be null!")
        String password
) {
}
