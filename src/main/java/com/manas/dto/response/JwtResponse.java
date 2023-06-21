package com.manas.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String email;
//    private List<String> roles;
    private String role;

    public JwtResponse(String accessToken, String refreshToken, Long id, String email, String role) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.email = email;
        this.role = role;
    }
}