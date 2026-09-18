package com.dataverse.dataverserentals.security.controllers;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.security.config.JwtConfig;
import com.dataverse.dataverserentals.security.controllers.dtos.request.LoginRequest;
import com.dataverse.dataverserentals.security.controllers.dtos.response.LoginResponse;
import com.dataverse.dataverserentals.security.controllers.dtos.response.RefreshResponse;
import com.dataverse.dataverserentals.common.helpers.ClientInfoUtil;
import com.dataverse.dataverserentals.security.exceptions.CredencialesInvalidasException;
import com.dataverse.dataverserentals.security.exceptions.InvalidRefreshTokenException;
import com.dataverse.dataverserentals.security.exceptions.UsuarioBloqueadoException;
import com.dataverse.dataverserentals.security.exceptions.UsuarioInactivoException;
import com.dataverse.dataverserentals.security.services.AuthenticationService;
import com.dataverse.dataverserentals.security.services.JwtService;
import com.dataverse.dataverserentals.security.services.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse httpResponse)
            throws CredencialesInvalidasException, UsuarioInactivoException, UsuarioBloqueadoException,
            RecordNotFoundException {
        log.info("Intento de autenticación para usuario: {}", request.nombreUsuario());
        LoginResponse loginResponse = authenticationService.authenticate(request);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("refresh")
    public ResponseEntity<RefreshResponse> refresh(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws InvalidRefreshTokenException, RecordNotFoundException {
        String refreshTokenValue = extractRefreshToken(httpRequest);

        if (refreshTokenValue == null) {
            throw new InvalidRefreshTokenException("Refresh token no encontrado en la cookie");
        }

        String userAgent = ClientInfoUtil.extractUserAgent(httpRequest);
        String ipAddress = ClientInfoUtil.extractIp(httpRequest);

        var newRefreshToken = refreshTokenService.rotateRefreshToken(refreshTokenValue, userAgent, ipAddress);
        var usuario = authenticationService.getUserById(newRefreshToken.getUsuarioId());
        String newAccessToken = jwtService.tokenForRoleAssume(usuario);

        Cookie accessCookie = jwtService.toShortCookie(newAccessToken);
        Cookie refreshCookie = jwtService.toRefreshCookie(newRefreshToken.getToken());
        httpResponse.addCookie(accessCookie);
        httpResponse.addCookie(refreshCookie);

        return ResponseEntity.ok(new RefreshResponse(newAccessToken, jwtConfig.getTipo()));
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.info("Cerrando sesión de usuario");

        String refreshTokenValue = extractRefreshToken(httpRequest);
        if (refreshTokenValue != null) {
            refreshTokenService.revokeToken(refreshTokenValue);
        }

        Cookie jwtCookie = jwtService.toLogoutCookie();
        Cookie clearRefreshCookie = jwtService.toClearRefreshCookie();
        httpResponse.addCookie(jwtCookie);
        httpResponse.addCookie(clearRefreshCookie);
        return ResponseEntity.ok().build();
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> refreshCookie = Arrays.stream(cookies)
                    .filter(c -> jwtConfig.getRefreshCookieName().equals(c.getName()))
                    .findFirst();
            if (refreshCookie.isPresent()) {
                return refreshCookie.get().getValue();
            }
        }
        return null;
    }

}