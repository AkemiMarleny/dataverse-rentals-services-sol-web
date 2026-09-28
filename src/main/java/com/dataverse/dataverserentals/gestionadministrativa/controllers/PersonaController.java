package com.dataverse.dataverserentals.gestionadministrativa.controllers;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.CreateResponse;
import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.ConflictException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailPersonaDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.PersonaPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;
import com.dataverse.dataverserentals.gestionadministrativa.services.PersonaService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/personas")
@Slf4j
@Tag(name = "Personas", description = "Gestión de personas naturales y jurídicas")
public class PersonaController {

    private final PersonaService personaService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar personas",
            description = "Devuelve las personas paginadas, con búsqueda opcional y filtro por tipo de persona.")
    @ApiResponse(responseCode = "200", description = "Página de personas")
    public ResponseEntity<Page<PersonaPaginaDto>> findAll(
            Pageable pageable,
            @Parameter(description = "Texto de búsqueda (nombres, documento, razón social)", example = "Juan")
            @RequestParam(required = false) String q,
            @Parameter(description = "Filtro por tipo de persona", example = "NATURAL_SIN_NEGOCIO")
            @RequestParam Optional<TipoPersona> tipoPersonaCode) {

        log.info("Listando personas con filtro '{}', tipo: {} y parámetros: {}", q, tipoPersonaCode, pageable);
        org.springframework.data.domain.Page<PersonaPaginaDto> personaPage = personaService
                .findAll(pageable, q, tipoPersonaCode)
                .map(PersonaPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(personaPage));
    }

    @PostMapping
    @Operation(
            summary = "Crear persona",
            description = "Registra una nueva persona (natural o jurídica) con sus datos de contacto y documento.")
    @ApiResponse(responseCode = "201", description = "Persona creada; se devuelve el ID de la persona")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. documento ya registrado)")
    @ApiResponse(responseCode = "422", description = "Datos de la persona no procesables")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<CreateResponse> create(@RequestBody CreatePersonDto request)
            throws Exception {

        log.info("Iniciando la creación de una nueva persona: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = personaService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{personaId}")
    @Operation(
            summary = "Obtener persona por ID",
            description = "Devuelve el detalle de una persona.")
    @ApiResponse(responseCode = "200", description = "Detalle de la persona")
    @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    public ResponseEntity<DetailPersonaDto> findById(@PathVariable Long personaId) throws RecordNotFoundException {

        log.info("Consultando detalles de la persona con ID: {}", personaId);
        Persona persona = personaService.findById(personaId);
        DetailPersonaDto detailPersonaDto = DetailPersonaDto.fromEntity(persona);
        return new ResponseEntity<>(detailPersonaDto, HttpStatus.OK);
    }

    @PutMapping("{personaId}")
    @Operation(
            summary = "Actualizar persona",
            description = "Actualiza los datos de una persona existente.")
    @ApiResponse(responseCode = "204", description = "Persona actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. documento ya registrado)")
    public ResponseEntity<Void> update(@PathVariable Long personaId, @RequestBody UpdatePersonDto request)
            throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización de persona: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        personaService.update(personaId, request, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}