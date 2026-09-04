package com.dataverse.dataverserentals.gestionoperativa.controllers;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.common.dto.Page;
import com.dataverse.dataverserentals.common.exceptions.AccessDeniedException;
import com.dataverse.dataverserentals.common.factories.PageFactory;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.response.PropuestaLiquidacionDto;
import com.dataverse.dataverserentals.gestionoperativa.services.PropuestaLiquidacionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/propuestas")
@RestController
public class PropuestaLiquidacionController {

    private final PropuestaLiquidacionService propuestaLiquidacionService;

    @GetMapping
    public ResponseEntity<Page<PropuestaLiquidacionDto>> findAll(
            Pageable pageable,
            @RequestParam Optional<Long> edificioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> fechaDesde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> fechaHasta) {
        log.info(
                "Listando propuestas de liquidación con filtros - edificioId: {}, fechaDesde: {}, fechaHasta: {}, pageable: {}",
                edificioId, fechaDesde, fechaHasta, pageable);

        var propuestasPage = propuestaLiquidacionService.findAllPropuestasLiquidacion(
                pageable, edificioId, fechaDesde, fechaHasta)
                .map(PropuestaLiquidacionDto::fromEntity);

        return ResponseEntity.ok(PageFactory.from(propuestasPage));
    }

    @PostMapping
    ResponseEntity<Void> generarPropuestas() throws AccessDeniedException {
        log.info("Iniciando calculo de propuestas de liquidación");
        propuestaLiquidacionService.calcularPropuestas();

        return ResponseEntity.noContent().build();
    }
}
