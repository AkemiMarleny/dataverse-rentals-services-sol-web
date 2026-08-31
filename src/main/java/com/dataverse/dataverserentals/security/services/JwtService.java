package com.dataverse.dataverserentals.security.services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.security.config.JwtConfig;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final String CLAIM_ROLE = "urol";
    private final String CLAIM_USUARIO_ID = "uid";
    private final String CLAIM_PROPIETARIO = "uown";

    private final JwtConfig jwtConfig;

    public String tokenFrom(Usuario usuario) {
        SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtConfig.getExpiration());

        return Jwts.builder()
                .subject(usuario.getNombreUsuario())
                .claim(CLAIM_ROLE, usuario.getRolCode())
                .claim(CLAIM_USUARIO_ID, usuario.getId())
                .claim(CLAIM_PROPIETARIO, usuario.getPersonaId())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UsuarioPrincipal getUsuarioPrincipalFromToken(String token) {

        Claims claims = getTokenPayload(token);

        String usuarioNombre = claims.getSubject();
        Long usuarioId = claims.get(CLAIM_USUARIO_ID, Long.class);
        RolUsuario usuarioRol = RolUsuario.valueOf(claims.get(CLAIM_ROLE, String.class));

        return new UsuarioPrincipal(usuarioId, usuarioNombre, usuarioRol);
    }

    private Claims getTokenPayload(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }
}