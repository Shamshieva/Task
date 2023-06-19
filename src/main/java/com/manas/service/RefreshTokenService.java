package com.manas.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.manas.entity.RefreshToken;
import com.manas.exceptions.TokenRefreshException;
import com.manas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.manas.repository.RefreshTokenRepository;
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

//    @Value("${bezkoder.app.jwtRefreshExpirationMs}")
    private final Long refreshTokenDurationMs = 7439047207429847932L;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signIn request");
        }
        return token;
    }

//    @Transactional
//    public void deleteByUserId(Long userId) {
//        refreshTokenRepository.deleteById(userId);
//    }
}