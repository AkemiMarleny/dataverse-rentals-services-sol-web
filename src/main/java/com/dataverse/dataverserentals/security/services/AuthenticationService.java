package com.dataverse.dataverserentals.security.services;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UsuarioRepository;
import com.dataverse.dataverserentals.security.config.JwtConfig;
import com.dataverse.dataverserentals.security.context.SecurityContextImpl;
import com.dataverse.dataverserentals.security.controllers.dtos.request.LoginRequest;
import com.dataverse.dataverserentals.security.controllers.dtos.response.CurrentUserResponse;
import com.dataverse.dataverserentals.security.controllers.dtos.response.LoginResponse;
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
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse authenticate(@Valid LoginRequest request)
            throws CredencialesInvalidasException, UsuarioInactivoException, UsuarioBloqueadoException {
        Usuario usuario = usuarioRepository.findByNombreUsuario(request.nombreUsuario())
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

        usuario.setFechaUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        String token = jwtService.tokenFrom(usuario);

        return new LoginResponse(token, jwtConfig.getTipo());
    }

    public CurrentUserResponse getCurrentUser() throws RecordNotFoundException {
        String nombreUsuario = securityContext.getUsuarioPrincipal().usuarioNombre();

        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RecordNotFoundException("Usuario no encontrado"));

        return new CurrentUserResponse(
                usuario.getId(),
                usuario.getPersonaId(),
                usuario.getNombreUsuario(),
                usuario.getRolCode());
    }
}