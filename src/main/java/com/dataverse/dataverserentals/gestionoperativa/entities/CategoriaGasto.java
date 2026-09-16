package com.dataverse.dataverserentals.gestionoperativa.entities;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Usuario;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.CreateCategoriaGastoDto;
import com.dataverse.dataverserentals.gestionoperativa.controllers.dtos.request.UpdateCategoriaGastoDto;

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

@Entity
@Table(name = "categorias_gasto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaGasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String nombre;

    @Column(nullable = true, length = 255)
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_creacion_id", insertable = false, updatable = false)
    private Usuario usuarioCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_modificacion_id", insertable = false, updatable = false)
    private Usuario usuarioModificacion;

    @Column(name = "usuario_modificacion_id", nullable = false)
    private Long usuarioModificacionId;

    public static CategoriaGasto from(CreateCategoriaGastoDto payload, Long usuarioId) {
        return CategoriaGasto.builder()
                .nombre(payload.nombre())
                .descripcion(payload.descripcion())
                .fechaCreacion(LocalDateTime.now())
                .usuarioCreacionId(usuarioId)
                .fechaModificacion(LocalDateTime.now())
                .usuarioModificacionId(usuarioId)
                .build();
    }

    public void updateWith(UpdateCategoriaGastoDto request, Long usuarioModificacionId) {
        this.nombre = request.nombre();
        this.descripcion = request.descripcion();
        this.fechaModificacion = LocalDateTime.now();
        this.usuarioModificacionId = usuarioModificacionId;
    }
}
