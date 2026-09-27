package com.dataverse.dataverserentals.gestionadministrativa.controllers;

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
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateBuildingAddress;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateEdificioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateEdificioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailEdificioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.EdificioPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.services.EdificioService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/edificios")
@Slf4j
@Tag(name = "Edificios", description = "Gestión de edificios y sus direcciones")
public class EdificioController {

    private final EdificioService edificioService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar edificios",
            description = "Devuelve los edificios paginados, con búsqueda opcional por nombre.")
    @ApiResponse(responseCode = "200", description = "Página de edificios")
    public ResponseEntity<Page<EdificioPaginaDto>> findAll(Pageable pageable,
            @Parameter(description = "Texto de búsqueda (nombre del edificio)", example = "Torre")
            @RequestParam(required = false) String q) {

        log.info("Listando edificios con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<EdificioPaginaDto> edificioPage = edificioService.findAll(pageable, q)
                .map(EdificioPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(edificioPage));
    }

    @PostMapping
    @Operation(
            summary = "Crear edificio",
            description = "Registra un nuevo edificio con su nombre y número de pisos.")
    @ApiResponse(responseCode = "201", description = "Edificio creado; se devuelve el ID del edificio")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. nombre de edificio ya existente)")
    public ResponseEntity<CreateResponse> create(@RequestBody CreateEdificioDto request) throws ConflictException {

        log.info("Iniciando la creación de una nuevo edificio: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = edificioService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{edificioId}")
    @Operation(
            summary = "Obtener edificio por ID",
            description = "Devuelve el detalle de un edificio.")
    @ApiResponse(responseCode = "200", description = "Detalle del edificio")
    @ApiResponse(responseCode = "404", description = "Edificio no encontrado")
    public ResponseEntity<DetailEdificioDto> findById(@PathVariable Long edificioId) throws RecordNotFoundException {

        log.info("Consultando detalles del edificio con ID: {}", edificioId);
        Edificio edificio = edificioService.findById(edificioId);
        DetailEdificioDto detailEdificioDto = DetailEdificioDto.fromEntity(edificio);
        return new ResponseEntity<>(detailEdificioDto, HttpStatus.OK);
    }

    @PutMapping("{edificioId}")
    @Operation(
            summary = "Actualizar edificio",
            description = "Actualiza el nombre y número de pisos de un edificio existente.")
    @ApiResponse(responseCode = "204", description = "Edificio actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Edificio no encontrado")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. nombre de edificio ya existente)")
    public ResponseEntity<Void> update(@PathVariable Long edificioId, @RequestBody UpdateEdificioDto request)
            throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización del edificio: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        edificioService.update(edificioId, request, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("{edificioId}/direcciones")
    @Operation(
            summary = "Agregar dirección a un edificio",
            description = "Registra una dirección con topónimo para el edificio indicado.")
    @ApiResponse(responseCode = "201", description = "Dirección creada; se devuelve el ID de la dirección")
    @ApiResponse(responseCode = "404", description = "Edificio o topónimo no encontrado")
    public ResponseEntity<CreateResponse> createBuildingAddress(@PathVariable Long edificioId,
            @RequestBody CreateBuildingAddress request) throws RecordNotFoundException {

        log.info("Iniciando la creación de una dirección para el edificio ID: {}. Request: {}", edificioId, request);
        Long edificioDireccionId = edificioService.createBuildingAddress(edificioId, request);
        return new ResponseEntity<>(new CreateResponse(edificioDireccionId), HttpStatus.CREATED);
    }

}