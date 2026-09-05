package com.dataverse.dataverserentals.gestionoperativa.controllers;

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
import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.DetallePropuestaLiquidacionPageDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.PropuestaLiquidacionDetailsDto;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.services.PropuestaLiquidacionService;
import com.dataverse.dataverserentals.security.context.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/propuestas")
@RestController
public class PropuestaLiquidacionController {

    private final SecurityContext securityContext;
    private final PropuestaLiquidacionService propuestaLiquidacionService;

    @GetMapping
    public ResponseEntity<Page<PropuestaLiquidacionDetailsDto>> findAll(
            Pageable pageable,
            @RequestParam Optional<Long> edificioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> fechaDesde,
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
    public ResponseEntity<PropuestaLiquidacionDetailsDto> getById(@PathVariable Long propuestaLiquidacionId)
            throws RecordNotFoundException, AccessDeniedException {
        log.info("Obteniendo detalle de propuesta de liquidación con id: {}", propuestaLiquidacionId);

        PropuestaLiquidacion propuesta = propuestaLiquidacionService
                .findPropuestaLiquidacionDetails(propuestaLiquidacionId);

        return ResponseEntity.ok(PropuestaLiquidacionDetailsDto.fromEntity(propuesta));
    }

    @GetMapping("{propuestaId}/detalles")
    public ResponseEntity<Page<DetallePropuestaLiquidacionPageDto>> findDetalles(
            @PathVariable Long propuestaId,
            Pageable pageable) throws AccessDeniedException {
        log.info("Listando detalles de propuesta de liquidación con propuestaId: {}", propuestaId);

        var detallesPage = propuestaLiquidacionService.findDetallesByPropuestaId(propuestaId, pageable)
                .map(DetallePropuestaLiquidacionPageDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(detallesPage));
    }

    @PostMapping
    ResponseEntity<Void> generarPropuestas() throws AccessDeniedException {
        log.info("Iniciando calculo de propuestas de liquidación");
        propuestaLiquidacionService.calcularPropuestas();

        return ResponseEntity.noContent().build();
    }

    @PostMapping("{propuestaLiquidacionId}/aprobados")
    public ResponseEntity<Void> aprobar(
            @PathVariable Long propuestaLiquidacionId)
            throws RecordNotFoundException, UnprocesableEntity, AccessDeniedException {
        log.info("Aprobando propuesta de liquidacion con id: {}", propuestaLiquidacionId);

        propuestaLiquidacionService.aprobarPropuestaLiquidacion(
                propuestaLiquidacionId, securityContext.getUsuarioPrincipal());

        return ResponseEntity.noContent().build();
    }
}
