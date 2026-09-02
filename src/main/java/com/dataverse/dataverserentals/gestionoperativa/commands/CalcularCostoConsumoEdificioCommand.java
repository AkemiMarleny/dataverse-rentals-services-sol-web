package com.dataverse.dataverserentals.gestionoperativa.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.MedidorRepository;
import com.dataverse.dataverserentals.gestionoperativa.valueobjects.CostoConsumoPeriodo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalcularCostoConsumoEdificioCommand {

    private final MedidorRepository medidorRepository;

    private final CalcularCostoConsumoMedidorCommand calcularCostoConsumoMedidorCommand;

    public BigDecimal execute(
            Edificio edificio,
            LocalDateTime fechaInicioPeriodo,
            LocalDateTime fechaFinPeriodo) {

        List<Medidor> medidoresEdificio = medidorRepository.findAllByEdificioAndTipoMedidor(
                edificio.getId(),
                TipoMedidor.ELECTRICIDAD);

        BigDecimal consumoTotalEdificio = medidoresEdificio.stream()
                .parallel()
                .map(medidor -> {
                    return calcularCostoConsumoMedidorCommand.execute(
                            medidor,
                            fechaInicioPeriodo,
                            fechaFinPeriodo);
                })
                .map(CostoConsumoPeriodo::intoBigDecimal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return consumoTotalEdificio;
    }
}
