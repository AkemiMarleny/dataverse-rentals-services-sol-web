package com.dataverse.dataverserentals.gestionoperativa.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionoperativa.enums.EstadoPropuesta;
import com.dataverse.dataverserentals.security.context.UsuarioPrincipal;

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
@Table(name = "propuestas_liquidacion")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropuestaLiquidacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String codigo;

    @Column(name = "edificio_id", nullable = false)
    private Long edificioId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "edificio_id", insertable = false, updatable = false)
    private Edificio edificio;

    @Column(nullable = false)
    private LocalDateTime fechaCalculo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPropuesta estadoCode;

    @Column(nullable = false)
    private LocalDateTime fechaInicioPeriodo;

    @Column(nullable = false)
    private LocalDateTime fechaFinPeriodo;

    @Column(name = "aprobador_id", nullable = true)
    private Long aprobadorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aprobador_id", insertable = false, updatable = false)
    private Usuario aprobador;

    @Column(name = "fecha_aprobacion", nullable = true)
    private LocalDateTime fechaAprobacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_creacion_id", insertable = false, updatable = false)
    private Usuario usuarioCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    @Transient
    @Builder.Default()
    private List<DetallePropuestaLiquidacion> detalles = new ArrayList<>();

    public void agregarDetalle(DetallePropuestaLiquidacion detallePropuesta) {
        this.montoTotal = this.montoTotal.add(detallePropuesta.getMonto());

        this.detalles.add(detallePropuesta);
    }

    public void aprobar(UsuarioPrincipal aprobador) {
        this.setAprobadorId(aprobador.usuarioId());
        this.setEstadoCode(EstadoPropuesta.APROBADO);
        this.setFechaAprobacion(LocalDateTime.now());
    }
}
