package com.dataverse.dataverserentals.gestionoperativa.controllers;

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
import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateGastoOperativoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.UpdateGastoOperativoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.DetailGastoOperativoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.GastoOperativoPaginaDto;
import com.dataverse.dataverserentals.gestionoperativa.entities.GastoOperativo;
import com.dataverse.dataverserentals.gestionoperativa.services.GastoOperativoService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/edificios/{edificioId}/gastos-operativos")
@Slf4j
@Tag(name = "Gastos operativos", description = "Gestión de gastos operativos por edificio")
public class GastoOperativoController {

    private final GastoOperativoService gastoOperativoService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar gastos operativos de un edificio",
            description = "Devuelve los gastos operativos del edificio, paginados y con búsqueda opcional por categoría.")
    @ApiResponse(responseCode = "200", description = "Página de gastos operativos")
    public ResponseEntity<Page<GastoOperativoPaginaDto>> findAll(
            Pageable pageable,
            @PathVariable Long edificioId,
            @Parameter(description = "Texto de búsqueda (nombre de la categoría)", example = "mantenimiento")
            @RequestParam(required = false) String q) {

        log.info("Listando gastos operativos con edificioId: {}, categoriaNombre: {} y parámetros: {}",
                edificioId, q, pageable);

        org.springframework.data.domain.Page<GastoOperativoPaginaDto> gastoOperativoPage = gastoOperativoService
                .findAll(pageable, edificioId, q)
                .map(GastoOperativoPaginaDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(gastoOperativoPage));
    }

    @PostMapping
    @Operation(
            summary = "Crear gasto operativo",
            description = "Registra un nuevo gasto operativo para el edificio indicado.")
    @ApiResponse(responseCode = "201", description = "Gasto operativo creado; se devuelve el ID del gasto")
    public ResponseEntity<CreateResponse> create(
            @PathVariable Long edificioId,
            @RequestBody CreateGastoOperativoDto request) {

        log.info("Iniciando la creación de un nuevo gasto operativo en edificio {}: {}", edificioId, request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long id = gastoOperativoService.create(request, edificioId, usuarioId);
        return new ResponseEntity<>(new CreateResponse(id), HttpStatus.CREATED);
    }

    @GetMapping("{gastoOperativoId}")
    @Operation(
            summary = "Obtener gasto operativo por ID",
            description = "Devuelve el detalle de un gasto operativo del edificio.")
    @ApiResponse(responseCode = "200", description = "Detalle del gasto operativo")
    @ApiResponse(responseCode = "404", description = "Gasto operativo no encontrado")
    public ResponseEntity<DetailGastoOperativoDto> findById(
            @PathVariable Long edificioId,
            @PathVariable Long gastoOperativoId)
            throws RecordNotFoundException {

        log.info("Consultando detalles del gasto operativo con ID: {} en edificio {}", gastoOperativoId, edificioId);
        GastoOperativo gastoOperativo = gastoOperativoService.findById(edificioId, gastoOperativoId);
        DetailGastoOperativoDto detailGastoOperativoDto = DetailGastoOperativoDto.fromEntity(gastoOperativo);
        return new ResponseEntity<>(detailGastoOperativoDto, HttpStatus.OK);
    }

    @PutMapping("{gastoOperativoId}")
    @Operation(
            summary = "Actualizar gasto operativo",
            description = "Actualiza los datos de un gasto operativo existente.")
    @ApiResponse(responseCode = "204", description = "Gasto operativo actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida (p. ej. datos incompletos)")
    @ApiResponse(responseCode = "404", description = "Gasto operativo no encontrado")
    public ResponseEntity<Void> update(
            @PathVariable Long edificioId,
            @PathVariable Long gastoOperativoId,
            @RequestBody UpdateGastoOperativoDto request) throws RecordNotFoundException, FieldConstraintException {

        log.info("Iniciando la actualización del gasto operativo con ID: {} en edificio {}", gastoOperativoId,
                edificioId);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        gastoOperativoService.update(edificioId, gastoOperativoId, request, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}