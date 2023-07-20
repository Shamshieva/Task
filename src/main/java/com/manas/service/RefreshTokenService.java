package com.manas.service;

import com.manas.dto.request.AuthenticationRequest;
import com.manas.dto.response.SimpleResponse;
import com.manas.entity.RefreshToken;
import io.micrometer.observation.ObservationFilter;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface RefreshTokenService {
    ResponseEntity<?> authenticateUser(AuthenticationRequest request);

    RefreshToken verifyExpiration(RefreshToken token);

    RefreshToken createRefreshToken(Long userId);

    Optional<RefreshToken> findByToken(String token);
    SimpleResponse deleteByUserId(Long userId);
}
