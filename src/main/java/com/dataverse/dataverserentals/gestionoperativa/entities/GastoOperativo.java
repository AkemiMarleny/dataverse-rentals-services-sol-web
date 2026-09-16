package com.dataverse.dataverserentals.gestionoperativa.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Edificio;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateGastoOperativoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.UpdateGastoOperativoDto;
import com.dataverse.dataverserentals.gestionoperativa.enums.TipoGasto;

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
@Table(name = "gastos_operativos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GastoOperativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria_gasto_id", nullable = false)
    private Long categoriaGastoId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_gasto_id", insertable = false, updatable = false)
    private CategoriaGasto categoriaGasto;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoGasto tipoGastoCode;

    @Column(nullable = true)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDateTime fechaGasto;

    @Column(name = "edificio_id", nullable = false)
    private Long edificioId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "edificio_id", insertable = false, updatable = false)
    private Edificio edificio;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_creacion_id", insertable = false, updatable = false)
    private Usuario usuarioCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    @Column(nullable = true)
    private LocalDateTime fechaModificacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_modificacion_id", insertable = false, updatable = false)
    private Usuario usuarioModificacion;

    @Column(name = "usuario_modificacion_id", nullable = true)
    private Long usuarioModificacionId;

    public static GastoOperativo from(CreateGastoOperativoDto payload, Long usuarioCreacionId) {
        return GastoOperativo.builder()
                .edificioId(payload.edificioId())
                .categoriaGastoId(payload.categoriaGastoId())
                .tipoGastoCode(payload.tipoGastoCode())
                .descripcion(payload.descripcion())
                .monto(payload.monto())
                .fechaGasto(payload.fechaGasto())
                .fechaCreacion(LocalDateTime.now())
                .usuarioCreacionId(usuarioCreacionId)
                .fechaModificacion(LocalDateTime.now())
                .usuarioModificacionId(usuarioCreacionId)
                .build();
    }

    public void updateWith(UpdateGastoOperativoDto request, Long usuarioModificacionId) {
        this.edificioId = request.edificioId();
        this.categoriaGastoId = request.categoriaGastoId();
        this.tipoGastoCode = request.tipoGastoCode();
        this.descripcion = request.descripcion();
        this.monto = request.monto();
        this.fechaGasto = request.fechaGasto();
        this.fechaModificacion = LocalDateTime.now();
        this.usuarioModificacionId = usuarioModificacionId;
    }
}
