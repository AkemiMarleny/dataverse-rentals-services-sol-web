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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Slf4j
@Tag(name = "Autenticación", description = "Login, refresh y logout con JWT")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y devuelve el access token JWT en el cuerpo de la respuesta, o un token temporal si el flujo MFA está pendiente.")
    @ApiResponse(responseCode = "200", description = "Credenciales válidas; la respuesta incluye el token JWT")
    @ApiResponse(responseCode = "422", description = "Credenciales inválidas, usuario inactivo o bloqueado")
    @SecurityRequirements
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse httpResponse)
            throws CredencialesInvalidasException, UsuarioInactivoException, UsuarioBloqueadoException,
            RecordNotFoundException {
        log.info("Intento de autenticación para usuario: {}", request.nombreUsuario());
        LoginResponse loginResponse = authenticationService.authenticate(request);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("refresh")
    @Operation(
            summary = "Renovar access token",
            description = "Rota el refresh token enviado como cookie y devuelve un nuevo access token JWT.")
    @ApiResponse(responseCode = "200", description = "Nuevo access token generado y cookies renovadas")
    @ApiResponse(responseCode = "401", description = "Refresh token ausente, inválido o revocado")
    @ApiResponse(responseCode = "404", description = "Usuario asociado al token no encontrado")
    @SecurityRequirements
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
    @Operation(
            summary = "Cerrar sesión",
            description = "Revoca el refresh token y limpia las cookies de sesión.")
    @ApiResponse(responseCode = "200", description = "Sesión cerrada exitosamente")
    @SecurityRequirements
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