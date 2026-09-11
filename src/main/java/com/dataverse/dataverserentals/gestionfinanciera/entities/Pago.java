package com.dataverse.dataverserentals.gestionfinanciera.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionfinanciera.enums.EstadoPago;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pagos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recibo_id", nullable = false)
    private Long reciboId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recibo_id", insertable = false, updatable = false)
    private Recibo recibo;

    @Column(name = "validador_id", nullable = true)
    private Long validadorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validador_id", insertable = false, updatable = false)
    private Usuario validador;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal montoPagado;

    @Column(name = "url_voucher", length = 255, nullable = true)
    private String urlVoucher;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_codigo", nullable = true)
    private EstadoPago estadoCodigo;

    @Column(name = "fecha_aprobacion", nullable = true)
    private LocalDateTime fechaAprobacion;

    @Column(name = "fecha_rechazo", nullable = true)
    private LocalDateTime fechaRechazo;

    @Column(name = "motivo_rechazo", columnDefinition = "TEXT", nullable = true)
    private String motivoRechazo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_creacion_id", insertable = false, updatable = false)
    private Usuario usuarioCreacion;
}
