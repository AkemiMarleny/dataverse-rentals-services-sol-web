package com.dataverse.dataverserentals.gestionadministrativa.services;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateUsuarioDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.repositories.UsuarioRepository;

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
    private final SecurityContext securityContext;

    public Long create(@Valid CreateUsuarioDto payload)
            throws RecordNotFoundException, ConflictException, UnprocesableEntity {
        log.info("Registrando nuevo usuario con los siguientes datos: nombreUsuario={}, personaId={}, rolCode={}",
                payload.nombreUsuario(), payload.personaId(), payload.rolCode());

        Persona persona = personaService.findById(payload.personaId());

        if (usuarioRepository.existsByNombreUsuario(payload.nombreUsuario())) {
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
                .fechaUltimoLogin(null)
                .fechaCreacion(LocalDateTime.now())
                .usuarioCreacionId(securityContext.getUsuarioPrincipal().usuarioId())
                .build();

        return usuarioRepository.save(usuario).getId();
    }
}