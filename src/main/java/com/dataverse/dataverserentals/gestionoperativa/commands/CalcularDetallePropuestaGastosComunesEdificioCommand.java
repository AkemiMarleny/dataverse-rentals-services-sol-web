package com.dataverse.dataverserentals.gestionoperativa.commands;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionoperativa.entities.DetallePropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoConceptoPropuesta;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoGasto;
import com.dataverse.dataverserentals.gestionoperativa.repositories.GastoOperativoRepository;
import com.dataverse.dataverserentals.security.context.SecurityContext;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CalcularDetallePropuestaGastosComunesEdificioCommand {
    private final SecurityContext securityContext;
    private final GastoOperativoRepository gastoOperativoRepository;

    public DetallePropuestaLiquidacion execute(
            Edificio edificio,
            PropuestaLiquidacion propuesta) {
        log.info("generando detalle de propuesta de gastos comunes en edificioId: {}, edificioNombre: {}",
                edificio.getId(),
                edificio.getNombre());

        UsuarioPrincipal usuarioPrincipal = securityContext.getUsuarioPrincipal();

        BigDecimal montoTotalEnGastosOperativos = gastoOperativoRepository.sumMontoByEdificioAndTipoGastoAndPeriodo(
                edificio.getId(),
                List.of(TipoGasto.MANO_OBRA, TipoGasto.MATERIAL),
                propuesta.getFechaInicioPeriodo().toLocalDate(),
                propuesta.getFechaFinPeriodo().toLocalDate())
                .orElse(BigDecimal.ZERO);
        log.info("monto total en gastos operativos: {}, edificioId: {}, edificioNombre: {}",
                montoTotalEnGastosOperativos,
                edificio.getId(),
                edificio.getNombre());

        DetallePropuestaLiquidacion detallePropuesta = DetallePropuestaLiquidacion
                .builder()
                .propuestaLiquidacionId(propuesta.getId())
                .tipoConceptoPropuesta(TipoConceptoPropuesta.GASTOS_COMUNES)
                .monto(montoTotalEnGastosOperativos)
                .fechaCreacion(propuesta.getFechaCalculo())
                .usuarioCreacionId(usuarioPrincipal.usuarioId())
                .descripcion("Monto total en gastos operativos en áreas comunes")
                .build();

        return detallePropuesta;
    }
}
