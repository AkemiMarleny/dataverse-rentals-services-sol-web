package com.dataverse.dataverserentals.gestioncampo.controllers;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.CreateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.UpdateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.response.DetailLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.response.LecturaHistoricaPaginaDto;
import com.dataverse.dataverserentals.gestioncampo.entities.LecturaHistorica;
import com.dataverse.dataverserentals.gestioncampo.services.LecturaHistoricaService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/lecturas-historicas")
@Slf4j
@Tag(name = "Lecturas históricas", description = "Gestión de lecturas históricas de medidores")
public class LecturaHistoricaController {

    private final LecturaHistoricaService lecturaHistoricaService;
    private final SecurityContext securityContext;

    @GetMapping
    @Operation(
            summary = "Listar lecturas históricas",
            description = "Devuelve las lecturas históricas paginadas, con filtros opcionales por número de serie del medidor y rango de fechas.")
    @ApiResponse(responseCode = "200", description = "Página de lecturas históricas")
    public ResponseEntity<Page<LecturaHistoricaPaginaDto>> findAll(
            @Parameter(description = "Texto de búsqueda (número de serie del medidor)", example = "ELEC-2026-001")
            @RequestParam(required = false) String q,
            Pageable pageable,
            @Parameter(description = "Fecha de lectura desde (yyyy-MM-dd)", example = "2026-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @Parameter(description = "Fecha de lectura hasta (yyyy-MM-dd)", example = "2026-01-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        log.info(
                "Listando lecturas históricas con filtros: medidorNumeroSerie={}, fechaDesde={}, fechaHasta={}, parámetros: {}",
                q, fechaDesde, fechaHasta, pageable);

        org.springframework.data.domain.Page<LecturaHistoricaPaginaDto> lecturaHistoricaPage = lecturaHistoricaService
                .findAll(q, pageable, fechaDesde, fechaHasta)
                .map(LecturaHistoricaPaginaDto::fromEntity);
        return ResponseEntity.ok(PageFactory.from(lecturaHistoricaPage));
    }

    @PostMapping
    @Operation(
            summary = "Registrar lectura histórica",
            description = "Registra una nueva lectura de un medidor en una fecha determinada.")
    @ApiResponse(responseCode = "201", description = "Lectura registrada; se devuelve el ID de la lectura")
    @ApiResponse(responseCode = "400", description = "Datos de la lectura inválidos (p. ej. valor o fecha incorrectos)")
    public ResponseEntity<CreateResponse> create(
            @RequestBody CreateLecturaHistoricaDto request)
            throws FieldConstraintException {

        log.info("Iniciando la creación de una nueva lectura histórica: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        Long lecturaHistoricaId = lecturaHistoricaService.create(request, usuarioId);
        return new ResponseEntity<>(new CreateResponse(lecturaHistoricaId), HttpStatus.CREATED);
    }

    @GetMapping("{lecturaHistoricaId}")
    @Operation(
            summary = "Obtener lectura histórica por ID",
            description = "Devuelve el detalle de una lectura histórica, incluyendo datos del medidor, unidad y edificio.")
    @ApiResponse(responseCode = "200", description = "Detalle de la lectura histórica")
    @ApiResponse(responseCode = "404", description = "Lectura histórica no encontrada")
    public ResponseEntity<DetailLecturaHistoricaDto> findById(
            @PathVariable Long lecturaHistoricaId) throws RecordNotFoundException {

        log.info("Consultando detalles de la lectura histórica con ID: {}", lecturaHistoricaId);

        LecturaHistorica lecturaHistorica = lecturaHistoricaService.findById(lecturaHistoricaId);
        DetailLecturaHistoricaDto detailLecturaHistoricaDto = DetailLecturaHistoricaDto.fromEntity(lecturaHistorica);
        return new ResponseEntity<>(detailLecturaHistoricaDto, HttpStatus.OK);
    }

    @PutMapping("{lecturaHistoricaId}")
    @Operation(
            summary = "Actualizar lectura histórica",
            description = "Actualiza la fecha y el valor de una lectura histórica existente.")
    @ApiResponse(responseCode = "204", description = "Lectura actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Lectura histórica no encontrada")
    public ResponseEntity<Void> update(@PathVariable Long lecturaHistoricaId,
            @RequestBody UpdateLecturaHistoricaDto request) throws RecordNotFoundException {

        log.info("Iniciando actualización de la lectura histórica: {}", request);
        Long usuarioId = securityContext.getUsuarioPrincipal().usuarioId();
        lecturaHistoricaService.update(lecturaHistoricaId, request, usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}