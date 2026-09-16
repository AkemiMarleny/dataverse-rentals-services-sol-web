package com.dataverse.dataverserentals.security.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.dataverse.dataverserentals.security.services.JwtService;
import com.dataverse.dataverserentals.security.services.MfaService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/auth/mfa")
@RequiredArgsConstructor
@Slf4j
public class MfaController {

    private static final String MFA_ISSUER = "Dataverse Rentals";

    private final MfaService mfaService;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final SecurityContextImpl securityContext;
    private final MfaSecretEncryptor mfaSecretEncryptor;

    @GetMapping("setup")
    public ResponseEntity<MfaSetupResponse> setupMfa() throws RecordNotFoundException, MfaAlreadyEnabledException {
        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();

        Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(usuarioPrincipal.usuarioNombre())
                .orElseThrow(() -> new RecordNotFoundException("Usuario no encontrado"));

        if (usuario.getMfaEnabled()) {
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
    public ResponseEntity<MfaVerificationResponse> enableMfa(@RequestBody @Valid MfaSetupRequest request)
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

        String token = jwtService.tokenForRoleAssume(usuario);
        return new ResponseEntity<>(
                new MfaVerificationResponse(token, jwtConfig.getTipo(), false, false),
                HttpStatus.OK);
    }

    @PostMapping("verify")
    public ResponseEntity<MfaVerificationResponse> verifyMfa(@RequestBody MfaVerificationRequest request,
            HttpServletResponse httpResponse)
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

        String token = jwtService.tokenForRoleAssume(usuario);
        Cookie jwtCookie = jwtService.toCookie(token);
        httpResponse.addCookie(jwtCookie);

        return new ResponseEntity<>(
                new MfaVerificationResponse(token, jwtConfig.getTipo(), false, false),
                HttpStatus.OK);
    }
}
