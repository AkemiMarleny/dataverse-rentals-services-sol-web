package com.dataverse.dataverserentals.reportes.services;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dataverse.dataverserentals.gestionfinanciera.repositories.PagoRepository;
import com.dataverse.dataverserentals.gestionoperativa.repositories.GastoOperativoRepository;
import com.dataverse.dataverserentals.reportes.controllers.dtos.response.IngresoEgresoMensualItemDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteFinancieroService {

    private final PagoRepository pagoRepository;
    private final GastoOperativoRepository gastoOperativoRepository;

    private static final String[] MESES = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    public List<IngresoEgresoMensualItemDto> getResumenAnual(Integer anio) {
        int anioFinal = anio != null ? anio : Year.now().getValue();

        Map<Integer, BigDecimal> ingresosPorMes = obtenerIngresosPorMes(anioFinal);
        Map<Integer, BigDecimal> egresosPorMes = obtenerEgresosPorMes(anioFinal);

        List<IngresoEgresoMensualItemDto> resultado = new ArrayList<>();

        for (int mes = 1; mes <= 12; mes++) {
            BigDecimal montoIngreso = ingresosPorMes.getOrDefault(mes, BigDecimal.ZERO);
            BigDecimal montoEgreso = egresosPorMes.getOrDefault(mes, BigDecimal.ZERO);

            resultado.add(new IngresoEgresoMensualItemDto(
                    montoIngreso,
                    montoEgreso,
                    mes,
                    MESES[mes - 1]));
        }

        return resultado;
    }

    private Map<Integer, BigDecimal> obtenerIngresosPorMes(int anio) {
        Map<Integer, BigDecimal> ingresosPorMes = new HashMap<>();
        List<Object[]> resultados = pagoRepository.findIngresosAgrupadosByAnio(anio);

        for (Object[] fila : resultados) {
            int mes = ((Number) fila[0]).intValue();
            BigDecimal total = fila[1] != null ? (BigDecimal) fila[1] : BigDecimal.ZERO;
            ingresosPorMes.put(mes, total);
        }

        return ingresosPorMes;
    }

    private Map<Integer, BigDecimal> obtenerEgresosPorMes(int anio) {
        Map<Integer, BigDecimal> egresosPorMes = new HashMap<>();
        List<Object[]> resultados = gastoOperativoRepository.findEgresosAgrupadosByAnio(anio);

        for (Object[] fila : resultados) {
            int mes = ((Number) fila[0]).intValue();
            BigDecimal total = fila[1] != null ? (BigDecimal) fila[1] : BigDecimal.ZERO;
            egresosPorMes.put(mes, total);
        }

        return egresosPorMes;
    }
}
