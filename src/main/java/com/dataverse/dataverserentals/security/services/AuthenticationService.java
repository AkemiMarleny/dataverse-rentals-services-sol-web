package com.dataverse.dataverserentals.security.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UsuarioRepository;
import com.dataverse.dataverserentals.security.config.JwtConfig;
import com.dataverse.dataverserentals.security.context.SecurityContextImpl;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;
import com.dataverse.dataverserentals.security.controllers.dtos.request.LoginRequest;
import com.dataverse.dataverserentals.security.controllers.dtos.response.CurrentUserResponse;
import com.dataverse.dataverserentals.security.controllers.dtos.response.LoginResponse;
import com.dataverse.dataverserentals.security.entities.RefreshToken;
import com.dataverse.dataverserentals.security.enums.TokenScope;
import com.dataverse.dataverserentals.security.exceptions.CredencialesInvalidasException;
import com.dataverse.dataverserentals.security.exceptions.UsuarioBloqueadoException;
import com.dataverse.dataverserentals.security.exceptions.UsuarioInactivoException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final SecurityContextImpl securityContext;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse authenticate(@Valid LoginRequest request)
            throws CredencialesInvalidasException, UsuarioInactivoException, UsuarioBloqueadoException {
        Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(request.nombreUsuario())
                .orElseThrow(() -> new CredencialesInvalidasException("Credenciales inválidas"));

        if (usuario.getEstadoCode() == EstadoUsuario.INACTIVO) {
            throw new UsuarioInactivoException("El usuario se encuentra inactivo");
        }

        if (usuario.getEstadoCode() == EstadoUsuario.BLOQUEADO) {
            throw new UsuarioBloqueadoException("El usuario se encuentra bloqueado");
        }

        if (!passwordEncoder.matches(request.contrasena(), usuario.getContrasenaHash())) {
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        if (usuario.getMfaSetupRequired() != null && usuario.getMfaSetupRequired()) {
            String tempToken = jwtService.tokenForSetupMFA(usuario);
            return new LoginResponse(tempToken, jwtConfig.getTipo(), "MFA_SETUP_REQUIRED");
        }

        String tempToken = jwtService.tokenForVerify(usuario);
        return new LoginResponse(tempToken, jwtConfig.getTipo(), "MFA_REQUIRED");
    }

    public String createRefreshToken(String nombreUsuario) throws RecordNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(nombreUsuario)
                .orElseThrow(() -> new RecordNotFoundException("Usuario no encontrado"));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario.getId());
        return refreshToken.getToken();
    }

    public Usuario getUserById(Long usuarioId) throws RecordNotFoundException {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecordNotFoundException("Usuario no encontrado"));
    }

    public CurrentUserResponse getCurrentUser() throws RecordNotFoundException {
        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();
        String nombreUsuario = usuarioPrincipal.usuarioNombre();
        TokenScope tokenScope = usuarioPrincipal.scope();

        Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(nombreUsuario)
                .orElseThrow(() -> new RecordNotFoundException("Usuario no encontrado"));

        var mfaVerified = TokenScope.FULL_ACCESS.equals(tokenScope);
        var mfaConfigured = usuario.getMfaSecret() != null;

        return new CurrentUserResponse(
                usuario.getId(),
                usuario.getPersonaId(),
                usuario.getNombreUsuario(),
                usuario.getRolCode(),
                usuario.getMfaEnabled(),
                usuario.getMfaSetupRequired(),
                mfaConfigured,
                mfaVerified);
    }
}