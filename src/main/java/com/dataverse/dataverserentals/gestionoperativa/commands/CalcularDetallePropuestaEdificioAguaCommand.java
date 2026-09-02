package com.dataverse.dataverserentals.gestionoperativa.commands;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.MedidorRepository;
import com.dataverse.dataverserentals.gestionoperativa.entities.DetallePropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoConceptoPropuesta;
import com.dataverse.dataverserentals.gestionoperativa.valueobjects.CostoConsumoPeriodo;
import com.dataverse.dataverserentals.gestionoperativa.valueobjects.CostoExcedente;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalcularDetallePropuestaEdificioAguaCommand {

    private final MedidorRepository medidorRepository;

    private final CalcularCostoConsumoMedidorCommand calcularCostoConsumoMedidorCommand;
    private final CalcularCostoConsumoEdificioCommand calcularCostoConsumoEdificioCommand;

    public Optional<DetallePropuestaLiquidacion> execute(
            Edificio edificio,
            PropuestaLiquidacion propuesta) {

        log.info("generando detalle de propuesta de agua en edificioId: {}, edificioNombre: {}",
                edificio.getId(),
                edificio.getNombre());

        Optional<Medidor> medidorGeneralAguaOpt = medidorRepository.findByEdificioAndTipoMedidor(
                edificio.getId(),
                TipoMedidor.GENERAL_AGUA);
        if (medidorGeneralAguaOpt.isEmpty()) {
            return Optional.empty();
        }

        Medidor medidorGeneralAgua = medidorGeneralAguaOpt.get();

        CostoConsumoPeriodo costoAguaGeneralPeriodo = calcularCostoConsumoMedidorCommand.execute(
                medidorGeneralAgua,
                propuesta.getFechaInicioPeriodo(),
                propuesta.getFechaFinPeriodo());
        log.info("costoAguaGeneralPeriodo: {}, edificioId: {}, edificioNombre: {}",
                costoAguaGeneralPeriodo.intoBigDecimal(),
                edificio.getId(),
                edificio.getNombre());

        BigDecimal costoAguaUnidadesEdificio = calcularCostoConsumoEdificioCommand.execute(
                edificio,
                propuesta.getFechaInicioPeriodo(),
                propuesta.getFechaFinPeriodo());
        log.info("costoAguaUnidadesEdificio: {}, edificioId: {}, edificioNombre: {}",
                costoAguaUnidadesEdificio,
                edificio.getId(),
                edificio.getNombre());

        CostoExcedente excedente = CostoExcedente.from(
                costoAguaGeneralPeriodo.intoBigDecimal(),
                costoAguaUnidadesEdificio);
        log.info("excedenteAgua: {}, edificioId: {}, edificioNombre: {}",
                excedente.intoBigDecimal(),
                edificio.getId(),
                edificio.getNombre());

        DetallePropuestaLiquidacion detallePropuesta = DetallePropuestaLiquidacion
                .builder()
                .propuestaLiquidacionId(propuesta.getId())
                .tipoConceptoPropuesta(TipoConceptoPropuesta.AGUA_GASTOS_COMUNES)
                .descripcion("Excedente de agua en áreas comunes")
                .monto(excedente.intoBigDecimal())
                .fechaCreacion(propuesta.getFechaCalculo())
                .usuarioCreacionId(propuesta.getUsuarioCreacionId())
                .build();

        return Optional.of(detallePropuesta);
    }
}
