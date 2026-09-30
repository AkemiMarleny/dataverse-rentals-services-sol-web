package com.dataverse.dataverserentals.gestionoperativa.controllers;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.DetallePropuestaLiquidacionPageDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.PropuestaLiquidacionDetailsDto;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.services.PropuestaLiquidacionService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/propuestas")
@RestController
@Tag(name = "Propuestas de liquidación", description = "Cálculo, consulta y aprobación de propuestas de liquidación")
public class PropuestaLiquidacionController {

    private final SecurityContext securityContext;
    private final PropuestaLiquidacionService propuestaLiquidacionService;

    @GetMapping
    @Operation(
            summary = "Listar propuestas de liquidación",
            description = "Devuelve las propuestas de liquidación paginadas, con filtros opcionales por edificio y período.")
    @ApiResponse(responseCode = "200", description = "Página de propuestas de liquidación")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    public ResponseEntity<Page<PropuestaLiquidacionDetailsDto>> findAll(
            Pageable pageable,
            @Parameter(description = "Filtro por edificio", example = "1")
            @RequestParam Optional<Long> edificioId,
            @Parameter(description = "Inicio del período (yyyy-MM-dd)", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> fechaDesde,
            @Parameter(description = "Fin del período (yyyy-MM-dd)", example = "2026-01-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> fechaHasta)
            throws AccessDeniedException {
        log.info(
                "Listando propuestas de liquidación con filtros - edificioId: {}, fechaDesde: {}, fechaHasta: {}, pageable: {}",
                edificioId, fechaDesde, fechaHasta, pageable);

        var propuestasPage = propuestaLiquidacionService.findAllPropuestasLiquidacion(
                pageable, edificioId, fechaDesde, fechaHasta)
                .map(PropuestaLiquidacionDetailsDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(propuestasPage));
    }

    @GetMapping("{propuestaLiquidacionId}")
    @Operation(
            summary = "Obtener propuesta de liquidación por ID",
            description = "Devuelve el detalle de una propuesta de liquidación, incluyendo el monto total y el período.")
    @ApiResponse(responseCode = "200", description = "Detalle de la propuesta de liquidación")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Propuesta de liquidación no encontrada")
    public ResponseEntity<PropuestaLiquidacionDetailsDto> getById(@PathVariable Long propuestaLiquidacionId)
            throws RecordNotFoundException, AccessDeniedException {
        log.info("Obteniendo detalle de propuesta de liquidación con id: {}", propuestaLiquidacionId);

        PropuestaLiquidacion propuesta = propuestaLiquidacionService
                .findPropuestaLiquidacionDetails(propuestaLiquidacionId);

        return ResponseEntity.ok(PropuestaLiquidacionDetailsDto.fromEntity(propuesta));
    }

    @GetMapping("{propuestaId}/detalles")
    @Operation(
            summary = "Listar detalles de una propuesta de liquidación",
            description = "Devuelve los conceptos (detalles) de una propuesta de liquidación, paginados.")
    @ApiResponse(responseCode = "200", description = "Página de detalles de la propuesta")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    public ResponseEntity<Page<DetallePropuestaLiquidacionPageDto>> findDetalles(
            @PathVariable Long propuestaId,
            Pageable pageable) throws AccessDeniedException {
        log.info("Listando detalles de propuesta de liquidación con propuestaId: {}", propuestaId);

        var detallesPage = propuestaLiquidacionService.findDetallesByPropuestaId(propuestaId, pageable)
                .map(DetallePropuestaLiquidacionPageDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(detallesPage));
    }

    @PostMapping
    @Operation(
            summary = "Generar propuestas de liquidación",
            description = "Ejecuta el cálculo de las propuestas de liquidación pendientes.")
    @ApiResponse(responseCode = "204", description = "Propuestas generadas correctamente")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    ResponseEntity<Void> generarPropuestas() throws AccessDeniedException {
        log.info("Iniciando calculo de propuestas de liquidación");
        propuestaLiquidacionService.calcularPropuestas();

        return ResponseEntity.noContent().build();
    }

    @PostMapping("{propuestaLiquidacionId}/aprobados")
    @Operation(
            summary = "Aprobar propuesta de liquidación",
            description = "Aprueba una propuesta de liquidación pendiente, registrando al usuario aprobador.")
    @ApiResponse(responseCode = "204", description = "Propuesta aprobada correctamente")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Propuesta de liquidación no encontrada")
    @ApiResponse(responseCode = "422", description = "La propuesta no se puede aprobar (p. ej. ya está aprobada)")
    public ResponseEntity<Void> aprobar(
            @PathVariable Long propuestaLiquidacionId)
            throws RecordNotFoundException, UnprocesableEntity, AccessDeniedException {
        log.info("Aprobando propuesta de liquidacion con id: {}", propuestaLiquidacionId);

        propuestaLiquidacionService.aprobarPropuestaLiquidacion(
                propuestaLiquidacionId, securityContext.getUsuarioPrincipal());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{propuestaId}/recalcular")
    @Operation(
            summary = "Recalcular propuesta de liquidación",
            description = "Recalcula los montos de una propuesta de liquidación existente.")
    @ApiResponse(responseCode = "204", description = "Propuesta recalculada correctamente")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Propuesta de liquidación no encontrada")
    @ApiResponse(responseCode = "422", description = "La propuesta no se puede recalcular (p. ej. ya está aprobada)")
    public ResponseEntity<Void> recalcular(@PathVariable Long propuestaId)
            throws RecordNotFoundException, UnprocesableEntity, AccessDeniedException {
        log.info("Recalculando propuesta de liquidacion con id: {}", propuestaId);

        propuestaLiquidacionService.recalcularPropuesta(propuestaId);

        return ResponseEntity.noContent().build();
    }
}