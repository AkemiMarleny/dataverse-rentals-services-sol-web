package com.dataverse.dataverserentals.gestionfinanciera.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
