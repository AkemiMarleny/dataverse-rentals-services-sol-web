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
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoCivil;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.RolUsuario;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.PersonaRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UsuarioRepository;
import com.dataverse.dataverserentals.gestionadministrativa.services.PersonaService;
import com.dataverse.dataverserentals.gestionadministrativa.services.UsuarioService;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

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
        Long personaId = createDefaultPersona();
        createDefaultUsuario(personaId);
    }

    private Long createDefaultUsuario(Long personaId)
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

            UsuarioPrincipal usuarioPrincipal = new UsuarioPrincipal(
                    1L,
                    "Root",
                    RolUsuario.SYSTEM);

            return usuarioService.create(
                    createUsuario,
                    usuarioPrincipal);
        }

        return usuarioDefault.get().getId();
    }

    private Long createDefaultPersona() throws FieldConstraintException, ConflictException {
        Optional<Persona> personaDefault = personaRepository.findByTipoDocumentoCodeAndNumeroDocumento(
                TipoDocumento.DNI, "89876567");
        if (personaDefault.isEmpty()) {
            return personaService.create(new CreatePersonDto(
                    "Dataverse",
                    "Sys",
                    TipoDocumento.DNI,
                    "89876567",
                    Genero.FEMENINO,
                    EstadoCivil.SOLTERA,
                    LocalDate.of(1990, 10, 10),
                    "(+51) 987654212",
                    "info@dataverse.com"));
        }

        return personaDefault.get().getId();
    }

}
