package com.manas.controller;

import com.manas.config.jwt.JwtService;
import com.manas.dto.request.AuthenticationRequest;
import com.manas.dto.request.RegisterRequest;
import com.manas.dto.request.TokenRefreshRequest;
import com.manas.dto.response.AuthenticationResponse;
import com.manas.dto.response.TokenRefreshResponse;
import com.manas.entity.User;
import com.manas.exceptions.TokenRefreshException;
import com.manas.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manas.entity.RefreshToken;
import com.manas.service.RefreshTokenService;
import com.manas.dto.response.JwtResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @PostMapping("/signUp")
    public AuthenticationResponse signUp(@RequestBody @Valid RegisterRequest request){
        return userService.register(request);
    }

    @PostMapping("/signIn")
    public AuthenticationResponse authenticate( @RequestBody AuthenticationRequest request) {
        return userService.authenticate(request);
    }

//    @PostMapping("/signIn")
//    public ResponseEntity<?>  authenticate( @RequestBody AuthenticationRequest request) {
//        return refreshTokenService.authenticateUser(request);
//    }


    @PostMapping("/refreshToken")
    public TokenRefreshResponse refreshToken(@RequestBody String requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
        refreshTokenService.verifyExpiration(refreshToken);
        String token = jwtService.generateToken(refreshToken.getUser());
        return new TokenRefreshResponse(token, requestRefreshToken);
    }
}