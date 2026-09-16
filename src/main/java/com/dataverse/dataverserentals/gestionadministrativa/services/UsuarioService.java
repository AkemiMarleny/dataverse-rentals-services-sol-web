package com.dataverse.dataverserentals.gestionadministrativa.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateUsuarioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateUsuarioDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UsuarioRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.specifications.UsuarioSpecifications;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaService personaService;
    private final PasswordEncoder passwordEncoder;

    public Page<Usuario> findAll(Pageable pageable, String q) {
        log.info("Búsqueda paginada de usuarios [filtro: '{}', pageable: {}]", q, pageable);
        Specification<Usuario> spec = UsuarioSpecifications.bySearchTerm(q);
        return usuarioRepository.findAll(spec, pageable);
    }

    public Usuario findById(Long usuarioId, UsuarioPrincipal usuarioPrincipal)
            throws RecordNotFoundException, AccessDeniedException {
        if (!RolUsuario.ADMINISTRADOR.equals(usuarioPrincipal.usuarioRol())) {
            throw new AccessDeniedException(usuarioPrincipal);
        }

        log.info("Recuperando detalles del usuario con ID: {}", usuarioId);
        return usuarioRepository.findById(usuarioId).orElseThrow(() -> new RecordNotFoundException(
                String.format("El usuario con el ID %s no existe en nuestros registros.", usuarioId)));
    }

    public Long create(@Valid CreateUsuarioDto payload, UsuarioPrincipal usuarioPrincipal)
            throws RecordNotFoundException, ConflictException, UnprocesableEntity {
        log.info("Registrando nuevo usuario con los siguientes datos: nombreUsuario={}, personaId={}, rolCode={}",
                payload.nombreUsuario(), payload.personaId(), payload.rolCode());

        Persona persona = personaService.findById(payload.personaId());

        if (usuarioRepository.existsByNombreUsuarioIgnoreCase(payload.nombreUsuario())) {
            throw new ConflictException(
                    String.format("El nombre de usuario '%s' ya se encuentra registrado en el sistema.",
                            payload.nombreUsuario()));
        }

        if (!payload.contrasena().equals(payload.confirmarContrasena())) {
            throw new UnprocesableEntity("Las contraseñas no coinciden");
        }

        Usuario usuario = Usuario.builder()
                .personaId(persona.getId())
                .nombreUsuario(payload.nombreUsuario())
                .contrasenaHash(passwordEncoder.encode(payload.contrasena()))
                .rolCode(payload.rolCode())
                .estadoCode(payload.estadoCode())
                .mfaEnabled(Boolean.TRUE)
                .fechaUltimoLogin(null)
                .fechaCreacion(LocalDateTime.now())
                .usuarioCreacionId(usuarioPrincipal.usuarioId())
                .build();

        return usuarioRepository.save(usuario).getId();
    }

    public void actualizarUsuario(Long usuarioId, @Valid UpdateUsuarioDto payload, UsuarioPrincipal usuarioPrincipal)
            throws RecordNotFoundException, AccessDeniedException, ConflictException {
        log.info("Iniciando actualización del usuario con ID: {}. Datos recibidos: {}", usuarioId, payload);
        Usuario usuario = findById(usuarioId, usuarioPrincipal);

        if (!usuario.getNombreUsuario().equalsIgnoreCase(payload.nombreUsuario())) {
            if (usuarioRepository.existsByNombreUsuarioIgnoreCaseAndIdNot(payload.nombreUsuario(), usuarioId)) {
                throw new ConflictException(
                        String.format("El nombre de usuario '%s' ya se encuentra registrado en el sistema.",
                                payload.nombreUsuario()));
            }
        }

        usuario.actualizar(payload, usuarioPrincipal.usuarioId());
        usuarioRepository.save(usuario);
    }
}