package com.dataverse.dataverserentals.gestionfinanciera.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Contrato;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Unidad;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoMedidor;
import com.dataverse.dataverserentals.gestionadministrativa.repositories.UnidadRepository;
import com.dataverse.dataverserentals.gestionfinanciera.entities.DetalleRecibo;
import com.dataverse.dataverserentals.gestionfinanciera.entities.Recibo;
import com.dataverse.dataverserentals.gestionfinanciera.enums.EstadoRecibo;
import com.dataverse.dataverserentals.gestionfinanciera.enums.TipoConceptoRecibo;
import com.dataverse.dataverserentals.gestionfinanciera.repositories.DetalleReciboRepository;
import com.dataverse.dataverserentals.gestionfinanciera.repositories.ReciboRepository;
import com.dataverse.dataverserentals.gestionfinanciera.valueobjects.MontoProrrateado;
import com.dataverse.dataverserentals.gestionoperativa.commands.CalcularCostoConsumoMedidorUnidadCommand;
import com.dataverse.dataverserentals.gestionoperativa.entities.DetallePropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.entities.PropuestaLiquidacion;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoConceptoPropuesta;
import com.dataverse.dataverserentals.gestionoperativa.repositories.DetallePropuestaLiquidacionRepository;
import com.dataverse.dataverserentals.gestionoperativa.valueobjects.CostoConsumoPeriodo;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenerarReciboContratoCommand {
    DateTimeFormatter reciboFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DetallePropuestaLiquidacionRepository detallePropuestaLiquidacionRepository;
    private final ReciboRepository reciboRepository;
    private final DetalleReciboRepository detalleReciboRepository;
    private final UnidadRepository unidadRepository;

    private final CalcularCostoConsumoMedidorUnidadCommand calcularCostoConsumoMedidorUnidadCommand;

    @Transactional(rollbackFor = Exception.class)
    public void execute(
            UsuarioPrincipal usuarioPrincipal,
            PropuestaLiquidacion propuestaActivaPeriodo,
            Contrato contrato,
            BigDecimal areaArrendableEdificio,
            LocalDateTime fechaCalculo,
            LocalDateTime fechaInicioPeriodo,
            LocalDateTime fechaFinPeriodo) {
        log.info("generando recibo [contratoId={}] [areaArrendableEdificio={}]",
                contrato.getId(),
                areaArrendableEdificio);

        Optional<Unidad> unidadContratoOpt = unidadRepository.findById(contrato.getUnidadId());
        if (unidadContratoOpt.isEmpty()) {
            log.error("contrato sin unidad [contratoId={}] [unidadId={}]",
                    contrato.getId(),
                    unidadContratoOpt.get()
                            .getId());
            return;
        }

        Unidad unidadContrato = unidadContratoOpt.get();
        log.info("generando recibo [contratoId={}] [unidadId={}]",
                contrato.getId(),
                unidadContrato.getId());

        Recibo recibo = Recibo.builder()
                .contratoId(contrato.getId())
                .fechaEmision(fechaCalculo)
                .fechaVencimiento(fechaFinPeriodo)
                .montoTotal(BigDecimal.ZERO)
                .estadoCode(EstadoRecibo.PENDIENTE)
                .fechaCreacion(fechaCalculo)
                .build();
        recibo = reciboRepository.save(recibo);

        DetalleRecibo detalleReciboAlquiler = DetalleRecibo.builder()
                .reciboId(recibo.getId())
                .tipoConcepto(TipoConceptoRecibo.ALQUILER_MENSUAL)
                .descripcion("Alquiler mensual")
                .monto(contrato.getAlquilerMensual())
                .fechaCreacion(fechaCalculo)
                .usuarioCreacionId(usuarioPrincipal.usuarioId())
                .build();
        recibo.agregarDetalle(detalleReciboAlquiler);

        Optional<DetallePropuestaLiquidacion> detallePropuestaGastosComunes = detallePropuestaLiquidacionRepository
                .findFirstByPropuestaLiquidacionIdAndTipoConceptoPropuesta(
                        propuestaActivaPeriodo.getId(),
                        TipoConceptoPropuesta.GASTOS_COMUNES);
        if (detallePropuestaGastosComunes.isPresent()) {
            log.info("prorrateando gastos comunes [areaArrendableEdificio={}] [propuestaGastosComunes={}]",
                    areaArrendableEdificio,
                    detallePropuestaGastosComunes.get()
                            .getMonto());
            MontoProrrateado montoGastosComunesProrrateado = unidadContrato.prorratearMonto(
                    areaArrendableEdificio,
                    detallePropuestaGastosComunes.get()
                            .getMonto());

            DetalleRecibo detalleReciboGastosComunes = DetalleRecibo.builder()
                    .reciboId(recibo.getId())
                    .tipoConcepto(TipoConceptoRecibo.GASTOS_COMUNES)
                    .descripcion(
                            String.format("Gastos en áreas comunes periodo %s - %s",
                                    fechaInicioPeriodo.format(reciboFormatter),
                                    fechaFinPeriodo.format(reciboFormatter)))
                    .monto(montoGastosComunesProrrateado.intoBigDecimal())
                    .fechaCreacion(fechaCalculo)
                    .usuarioCreacionId(usuarioPrincipal.usuarioId())
                    .build();
            recibo.agregarDetalle(detalleReciboGastosComunes);
        }

        Optional<DetallePropuestaLiquidacion> detallePropuestaElectricidad = detallePropuestaLiquidacionRepository
                .findFirstByPropuestaLiquidacionIdAndTipoConceptoPropuesta(
                        propuestaActivaPeriodo.getId(),
                        TipoConceptoPropuesta.ELECTRICIDAD_GASTOS_COMUNES);
        if (detallePropuestaElectricidad.isPresent()) {
            log.info("prorrateando electricidad areas comunes [areaArrendableEdificio={}] [propuestaGastosComunes={}]",
                    areaArrendableEdificio,
                    detallePropuestaElectricidad.get()
                            .getMonto());

            MontoProrrateado gastosElectricidadProrrateado = unidadContrato.prorratearMonto(
                    areaArrendableEdificio,
                    detallePropuestaElectricidad.get()
                            .getMonto());

            DetalleRecibo detalleReciboElectricidadProrrateado = DetalleRecibo.builder()
                    .reciboId(recibo.getId())
                    .tipoConcepto(TipoConceptoRecibo.ELECTRICIDAD_GASTOS_COMUNES)
                    .descripcion(String.format("Gastos de electricidad en áreas comunes periodo %s - %s",
                            fechaInicioPeriodo.format(reciboFormatter),
                            fechaFinPeriodo.format(reciboFormatter)))
                    .monto(gastosElectricidadProrrateado.intoBigDecimal())
                    .fechaCreacion(fechaCalculo)
                    .usuarioCreacionId(usuarioPrincipal.usuarioId())
                    .build();
            recibo.agregarDetalle(detalleReciboElectricidadProrrateado);

        }

        Optional<DetallePropuestaLiquidacion> detallePropuestaAgua = detallePropuestaLiquidacionRepository
                .findFirstByPropuestaLiquidacionIdAndTipoConceptoPropuesta(
                        propuestaActivaPeriodo.getId(),
                        TipoConceptoPropuesta.AGUA_GASTOS_COMUNES);
        if (detallePropuestaAgua.isPresent()) {
            log.info("prorrateando agua areas comunes [areaArrendableEdificio={}] [propuestaGastosComunes={}]",
                    areaArrendableEdificio,
                    detallePropuestaAgua.get()
                            .getMonto());
            MontoProrrateado gastosAguaProrrateado = unidadContrato.prorratearMonto(
                    areaArrendableEdificio,
                    detallePropuestaAgua.get()
                            .getMonto());

            DetalleRecibo detalleReciboAguaProrrateado = DetalleRecibo.builder()
                    .reciboId(recibo.getId())
                    .tipoConcepto(TipoConceptoRecibo.AGUA_GASTOS_COMUNES)
                    .descripcion(String.format("Gastos de agua en áreas comunes periodo %s - %s",
                            fechaInicioPeriodo.format(reciboFormatter),
                            fechaFinPeriodo.format(reciboFormatter)))
                    .monto(gastosAguaProrrateado.intoBigDecimal())
                    .fechaCreacion(fechaCalculo)
                    .usuarioCreacionId(usuarioPrincipal.usuarioId())
                    .build();
            recibo.agregarDetalle(detalleReciboAguaProrrateado);
        }

        Optional<CostoConsumoPeriodo> costoConsumoElectricidad = calcularCostoConsumoMedidorUnidadCommand.execute(
                contrato.getUnidad(),
                TipoMedidor.ELECTRICIDAD,
                fechaInicioPeriodo,
                fechaFinPeriodo);
        if (costoConsumoElectricidad.isPresent()) {
            DetalleRecibo detalleReciboElectricidad = DetalleRecibo.builder()
                    .reciboId(recibo.getId())
                    .tipoConcepto(TipoConceptoRecibo.ELECTRICIDAD)
                    .descripcion(String.format("Consumo electricidad periodo %s - %s",
                            fechaInicioPeriodo.format(reciboFormatter),
                            fechaFinPeriodo.format(reciboFormatter)))
                    .monto(costoConsumoElectricidad.get().intoBigDecimal())
                    .fechaCreacion(fechaCalculo)
                    .usuarioCreacionId(usuarioPrincipal.usuarioId())
                    .build();
            recibo.agregarDetalle(detalleReciboElectricidad);
        }

        Optional<CostoConsumoPeriodo> costoConsumoAgua = calcularCostoConsumoMedidorUnidadCommand.execute(
                contrato.getUnidad(),
                TipoMedidor.AGUA,
                fechaInicioPeriodo,
                fechaFinPeriodo);
        if (costoConsumoAgua.isPresent()) {
            DetalleRecibo detalleReciboAgua = DetalleRecibo.builder()
                    .reciboId(recibo.getId())
                    .tipoConcepto(TipoConceptoRecibo.AGUA)
                    .descripcion(String.format("Consumo de agua periodo %s - %s",
                            fechaInicioPeriodo.format(reciboFormatter),
                            fechaFinPeriodo.format(reciboFormatter)))
                    .monto(costoConsumoAgua.get().intoBigDecimal())
                    .fechaCreacion(fechaCalculo)
                    .usuarioCreacionId(usuarioPrincipal.usuarioId())
                    .build();
            recibo.agregarDetalle(detalleReciboAgua);
        }

        reciboRepository.save(recibo);
        detalleReciboRepository.saveAll(recibo.getDetalles());
    }
}
