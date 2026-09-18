package com.dataverse.dataverserentals.security.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataverse.dataverserentals.security.config.JwtConfig;
import com.dataverse.dataverserentals.security.entities.RefreshToken;
import com.dataverse.dataverserentals.security.exceptions.InvalidRefreshTokenException;
import com.dataverse.dataverserentals.security.repositories.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final int MAX_ACTIVE_SESSIONS = 5;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;

    @Transactional
    public RefreshToken createRefreshToken(Long usuarioId) {
        enforceSessionLimit(usuarioId);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .usuarioId(usuarioId)
                .expiryDate(LocalDateTime.now().plus(jwtConfig.getRefreshExpiration(), ChronoUnit.MILLIS))
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken rotateRefreshToken(String tokenValue) throws InvalidRefreshTokenException {
        RefreshToken oldToken = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token no encontrado"));

        if (!oldToken.isValid()) {
            throw new InvalidRefreshTokenException("Refresh token inválido o expirado");
        }

        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        return createRefreshToken(oldToken.getUsuarioId());
    }

    @Transactional
    public void revokeToken(String tokenValue) {
        refreshTokenRepository.findByToken(tokenValue)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                    log.info("Refresh token revocado para usuarioId={}", token.getUsuarioId());
                });
    }

    @Transactional
    public void revokeAllForUser(Long usuarioId) {
        refreshTokenRepository.revokeAllByUsuarioId(usuarioId);
        log.info("Todos los refresh tokens revocados para usuarioId={}", usuarioId);
    }

    public long countActiveSessions(Long usuarioId) {
        return refreshTokenRepository.countByUsuarioIdAndRevokedFalse(usuarioId);
    }

    private void enforceSessionLimit(Long usuarioId) {
        long active = countActiveSessions(usuarioId);
        if (active >= MAX_ACTIVE_SESSIONS) {
            var tokens = refreshTokenRepository.findAllByUsuarioIdAndRevokedFalse(usuarioId);
            tokens.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
            tokens.stream()
                    .limit(active - MAX_ACTIVE_SESSIONS + 1)
                    .forEach(token -> {
                        token.setRevoked(true);
                        refreshTokenRepository.save(token);
                    });
            log.info("Límite de sesiones alcanzado para usuarioId={}, se revocó la más antigua", usuarioId);
        }
    }
}
