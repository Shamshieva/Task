package com.manas.service.impl;

import java.beans.SimpleBeanInfo;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.manas.config.jwt.JwtService;
import com.manas.dto.request.AuthenticationRequest;
import com.manas.dto.request.TokenRefreshRequest;
import com.manas.dto.response.JwtResponse;
import com.manas.dto.response.SimpleResponse;
import com.manas.dto.response.TokenRefreshResponse;
import com.manas.entity.RefreshToken;
import com.manas.entity.User;
import com.manas.exceptions.NotFoundException;
import com.manas.exceptions.TokenRefreshException;
import com.manas.repository.UserRepository;
import com.manas.service.RefreshTokenService;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.manas.repository.RefreshTokenRepository;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final Long refreshTokenDurationMs = 360000L;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signIn request");
        }
        return token;
    }

    @Override
    public ResponseEntity<?> authenticateUser(AuthenticationRequest request) {
        if (!userRepository.existsByEmail(request.email())){
            throw new NotFoundException(String.format("User with email %s doesn't exist!", request.email()));
        }
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()->new UsernameNotFoundException(
                        String.format("User with email %s doesn't exist!", request.email()))
                );
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String jwt = jwtService.generateToken(user);
        RefreshToken refreshToken = createRefreshToken(user.getId());
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken.getToken(),
                user.getId(),
                user.getEmail(),
                user.getRole().name()));
    }

    @Transactional
    public SimpleResponse deleteByUserId(Long userId) {
        refreshTokenRepository.deleteById(userId);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Deleted!")
                .build();
    }
}