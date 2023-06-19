package com.manas.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenRefreshRequest {
//    @NotBlank
    private String refreshToken;

}
