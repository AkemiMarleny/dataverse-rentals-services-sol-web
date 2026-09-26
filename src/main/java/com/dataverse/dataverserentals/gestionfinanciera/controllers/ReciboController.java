package com.dataverse.dataverserentals.gestionfinanciera.controllers;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.exceptions.RecordNotFoundException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response.DetalleReciboDto;
import com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response.ReciboDetailDto;
import com.dataverse.dataverserentals.gestionfinanciera.controllers.dtos.response.ReciboDto;
import com.dataverse.dataverserentals.gestionfinanciera.enums.EstadoRecibo;
import com.dataverse.dataverserentals.gestionfinanciera.services.DetalleReciboService;
import com.dataverse.dataverserentals.gestionfinanciera.services.ReciboService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recibos")
@Tag(name = "Recibos", description = "Consultas y generación de recibos de alquiler")
public class ReciboController {

    private final ReciboService reciboService;
    private final DetalleReciboService detalleReciboService;

    @GetMapping
    @Operation(
            summary = "Listar recibos",
            description = "Devuelve los recibos paginados, con filtros opcionales por fecha de emisión y estado.")
    @ApiResponse(responseCode = "200", description = "Página de recibos")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    public ResponseEntity<Page<ReciboDto>> findAll(
            Pageable pageable,
            @Parameter(description = "Fecha de emisión desde (yyyy-MM-dd)", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> fechaDesde,
            @Parameter(description = "Fecha de emisión hasta (yyyy-MM-dd)", example = "2026-01-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> fechaHasta,
            @Parameter(description = "Filtro por estado del recibo", example = "PENDIENTE")
            @RequestParam Optional<EstadoRecibo> estadoCode) throws AccessDeniedException {
        log.info(
                "Listando recibos con filtros - edificioId: {}, fechaDesde: {}, fechaHasta: {}, estadoCode: {}, pageable: {}",
                fechaDesde, fechaHasta, estadoCode, pageable);

        var recibosPage = reciboService.findAllRecibos(
                pageable, fechaDesde, fechaHasta, estadoCode)
                .map(ReciboDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(recibosPage));
    }

    @GetMapping("/{reciboId}")
    @Operation(
            summary = "Obtener recibo por ID",
            description = "Devuelve el detalle de un recibo, incluyendo el monto pagado.")
    @ApiResponse(responseCode = "200", description = "Detalle del recibo")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Recibo no encontrado")
    public ResponseEntity<ReciboDetailDto> findById(
            @PathVariable Long reciboId) throws AccessDeniedException, RecordNotFoundException {
        log.info("Obteniendo recibo con ID: {}", reciboId);

        var recibo = reciboService.findReciboById(reciboId);

        return ResponseEntity.ok(ReciboDetailDto.fromEntity(recibo));
    }

    @PostMapping
    @Operation(
            summary = "Generar recibos",
            description = "Genera los recibos pendientes de todos los contratos activos.")
    @ApiResponse(responseCode = "204", description = "Recibos generados correctamente")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    ResponseEntity<Void> generarRecibos() throws AccessDeniedException {
        reciboService.generarRecibos();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{reciboId}/detalles")
    @Operation(
            summary = "Listar detalles de un recibo",
            description = "Devuelve los conceptos (detalles) de un recibo, paginados.")
    @ApiResponse(responseCode = "200", description = "Página de detalles del recibo")
    @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    @ApiResponse(responseCode = "404", description = "Recibo no encontrado")
    public ResponseEntity<Page<DetalleReciboDto>> findDetallesByReciboId(
            @PathVariable Long reciboId,
            Pageable pageable) throws AccessDeniedException, RecordNotFoundException {
        log.info("Listando detalles del recibo con ID: {}", reciboId);

        var detallesPage = detalleReciboService.findDetallesByReciboId(reciboId, pageable)
                .map(DetalleReciboDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(detallesPage));
    }
}