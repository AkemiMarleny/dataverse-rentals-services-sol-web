package com.dataverse.dataverserentals.gestionoperativa.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoConceptoPropuesta;

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

@Entity
@Table(name = "detalle_propuestas_liquidacion")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetallePropuestaLiquidacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "propuesta_liquidacion_id", nullable = false)
    private Long propuestaLiquidacionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "propuesta_liquidacion_id", insertable = false, updatable = false)
    private PropuestaLiquidacion propuestaLiquidacion;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConceptoPropuesta tipoConceptoPropuesta;

    @Column(nullable = true)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_creacion_id", insertable = false, updatable = false)
    private Usuario usuarioCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;
}
