package com.dataverse.dataverserentals.administrations.entities;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateMedidorDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.request.UpdateMedidorDto;
import com.dataverse.dataverserentals.administrations.enums.TipoMedidor;

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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "meters", uniqueConstraints = {
        @UniqueConstraint(name = "uk_unidad_tipo_medidor", columnNames = { "unidad_id", "tipo_medidor_code" })
})
@Getter
@Setter
public class Medidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unidad_id", insertable = false, updatable = false)
    private Unidad unidad;

    @Column(name = "unidad_id", nullable = false)
    private Long unidadId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMedidor tipoMedidorCode;

    @Column(nullable = false, unique = true, length = 20)
    private String numeroSerie;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_creacion_id", insertable = false, updatable = false)
    private Persona usuarioCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    @Column(nullable = true)
    private LocalDateTime fechaModificacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_modificacion_id", insertable = false, updatable = false)
    private Persona usuarioModificacion;

    @Column(name = "usuario_modificacion_id", nullable = true)
    private Long usuarioModificacionId;

    public static Medidor from(CreateMedidorDto request, Long unidadId) {
        Medidor medidor = new Medidor();
        medidor.setUnidadId(unidadId);
        medidor.setTipoMedidorCode(request.tipoMedidorCode());
        medidor.setNumeroSerie(request.numeroSerie());
        medidor.setFechaCreacion(request.fechaCreacion());
        medidor.setUsuarioCreacionId(request.usuarioCreacionId());
        medidor.setFechaModificacion(request.fechaModificacion());
        medidor.setUsuarioModificacionId(request.usuarioModificacionId());
        return medidor;
    }

    public void updateWith(UpdateMedidorDto request) {
        this.tipoMedidorCode = request.tipoMedidorCode();
        this.numeroSerie = request.numeroSerie();
        this.fechaCreacion = request.fechaCreacion();
        this.usuarioCreacionId = request.usuarioCreacionId();
        this.fechaModificacion = request.fechaModificacion();
        this.usuarioModificacionId = request.usuarioModificacionId();
    }

}
