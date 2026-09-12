package com.dataverse.dataverserentals.gestionadministrativa.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoContrato;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Table(name = "contratos")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unidad_id", insertable = false, updatable = false)
    private Unidad unidad;

    @Column(name = "unidad_id", nullable = false)
    private Long unidadId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "persona_id", insertable = false, updatable = false)
    private Persona persona;

    @Column(name = "persona_id", nullable = false)
    private Long personaId;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = true)
    private LocalDateTime fechaCierre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal alquilerMensual;

    @Column(nullable = true, length = 100)
    private String pdfUrl;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_creacion_id", insertable = false, updatable = false)
    private Usuario usuarioCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    public EstadoContrato getEstado() {
        LocalDateTime now = LocalDateTime.now();

        if (this.getFechaCierre() != null) {
            return EstadoContrato.CERRADO;
        }

        if (now.isAfter(this.getFechaFin())) {
            return EstadoContrato.VENCIDO;
        }

        if (now.isAfter(this.getFechaFin().minusDays(30))) {
            return EstadoContrato.POR_VENCER;
        }

        return EstadoContrato.VIGENTE;
    }

}
