package com.dataverse.dataverserentals.gestionoperativa.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestioncampo.repositories.LecturaHistoricaRepository;
import com.dataverse.dataverserentals.gestionoperativa.valueobjects.ConsumoKwh;
import com.dataverse.dataverserentals.gestionoperativa.valueobjects.CostoConsumoPeriodo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CalcularCostoConsumoMedidorCommand {

    private final LecturaHistoricaRepository lecturaHistoricaRepository;

    public CostoConsumoPeriodo execute(
            Medidor medidor,
            LocalDateTime fechaInicioPeriodo,
            LocalDateTime fechaFinPeriodo) {

        BigDecimal primeraLecturaDeMedidor = lecturaHistoricaRepository
                .findFirstByMedidorIdAndFechaLecturaBetweenOrderByFechaLecturaAsc(
                        medidor.getId(),
                        fechaInicioPeriodo,
                        fechaFinPeriodo)
                .map(lectura -> lectura.getValorLectura())
                .orElse(BigDecimal.valueOf(0.0D));

        BigDecimal ultimaLecturaDeMedidor = lecturaHistoricaRepository
                .findFirstByMedidorIdAndFechaLecturaBetweenOrderByFechaLecturaDesc(
                        medidor.getId(),
                        fechaInicioPeriodo,
                        fechaFinPeriodo)
                .map(lectura -> lectura.getValorLectura())
                .orElse(BigDecimal.valueOf(0.0D));

        ConsumoKwh consumoKwh = ConsumoKwh.from(
                primeraLecturaDeMedidor,
                ultimaLecturaDeMedidor);

        return CostoConsumoPeriodo.from(consumoKwh);
    }
}
