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
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateUnidadDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateUnidadDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailUnidadDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.UnidadPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;
import com.dataverse.dataverserentals.gestionadministrativa.services.UnidadService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/edificios/{edificioId}/unidades")
@Slf4j
@Tag(name = "Unidades", description = "Gestión de unidades dentro de un edificio")
public class UnidadController {

    private final UnidadService unidadService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar unidades de un edificio",
            description = "Devuelve las unidades del edificio, paginadas y con búsqueda obligatoria.")
    @ApiResponse(responseCode = "200", description = "Página de unidades")
    public ResponseEntity<Page<UnidadPaginaDto>> findAll(
            @PathVariable Long edificioId,
            Pageable pageable,
            @Parameter(description = "Texto de búsqueda (piso o puerta)", example = "5")
            @RequestParam String q) {

        log.info("Listando unidades con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<UnidadPaginaDto> unidadPage = unidadService
                .findAll(pageable, edificioId, q)
                .map(UnidadPaginaDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(unidadPage));
    }

    @PostMapping
    @Operation(
            summary = "Crear unidad",
            description = "Registra una nueva unidad en el edificio indicado.")
    @ApiResponse(responseCode = "201", description = "Unidad creada; se devuelve el ID de la unidad")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. piso y puerta ya existentes)")
    public ResponseEntity<CreateResponse> create(@PathVariable Long edificioId, @RequestBody CreateUnidadDto request)
            throws ConflictException {

        log.info("Iniciando la creación de una nueva unidad: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = unidadService.create(edificioId, request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{unidadId}")
    @Operation(
            summary = "Obtener unidad por ID",
            description = "Devuelve el detalle de una unidad del edificio.")
    @ApiResponse(responseCode = "200", description = "Detalle de la unidad")
    @ApiResponse(responseCode = "404", description = "Unidad no encontrada")
    public ResponseEntity<DetailUnidadDto> findById(@PathVariable Long edificioId, @PathVariable Long unidadId)
            throws RecordNotFoundException {

        log.info("Consultando detalles de la unidad con ID: {}", unidadId);
        Unidad unidad = unidadService.findById(edificioId, unidadId);
        DetailUnidadDto detailUnidadDto = DetailUnidadDto.fromEntity(unidad);
        return new ResponseEntity<>(detailUnidadDto, HttpStatus.OK);
    }

    @PutMapping("{unidadId}")
    @Operation(
            summary = "Actualizar unidad",
            description = "Actualiza los datos de una unidad existente.")
    @ApiResponse(responseCode = "204", description = "Unidad actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Unidad no encontrada")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. piso y puerta ya existentes)")
    public ResponseEntity<Void> update(@PathVariable Long edificioId, @PathVariable Long unidadId,
            @RequestBody UpdateUnidadDto request)
            throws ConflictException, RecordNotFoundException {

        log.info("Iniciando actualización de la unidad: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        unidadService.update(edificioId, unidadId, request, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}