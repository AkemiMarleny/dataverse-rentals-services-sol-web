package com.dataverse.dataverserentals.gestionadministrativa.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoContrato;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "v_contratos")
@Entity
@Immutable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VContrato {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unidad_id", insertable = false, updatable = false)
    private Unidad unidad;

    @Column(name = "unidad_id")
    private Long unidadId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "persona_id", insertable = false, updatable = false)
    private Persona persona;

    @Column(name = "persona_id")
    private Long personaId;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private LocalDateTime fechaCierre;

    private BigDecimal alquilerMensual;

    private String pdfUrl;

    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_creacion_id", insertable = false, updatable = false)
    private Usuario usuarioCreacion;

    @Column(name = "usuario_creacion_id")
    private Long usuarioCreacionId;

    @Enumerated(EnumType.STRING)
    private EstadoContrato estado;

}
