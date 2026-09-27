package com.dataverse.dataverserentals.reportes.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dataverse.dataverserentals.reportes.controllers.dtos.response.IngresoEgresoMensualDto;
import com.dataverse.dataverserentals.reportes.controllers.dtos.response.IngresoEgresoMensualItemDto;
import com.dataverse.dataverserentals.reportes.controllers.dtos.response.MorosidadDto;
import com.dataverse.dataverserentals.reportes.controllers.dtos.response.OcupacionUnidadesDto;
import com.dataverse.dataverserentals.reportes.services.ReporteFinancieroService;
import com.dataverse.dataverserentals.reportes.services.ReporteMorosidadService;
import com.dataverse.dataverserentals.reportes.services.ReporteOcupacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reportes")
@Tag(name = "Reportes", description = "Reportes financieros, de morosidad y de ocupación")
public class ReporteController {

    private final ReporteFinancieroService reporteFinancieroService;
    private final ReporteOcupacionService reporteOcupacionService;
    private final ReporteMorosidadService reporteMorosidadService;

    @GetMapping("/resumen-anual")
    @Operation(
            summary = "Resumen anual de ingresos y egresos",
            description = "Devuelve el resumen mensual de ingresos y egresos del año indicado (o el año actual si no se envía).")
    @ApiResponse(responseCode = "200", description = "Resumen anual de ingresos y egresos")
    public ResponseEntity<IngresoEgresoMensualDto> getResumenAnual(
            @Parameter(description = "Año a consultar (si no se envía, se usa el año actual)", example = "2026")
            @RequestParam(required = false) Integer anio) {
        log.info("Generando resumen anual para el año: {}", anio != null ? anio : "actual");

        List<IngresoEgresoMensualItemDto> resumen = reporteFinancieroService.getResumenAnual(anio);
        return ResponseEntity.ok(new IngresoEgresoMensualDto(
                anio, resumen));
    }

    @GetMapping("/ocupacion-unidades")
    @Operation(
            summary = "Reporte de ocupación de unidades",
            description = "Devuelve la ocupación mensual de unidades del año indicado (o el año actual si no se envía).")
    @ApiResponse(responseCode = "200", description = "Reporte de ocupación de unidades")
    public ResponseEntity<OcupacionUnidadesDto> getOcupacionUnidades(
            @Parameter(description = "Año a consultar (si no se envía, se usa el año actual)", example = "2026")
            @RequestParam(required = false) Integer anio) {
        log.info("Generando reporte de ocupación de unidades para el año: {}", anio != null ? anio : "actual");

        OcupacionUnidadesDto reporte = reporteOcupacionService.getOcupacionPorAnio(anio);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/morosidad")
    @Operation(
            summary = "Reporte de morosidad",
            description = "Devuelve el reporte mensual de pagos morosos del año indicado (o el año actual si no se envía).")
    @ApiResponse(responseCode = "200", description = "Reporte de morosidad")
    public ResponseEntity<MorosidadDto> getMorosidad(
            @Parameter(description = "Año a consultar (si no se envía, se usa el año actual)", example = "2026")
            @RequestParam(required = false) Integer anio) {
        log.info("Generando reporte de morosidad para el año: {}", anio != null ? anio : "actual");

        MorosidadDto reporte = reporteMorosidadService.getMorosidadPorAnio(anio);
        return ResponseEntity.ok(reporte);
    }
}