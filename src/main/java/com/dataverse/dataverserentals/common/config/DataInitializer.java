package com.dataverse.dataverserentals.common.config;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateUsuarioDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.PersonaRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UsuarioRepository;
import com.dataverse.dataverserentals.gestionadministrativa.services.PersonaService;
import com.dataverse.dataverserentals.gestionadministrativa.services.UsuarioService;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;
import com.dataverse.dataverserentals.security.enums.TokenScope;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final PersonaService personaService;
    private final UsuarioService usuarioService;

    @Override
    public void run(String... args) throws Exception {
        UsuarioPrincipal usuarioPrincipal = new UsuarioPrincipal(
                1L,
                "Root",
                RolUsuario.SYSTEM,
                1L,
                TokenScope.FULL_ACCESS);
        Long personaId = createDefaultPersona(usuarioPrincipal);
        createDefaultUsuario(personaId, usuarioPrincipal);

        Long adminId = createDefaultAdminPersona(usuarioPrincipal);
        createDefaultAdminUsuario(adminId, usuarioPrincipal);
    }

    private Long createDefaultUsuario(Long personaId, UsuarioPrincipal usuarioPrincipal)
            throws RecordNotFoundException, ConflictException, UnprocesableEntity {
        Optional<Usuario> usuarioDefault = usuarioRepository.findByNombreUsuario("dataversesys");
        if (usuarioDefault.isEmpty()) {
            CreateUsuarioDto createUsuario = new CreateUsuarioDto(
                    personaId,
                    "dataversesys",
                    "dataversesys",
                    "dataversesys",
                    RolUsuario.SYSTEM,
                    EstadoUsuario.ACTIVO);

            return usuarioService.create(
                    createUsuario,
                    usuarioPrincipal);
        }

        return usuarioDefault.get().getId();
    }

    private Long createDefaultPersona(UsuarioPrincipal usuarioPrincipal)
            throws FieldConstraintException, ConflictException {
        Optional<Persona> personaDefault = personaRepository.findByTipoDocumentoCodeAndNumeroDocumento(
                TipoDocumento.DNI, "89876567");
        if (personaDefault.isEmpty()) {
            return personaService.create(
                    new CreatePersonDto(
                            TipoPersona.NATURAL_SIN_NEGOCIO,
                            TipoDocumento.DNI,
                            "89876567",
                            "(+51) 987654212",
                            "info@dataverse.com",
                            "Dataverse",
                            "Sys",
                            Genero.FEMENINO,
                            LocalDate.of(1990, 10, 10),
                            null,
                            null),
                    usuarioPrincipal.usuarioId());
        }

        return personaDefault.get().getId();
    }

    private Long createDefaultAdminPersona(UsuarioPrincipal usuarioPrincipal) {
        Optional<Persona> personaDefault = personaRepository.findByTipoDocumentoCodeAndNumeroDocumento(
                TipoDocumento.DNI, "12345678");
        if (personaDefault.isEmpty()) {
            return personaService.create(
                    new CreatePersonDto(
                            TipoPersona.NATURAL_SIN_NEGOCIO,
                            TipoDocumento.DNI,
                            "12345678",
                            "(+51) 997654212",
                            "admin@dataverse.com",
                            "Dataverse",
                            "Admin",
                            Genero.FEMENINO,
                            LocalDate.of(1990, 10, 10),
                            null,
                            null),
                    usuarioPrincipal.usuarioId());
        }

        return personaDefault.get().getId();

    }

    private Long createDefaultAdminUsuario(Long personaId, UsuarioPrincipal usuarioPrincipal)
            throws RecordNotFoundException, ConflictException, UnprocesableEntity {
        Optional<Usuario> usuarioDefault = usuarioRepository.findByNombreUsuario("dataverseadmin");
        if (usuarioDefault.isEmpty()) {
            CreateUsuarioDto createUsuario = new CreateUsuarioDto(
                    personaId,
                    "dataverseadmin",
                    "dataverseadmin",
                    "dataverseadmin",
                    RolUsuario.ADMINISTRADOR,
                    EstadoUsuario.ACTIVO);

            return usuarioService.create(
                    createUsuario,
                    usuarioPrincipal);
        }

        return usuarioDefault.get().getId();
    }
}
