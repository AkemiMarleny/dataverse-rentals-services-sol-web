package com.dataverse.dataverserentals.security.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UsuarioRepository;
import com.dataverse.dataverserentals.security.config.JwtConfig;
import com.dataverse.dataverserentals.security.config.MfaSecretEncryptor;
import com.dataverse.dataverserentals.security.context.SecurityContextImpl;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;
import com.dataverse.dataverserentals.security.controllers.dtos.request.MfaSetupRequest;
import com.dataverse.dataverserentals.security.controllers.dtos.request.MfaVerificationRequest;
import com.dataverse.dataverserentals.security.controllers.dtos.response.MfaSetupResponse;
import com.dataverse.dataverserentals.security.controllers.dtos.response.MfaVerificationResponse;
import com.dataverse.dataverserentals.security.exceptions.InvalidMfaCodeException;
import com.dataverse.dataverserentals.security.exceptions.MfaAlreadyEnabledException;
import com.dataverse.dataverserentals.common.helpers.ClientInfoUtil;
import com.dataverse.dataverserentals.security.services.JwtService;
import com.dataverse.dataverserentals.security.services.MfaService;
import com.dataverse.dataverserentals.security.services.RefreshTokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/auth/mfa")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticación MFA", description = "Configuración y verificación del segundo factor (MFA)")
public class MfaController {

    private static final String MFA_ISSUER = "Dataverse Rentals";

    private final MfaService mfaService;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final SecurityContextImpl securityContext;
    private final MfaSecretEncryptor mfaSecretEncryptor;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("setup")
    @Operation(
            summary = "Iniciar configuración de MFA",
            description = "Genera el secreto TOTP, la URL del código QR y los códigos de respaldo para registrar la app autenticadora.")
    @ApiResponse(responseCode = "200", description = "Secreto, QR y códigos de respaldo generados")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "422", description = "El MFA ya se encuentra activo")
    public ResponseEntity<MfaSetupResponse> setupMfa() throws RecordNotFoundException, MfaAlreadyEnabledException {
        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();

        Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(usuarioPrincipal.usuarioNombre())
                .orElseThrow(() -> new RecordNotFoundException("Usuario no encontrado"));

        if (usuario.getMfaSecret() != null) {
            throw new MfaAlreadyEnabledException(
                    "MFA ya se encuentra activo.");
        }

        String secret = mfaService.generateMfaSecret();
        String username = usuarioPrincipal.usuarioNombre();
        String qrCodeUrl = mfaService.generateQrCodeUrl(secret, username, MFA_ISSUER);
        List<String> backupCodes = mfaService.generateBackupCodes();

        return new ResponseEntity<>(
                new MfaSetupResponse(secret, qrCodeUrl, backupCodes.toArray(new String[0])),
                HttpStatus.OK);
    }

    @PostMapping("verify-setup")
    @Operation(
            summary = "Validar código de configuración",
            description = "Valida un código TOTP contra el secreto antes de habilitar el MFA.")
    @ApiResponse(responseCode = "200", description = "Código TOTP válido")
    @ApiResponse(responseCode = "401", description = "Código MFA inválido")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<MfaVerificationResponse> verifySetup(@RequestBody MfaSetupRequest request)
            throws RecordNotFoundException, InvalidMfaCodeException {
        if (!mfaService.validateTotpCode(request.secret(), Integer.parseInt(request.code()))) {
            throw new InvalidMfaCodeException("Código MFA inválido");
        }

        return new ResponseEntity<>(
                new MfaVerificationResponse(null, null, true, false),
                HttpStatus.OK);
    }

    @PostMapping("enable")
    @Operation(
            summary = "Habilitar MFA",
            description = "Valida el código TOTP, guarda el secreto cifrado y activa el MFA para el usuario autenticado.")
    @ApiResponse(responseCode = "204", description = "MFA habilitado correctamente")
    @ApiResponse(responseCode = "401", description = "Código MFA inválido")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<Void> enableMfa(@RequestBody @Valid MfaSetupRequest request,
            HttpServletResponse httpResponse)
            throws RecordNotFoundException, InvalidMfaCodeException {
        if (!mfaService.validateTotpCode(request.secret(), Integer.parseInt(request.code()))) {
            throw new InvalidMfaCodeException("Código MFA inválido");
        }

        String username = securityContext.getUsuarioPrincipal().usuarioNombre();
        Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(username)
                .orElseThrow(() -> new RecordNotFoundException("Usuario no encontrado"));

        usuario.setMfaSecret(mfaSecretEncryptor.encrypt(request.secret()));
        usuario.setMfaEnabled(true);
        usuario.setMfaSetupRequired(false);
        List<String> backupCodes = mfaService.generateBackupCodes();
        usuario.setMfaBackupCodes(mfaSecretEncryptor.encrypt(mfaService.encodeBackupCodes(backupCodes)));
        usuarioRepository.save(usuario);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("verify")
    @Transactional(rollbackFor = Exception.class)
    @Operation(
            summary = "Verificar código MFA",
            description = "Verifica el código TOTP (o código de respaldo). Si es válido, emite el access token de acceso completo y fija las cookies de sesión.")
    @ApiResponse(responseCode = "200", description = "Código válido; se devuelve el access token y se fijan las cookies")
    @ApiResponse(responseCode = "401", description = "Código MFA inválido o MFA no configurado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<MfaVerificationResponse> verifyMfa(@RequestBody MfaVerificationRequest request,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws RecordNotFoundException, InvalidMfaCodeException {
        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();

        Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(usuarioPrincipal.usuarioNombre())
                .orElseThrow(() -> new RecordNotFoundException("Usuario no encontrado"));

        if (usuario.getMfaSecret() == null || !usuario.getMfaEnabled()) {
            throw new InvalidMfaCodeException("MFA no configurado para este usuario");
        }

        boolean valid = mfaService.validateTotpCode(mfaSecretEncryptor.decrypt(usuario.getMfaSecret()),
                Integer.parseInt(request.code()));

        if (!valid && usuario.getMfaBackupCodes() != null) {
            valid = mfaService.verifyBackupCode(mfaSecretEncryptor.decrypt(usuario.getMfaBackupCodes()),
                    request.code());
        }

        if (!valid) {
            throw new InvalidMfaCodeException("Código MFA inválido");
        }

        usuario.setFechaUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        String token = jwtService.tokenForRoleAssume(usuario);
        Cookie jwtCookie = jwtService.toShortCookie(token);
        httpResponse.addCookie(jwtCookie);

        String refreshToken = refreshTokenService
                .createRefreshToken(usuario.getId(),
                        ClientInfoUtil.extractUserAgent(httpRequest),
                        ClientInfoUtil.extractIp(httpRequest))
                .getToken();
        Cookie refreshCookie = jwtService.toRefreshCookie(refreshToken);
        httpResponse.addCookie(refreshCookie);

        return new ResponseEntity<>(
                new MfaVerificationResponse(token, jwtConfig.getTipo(), false, false),
                HttpStatus.OK);
    }
}