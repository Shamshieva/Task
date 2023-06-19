package com.manas.dto.request;

import lombok.Builder;

@Builder
public record LoginRequest(
        String username,
        String password
) {
}
