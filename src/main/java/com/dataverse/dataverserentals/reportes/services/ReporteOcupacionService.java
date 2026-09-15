package com.dataverse.dataverserentals.reportes.services;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dataverse.dataverserentals.gestionadministrativa.repositories.ContratoRepository;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UnidadRepository;
import com.dataverse.dataverserentals.reportes.controllers.dtos.response.OcupacionUnidadesDto;
import com.dataverse.dataverserentals.reportes.controllers.dtos.response.OcupacionUnidadesItemDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteOcupacionService {

    private final UnidadRepository unidadRepository;
    private final ContratoRepository contratoRepository;

    private static final String[] MESES = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    public OcupacionUnidadesDto getOcupacionPorAnio(Integer anio) {
        int anioFinal = anio != null ? anio : Year.now().getValue();
        long totalUnidades = unidadRepository.count();

        List<OcupacionUnidadesItemDto> resumen = new ArrayList<>();
        double sumaPorcentajes = 0;

        for (int mes = 1; mes <= 12; mes++) {
            LocalDateTime primeroDiaMes = LocalDateTime.of(anioFinal, Month.of(mes), 1, 0, 0, 0);
            LocalDateTime ultimoDiaMes = primeroDiaMes.plusMonths(1).minusSeconds(1);

            Long unidadesOcupadas = contratoRepository.countUnidadesOcupadasByMes(
                    primeroDiaMes, ultimoDiaMes);

            long ocupadas = unidadesOcupadas != null ? unidadesOcupadas : 0L;
            long disponibles = totalUnidades - ocupadas;
            double porcentajeOcupacion = totalUnidades > 0
                    ? (double) ocupadas / totalUnidades * 100
                    : 0;

            sumaPorcentajes += porcentajeOcupacion;

            resumen.add(new OcupacionUnidadesItemDto(
                    mes,
                    MESES[mes - 1],
                    ocupadas,
                    disponibles));
        }

        double promedioOcupacion = Math.round((sumaPorcentajes / 12) * 100.0) / 100.0;

        return new OcupacionUnidadesDto(
                anioFinal,
                totalUnidades,
                promedioOcupacion,
                resumen);
    }
}
