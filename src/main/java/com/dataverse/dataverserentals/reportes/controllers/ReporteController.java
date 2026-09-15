package com.dataverse.dataverserentals.reportes.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.reportes.controllers.dtos.response.IngresoEgresoMensualDto;
import com.dataverse.dataverserentals.reportes.controllers.dtos.response.IngresoEgresoMensualItemDto;
import com.dataverse.dataverserentals.reportes.services.ReporteFinancieroService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reportes")
public class ReporteController {

    private final ReporteFinancieroService reporteFinancieroService;

    @GetMapping("/resumen-anual")
    public ResponseEntity<IngresoEgresoMensualDto> getResumenAnual(
            @RequestParam(required = false) Integer anio) {
        log.info("Generando resumen anual para el año: {}", anio != null ? anio : "actual");

        List<IngresoEgresoMensualItemDto> resumen = reporteFinancieroService.getResumenAnual(anio);
        return ResponseEntity.ok(new IngresoEgresoMensualDto(
                anio, resumen));
    }
}
