package com.dataverse.dataverserentals.gestionfinanciera.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dataverse.dataverserentals.common.exceptions.UnprocesableEntity;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Contrato;
import com.dataverse.dataverserentals.gestionfinanciera.enums.EstadoRecibo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "recibos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recibo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String codigo;

    @Column(name = "contrato_id", nullable = false)
    private Long contratoId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contrato_id", insertable = false, updatable = false)
    private Contrato contrato;

    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    @Column(nullable = false)
    private LocalDateTime fechaVencimiento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPagado;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoRecibo estadoCode;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Transient
    @Builder.Default()
    private List<DetalleRecibo> detalles = new ArrayList<>();

    public void agregarDetalle(DetalleRecibo detalleRecibo) {
        this.montoTotal = this.montoTotal.add(detalleRecibo.getMonto());

        this.detalles.add(detalleRecibo);
    }

    public void agregarPago(Pago pago) throws UnprocesableEntity {
        var montoPagado = this.montoPagado.add(pago.getMontoPagado());
        log.info("intentando realizar pago [montoPagar={}] [montoPagado={}] [montoTotal={}]",
                pago.getMontoPagado(),
                montoPagado,
                this.getMontoTotal());

        if (montoPagado.compareTo(this.montoTotal) < 0) {
            var message = String.format(
                    "El recibo no permite pagos superiores al monto total monto pagado: {}, monto total: {}",
                    montoPagado,
                    this.getMontoTotal());
            throw new UnprocesableEntity(message);
        }

        this.montoPagado = montoPagado;
        this.estadoCode = this.newEstadoPago();
    }

    private EstadoRecibo newEstadoPago() {
        if (this.montoPagado.compareTo(this.montoTotal) == 0) {
            return EstadoRecibo.ABONADO;
        }

        return EstadoRecibo.PARCIALMENTE_PAGADO;
    }
}
