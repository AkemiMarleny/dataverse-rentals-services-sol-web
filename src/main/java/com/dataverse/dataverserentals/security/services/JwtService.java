package com.dataverse.dataverserentals.security.services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.security.config.JwtConfig;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;
import com.dataverse.dataverserentals.security.enums.TokenScope;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final long FIVE_MINUTES_MS = 300000;
    private final long TREE_MINUTES_MS = 180000;

    private final String CLAIM_ROLE = "urol";
    private final String CLAIM_USUARIO_ID = "uid";
    private final String CLAIM_PROPIETARIO = "uown";
    private final String CLAIM_SCOPE = "scope";

    private final JwtConfig jwtConfig;

    public Cookie toShortCookie(String token) {
        Cookie jwtCookie = new Cookie(jwtConfig.getCookieName(), token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(jwtConfig.isCookieSecure());
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) (jwtConfig.getShortExpiration() / 1000));

        return jwtCookie;
    }

    public Cookie toLogoutCookie() {
        Cookie jwtCookie = new Cookie(jwtConfig.getCookieName(), null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(jwtConfig.isCookieSecure());
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);

        return jwtCookie;
    }

    public Cookie toRefreshCookie(String token) {
        Cookie refreshCookie = new Cookie(jwtConfig.getRefreshCookieName(), token);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(jwtConfig.isCookieSecure());
        refreshCookie.setPath("/api/v1/auth/refresh");
        refreshCookie.setMaxAge((int) (jwtConfig.getRefreshExpiration() / 1000));

        return refreshCookie;
    }

    public Cookie toClearRefreshCookie() {
        Cookie refreshCookie = new Cookie(jwtConfig.getRefreshCookieName(), null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(jwtConfig.isCookieSecure());
        refreshCookie.setPath("/api/v1/auth/refresh");
        refreshCookie.setMaxAge(0);
        return refreshCookie;
    }

    public String tokenForSetupMFA(Usuario usuario) {
        return tokenBuilder(
                usuario,
                TokenScope.MFA_SETUP,
                FIVE_MINUTES_MS)
                .compact();
    }

    public String tokenForVerify(Usuario usuario) {
        return tokenBuilder(
                usuario,
                TokenScope.MFA_VERIFY,
                TREE_MINUTES_MS)
                .compact();
    }

    public String tokenForRoleAssume(Usuario usuario) {
        return tokenBuilder(
                usuario,
                TokenScope.FULL_ACCESS,
                jwtConfig.getShortExpiration())
                .compact();
    }

    public JwtBuilder tokenBuilder(Usuario usuario, TokenScope scope, long durationInMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + durationInMs);

        SecretKey key = Keys.hmacShaKeyFor(
                jwtConfig.getSecret()
                        .getBytes());

        return Jwts.builder()
                .subject(usuario.getNombreUsuario())
                .claim(CLAIM_ROLE, usuario.getRolCode())
                .claim(CLAIM_USUARIO_ID, usuario.getId())
                .claim(CLAIM_PROPIETARIO, usuario.getPersonaId())
                .claim(CLAIM_SCOPE, scope.name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key);
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
        Long personaId = claims.get(CLAIM_PROPIETARIO, Long.class);
        TokenScope scope = TokenScope.valueOf(claims.get(CLAIM_SCOPE, String.class));

        return new UsuarioPrincipal(usuarioId, usuarioNombre, usuarioRol, personaId, scope);
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