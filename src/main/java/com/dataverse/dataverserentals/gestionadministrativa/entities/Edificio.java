package com.dataverse.dataverserentals.gestionadministrativa.entities;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.common.helpers.NombreCapitalizar;
import com.dataverse.dataverserentals.common.helpers.Normalizar;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateEdificioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateEdificioDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "edificios")
@Getter
@Setter
public class Edificio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String nombre;

    @Column(length = 200, nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Integer numeroPisos;

    @Column(nullable = false)
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

    public static Edificio from(CreateEdificioDto request, Long usuarioCreacionId) {
        Edificio edificio = new Edificio();
        String nombreNormalizato = new NombreCapitalizar(new Normalizar(request.nombre()).get()).get();
        edificio.setNombre(nombreNormalizato);
        edificio.setDireccion(request.direccion());
        edificio.setNumeroPisos(request.numeroPisos());
        edificio.setFechaCreacion(LocalDateTime.now());
        edificio.setUsuarioCreacionId(usuarioCreacionId);
        edificio.setFechaModificacion(LocalDateTime.now());
        edificio.setUsuarioModificacionId(usuarioCreacionId);
        return edificio;
    }

    public void updateWith(UpdateEdificioDto request, Long usuarioModificacionId) {
        String nombreNormalizato = new NombreCapitalizar(new Normalizar(request.nombre()).get()).get();
        this.nombre = nombreNormalizato;
        this.direccion = request.direccion();
        this.numeroPisos = request.numeroPisos();
        this.fechaModificacion = LocalDateTime.now();
        this.usuarioModificacionId = usuarioModificacionId;
    }

}
