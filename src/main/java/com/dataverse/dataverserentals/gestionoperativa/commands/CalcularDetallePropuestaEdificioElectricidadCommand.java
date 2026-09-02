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
@Slf4j
@RequiredArgsConstructor
public class CalcularDetallePropuestaEdificioElectricidadCommand {

    private final MedidorRepository medidorRepository;

    private final CalcularCostoConsumoMedidorCommand calcularCostoConsumoMedidorCommand;
    private final CalcularCostoConsumoEdificioCommand calcularCostoConsumoEdificioCommand;

    public Optional<DetallePropuestaLiquidacion> execute(
            Edificio edificio,
            PropuestaLiquidacion propuesta) {

        log.info("generando detalle de propuesta de electricidad en edificioId: {}, edificioNombre: {}",
                edificio.getId(),
                edificio.getNombre());

        Optional<Medidor> medidorGeneralElectricidadOpt = medidorRepository.findByEdificioAndTipoMedidor(
                edificio.getId(),
                TipoMedidor.GENERAL_ELECTRICIDAD);
        if (medidorGeneralElectricidadOpt.isEmpty()) {
            return Optional.empty();
        }

        Medidor medidorGeneralElectricidad = medidorGeneralElectricidadOpt.get();

        CostoConsumoPeriodo costoElectricidadGeneralPeriodo = calcularCostoConsumoMedidorCommand.execute(
                medidorGeneralElectricidad,
                propuesta.getFechaInicioPeriodo(),
                propuesta.getFechaFinPeriodo());

        log.info("costoElectricidadGeneralPeriodo: {}, edificioId: {}, edificioNombre: {}",
                costoElectricidadGeneralPeriodo.intoBigDecimal(),
                edificio.getId(),
                edificio.getNombre());

        BigDecimal costoElectricidadUnidadesEdificio = calcularCostoConsumoEdificioCommand.execute(
                edificio,
                propuesta.getFechaInicioPeriodo(),
                propuesta.getFechaFinPeriodo());

        log.info("costoElectricidadUnidadesEdificio: {}, edificioId: {}, edificioNombre: {}",
                costoElectricidadUnidadesEdificio,
                edificio.getId(),
                edificio.getNombre());

        CostoExcedente excedente = CostoExcedente.from(
                costoElectricidadGeneralPeriodo.intoBigDecimal(),
                costoElectricidadUnidadesEdificio);
        log.info("excedenteElectricidad: {}, edificioId: {}, edificioNombre: {}",
                excedente.intoBigDecimal(),
                edificio.getId(),
                edificio.getNombre());

        DetallePropuestaLiquidacion detallePropuesta = DetallePropuestaLiquidacion
                .builder()
                .propuestaLiquidacionId(propuesta.getId())
                .tipoConceptoPropuesta(TipoConceptoPropuesta.ELECTRICIDAD_GASTOS_COMUNES)
                .descripcion("Excedente de electricidad en áreas comunes")
                .monto(excedente.intoBigDecimal())
                .fechaCreacion(propuesta.getFechaCalculo())
                .usuarioCreacionId(propuesta.getUsuarioCreacionId())
                .build();

        return Optional.of(detallePropuesta);
    }
}
