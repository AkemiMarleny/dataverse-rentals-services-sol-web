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
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateMedidorDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateMedidorDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.DetailMedidorDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response.MedidorPaginaDto;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.services.MedidorService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/edificios/{edificioId}/unidades/{unidadId}/medidores")
@Slf4j
@Tag(name = "Medidores", description = "Gestión de medidores de luz y agua por unidad")
public class MedidorController {

    private final MedidorService medidorService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar medidores de una unidad",
            description = "Devuelve los medidores de la unidad, paginados y con búsqueda opcional.")
    @ApiResponse(responseCode = "200", description = "Página de medidores")
    public ResponseEntity<Page<MedidorPaginaDto>> findAll(Pageable pageable, @PathVariable Long unidadId,
            @Parameter(description = "Texto de búsqueda (número de serie)", example = "SER-2026")
            @RequestParam(required = false) String q) {

        log.info("Listando medidores con filtro '{}' y parámetros: {}", q, pageable);
        org.springframework.data.domain.Page<MedidorPaginaDto> medidorPage = medidorService
                .findAll(pageable, unidadId, q)
                .map(MedidorPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(medidorPage));
    }

    @PostMapping
    @Operation(
            summary = "Crear medidor",
            description = "Registra un nuevo medidor para la unidad indicada.")
    @ApiResponse(responseCode = "201", description = "Medidor creado; se devuelve el ID del medidor")
    @ApiResponse(responseCode = "404", description = "Edificio o unidad no encontrada")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. número de serie ya existente)")
    public ResponseEntity<CreateResponse> create(@PathVariable Long edificioId, @PathVariable Long unidadId,
            @RequestBody CreateMedidorDto request)
            throws ConflictException, RecordNotFoundException {

        log.info("Iniciando la creación de una nuevo medidor: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = medidorService.create(request, edificioId, unidadId, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{medidorId}")
    @Operation(
            summary = "Obtener medidor por ID",
            description = "Devuelve el detalle de un medidor de la unidad.")
    @ApiResponse(responseCode = "200", description = "Detalle del medidor")
    @ApiResponse(responseCode = "404", description = "Medidor no encontrado")
    public ResponseEntity<DetailMedidorDto> findById(
            @PathVariable Long edificioId,
            @PathVariable Long unidadId,
            @PathVariable Long medidorId) throws RecordNotFoundException {

        log.info("Consultando detalles del medidor con ID: {}", medidorId);
        Medidor medidor = medidorService.findById(edificioId, unidadId, medidorId);
        DetailMedidorDto detailMedidorDto = DetailMedidorDto.fromEntity(medidor);
        return new ResponseEntity<>(detailMedidorDto, HttpStatus.OK);
    }

    @PutMapping("{medidorId}")
    @Operation(
            summary = "Actualizar medidor",
            description = "Actualiza el tipo y número de serie de un medidor existente.")
    @ApiResponse(responseCode = "204", description = "Medidor actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Medidor no encontrado")
    @ApiResponse(responseCode = "409", description = "Conflicto (p. ej. número de serie ya existente)")
    public ResponseEntity<Void> update(
            @PathVariable Long edificioId,
            @PathVariable Long unidadId,
            @PathVariable Long medidorId,
            @RequestBody UpdateMedidorDto request) throws RecordNotFoundException, ConflictException {

        log.info("Iniciando actualización del medidor: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        medidorService.update(request, edificioId, unidadId, medidorId, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}