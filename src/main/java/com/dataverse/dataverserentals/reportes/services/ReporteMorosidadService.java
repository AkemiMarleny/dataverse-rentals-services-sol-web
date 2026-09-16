package com.dataverse.dataverserentals.reportes.services;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dataverse.dataverserentals.gestionfinanciera.repositories.PagoRepository;
import com.dataverse.dataverserentals.reportes.controllers.dtos.response.MorosidadDto;
import com.dataverse.dataverserentals.reportes.controllers.dtos.response.MorosidadItemDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteMorosidadService {

    private final PagoRepository pagoRepository;

    private static final String[] MESES = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    public MorosidadDto getMorosidadPorAnio(Integer anio) {
        int anioFinal = anio != null ? anio : Year.now().getValue();

        List<Object[]> resultados = pagoRepository.findPagosMorososAgrupadosByAnio(anioFinal);

        Map<Integer, MorosidadItemData> datosPorMes = new HashMap<>();
        long totalPagosMorosos = 0;

        for (Object[] fila : resultados) {
            int mes = ((Number) fila[0]).intValue();
            long p1a30 = fila[1] != null ? ((Number) fila[1]).longValue() : 0L;
            long p31a60 = fila[2] != null ? ((Number) fila[2]).longValue() : 0L;
            long pMas60 = fila[3] != null ? ((Number) fila[3]).longValue() : 0L;

            datosPorMes.put(mes, new MorosidadItemData(p1a30, p31a60, pMas60));
            totalPagosMorosos += p1a30 + p31a60 + pMas60;
        }

        List<MorosidadItemDto> resumen = new ArrayList<>();
        double suma1a30 = 0;
        double suma31a60 = 0;
        double sumaMas60 = 0;

        for (int mes = 1; mes <= 12; mes++) {
            MorosidadItemData data = datosPorMes.getOrDefault(mes,
                    new MorosidadItemData(0L, 0L, 0L));

            resumen.add(new MorosidadItemDto(
                    mes,
                    MESES[mes - 1],
                    data.p1a30(),
                    data.p31a60(),
                    data.pMas60()));

            suma1a30 += data.p1a30();
            suma31a60 += data.p31a60();
            sumaMas60 += data.pMas60();
        }

        double promedio1a30 = Math.round((suma1a30 / 12) * 100.0) / 100.0;
        double promedio31a60 = Math.round((suma31a60 / 12) * 100.0) / 100.0;
        double promedioMas60 = Math.round((sumaMas60 / 12) * 100.0) / 100.0;

        return new MorosidadDto(
                anioFinal,
                totalPagosMorosos,
                promedio1a30,
                promedio31a60,
                promedioMas60,
                resumen);
    }

    private record MorosidadItemData(long p1a30, long p31a60, long pMas60) {
    }
}
