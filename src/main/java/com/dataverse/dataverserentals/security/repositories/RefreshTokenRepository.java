package com.dataverse.dataverserentals.security.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dataverse.dataverserentals.security.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUsuarioIdAndRevokedFalse(Long usuarioId);

    long countByUsuarioIdAndRevokedFalse(Long usuarioId);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.usuarioId = :usuarioId")
    void revokeAllByUsuarioId(Long usuarioId);

    void deleteAllByExpiryDateBefore(LocalDateTime before);
}
