package com.passArena.auth_service.service;

import com.passArena.auth_service.entity.RefreshToken;
import com.passArena.auth_service.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String createRefreshToken(UUID userId)
    {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);

        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .tokenHash(tokenHash)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration/100))
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();

        refreshTokenRepository.save(refreshToken);

        return rawToken;

    }

    @Transactional(readOnly = true)
    public RefreshToken validateRefreshToken(String rawToken)
    {
        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(refreshToken.isRevoked())
        {
            throw new RuntimeException("Refresh token is expired");
        }

        if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now()))
        {
            throw new RuntimeException("Refresh token is expired");
        }
        return refreshToken;
    }

    @Transactional
    public void revokeToken(String rawToken)
    {
        String tokenHash = hashToken(rawToken);
        refreshTokenRepository
                .findByTokenHash(tokenHash)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    token.setRevokedAt(LocalDateTime.now());
                    refreshTokenRepository.save(token);
                });
    }

    private String hashToken(String token)
    {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder()
                    .encodeToString(hash);
        }catch (NoSuchAlgorithmException e)
        {
            throw new IllegalStateException("SHA-256 algorithm not found", e);
        }
    }

    @Transactional
    public void deleteAllTokensForUser(UUID userId)
    {
        refreshTokenRepository.deleteByUserId(userId);
    }

}
