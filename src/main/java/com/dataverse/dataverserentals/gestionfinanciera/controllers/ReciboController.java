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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/recibos")
public class ReciboController {

    private final ReciboService reciboService;
    private final DetalleReciboService detalleReciboService;

    @GetMapping
    public ResponseEntity<Page<ReciboDto>> findAll(
            Pageable pageable,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> fechaDesde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> fechaHasta,
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
    public ResponseEntity<ReciboDetailDto> findById(
            @PathVariable Long reciboId) throws AccessDeniedException, RecordNotFoundException {
        log.info("Obteniendo recibo con ID: {}", reciboId);

        var recibo = reciboService.findReciboById(reciboId);

        return ResponseEntity.ok(ReciboDetailDto.fromEntity(recibo));
    }

    @PostMapping
    ResponseEntity<Void> generarRecibos() throws AccessDeniedException {
        reciboService.generarRecibos();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{reciboId}/detalles")
    public ResponseEntity<Page<DetalleReciboDto>> findDetallesByReciboId(
            @PathVariable Long reciboId,
            Pageable pageable) throws AccessDeniedException, RecordNotFoundException {
        log.info("Listando detalles del recibo con ID: {}", reciboId);

        var detallesPage = detalleReciboService.findDetallesByReciboId(reciboId, pageable)
                .map(DetalleReciboDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(detallesPage));
    }
}
