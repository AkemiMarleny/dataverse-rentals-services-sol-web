package com.dataverse.dataverserentals.gestionoperativa.commands;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.MedidorRepository;
import com.dataverse.dataverserentals.gestionoperativa.valueobjects.CostoConsumoPeriodo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CalcularCostoConsumoMedidorUnidadCommand {

    private final MedidorRepository medidorRepository;

    private final CalcularCostoConsumoMedidorCommand calcularCostoConsumoMedidorCommand;

    public Optional<CostoConsumoPeriodo> execute(
            Unidad unidad,
            TipoMedidor tipoMedidor,
            LocalDateTime fechaInicioPeriodo,
            LocalDateTime fechaFinPeriodo) {
        Optional<Medidor> medidorUnidadOpt = medidorRepository.findByUnidadAndTipoMedidor(
                unidad.getId(),
                tipoMedidor);
        if (medidorUnidadOpt.isEmpty()) {
            log.warn("medidor de unidad no presente [unidadId={}] [tipoMedidor={}]",
                    unidad.getId(),
                    tipoMedidor);
            return Optional.empty();
        }

        Medidor medidorUnidad = medidorUnidadOpt.get();

        CostoConsumoPeriodo costoMedidorUnidadPeriodo = calcularCostoConsumoMedidorCommand.execute(
                medidorUnidad,
                fechaInicioPeriodo,
                fechaFinPeriodo);

        log.info("[costoMedidorUnidadPeriodo={}] [unidadId={}] [unidadNumeroPuerta={}]",
                costoMedidorUnidadPeriodo.intoBigDecimal(),
                unidad.getId(),
                unidad.getNumeroPuerta());

        return Optional.of(costoMedidorUnidadPeriodo);
    }
}
