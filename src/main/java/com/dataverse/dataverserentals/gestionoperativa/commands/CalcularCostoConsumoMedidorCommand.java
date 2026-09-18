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

        log.info("calculando costo consumo medidor [medidorId={}] [fechaInicioPeriodo={}] [fechaFinPeriodo={}]",
                medidor.getId(),
                fechaInicioPeriodo,
                fechaFinPeriodo);

        BigDecimal primeraLecturaDeMedidor = lecturaHistoricaRepository
                .findFirstByMedidorIdAndFechaLecturaBetweenOrderByFechaLecturaAsc(
                        medidor.getId(),
                        fechaInicioPeriodo.toLocalDate(),
                        fechaFinPeriodo.toLocalDate())
                .map(lectura -> lectura.getValorLectura())
                .orElse(BigDecimal.valueOf(0.0D));
        log.info("[primeraLecturaDeMedidor={}] [medidorId={}] [fechaInicioPeriodo={}] [fechaFinPeriodo={}]",
                primeraLecturaDeMedidor,
                medidor.getId(),
                fechaInicioPeriodo,
                fechaFinPeriodo);

        BigDecimal ultimaLecturaDeMedidor = lecturaHistoricaRepository
                .findFirstByMedidorIdAndFechaLecturaBetweenOrderByFechaLecturaDesc(
                        medidor.getId(),
                        fechaInicioPeriodo.toLocalDate(),
                        fechaFinPeriodo.toLocalDate())
                .map(lectura -> lectura.getValorLectura())
                .orElse(BigDecimal.valueOf(0.0D));
        log.info("[ultimaLecturaDeMedidor={}] [medidorId={}] [fechaInicioPeriodo={}] [fechaFinPeriodo={}]",
                ultimaLecturaDeMedidor,
                medidor.getId(),
                fechaInicioPeriodo,
                fechaFinPeriodo);

        ConsumoKwh consumoKwh = ConsumoKwh.from(
                primeraLecturaDeMedidor,
                ultimaLecturaDeMedidor);
        log.info("[consumoKwh={}] [medidorId={}] [fechaInicioPeriodo={}] [fechaFinPeriodo={}]",
                consumoKwh.intoBigDeciaml(),
                medidor.getId(),
                fechaInicioPeriodo,
                fechaFinPeriodo);

        CostoConsumoPeriodo costoConsumoPeriodo = CostoConsumoPeriodo.from(consumoKwh);

        log.info("[consumoKwh={}] [costoConsumoPeriodo={}] [medidorId={}] [fechaInicioPeriodo={}] [fechaFinPeriodo={}]",
                consumoKwh.intoBigDeciaml(),
                costoConsumoPeriodo.intoBigDecimal(),
                medidor.getId(),
                fechaInicioPeriodo,
                fechaFinPeriodo);

        return costoConsumoPeriodo;
    }
}
