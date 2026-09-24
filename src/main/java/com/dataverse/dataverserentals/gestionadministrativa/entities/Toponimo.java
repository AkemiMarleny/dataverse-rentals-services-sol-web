package com.dataverse.dataverserentals.gestionadministrativa.entities;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.common.helpers.CapitalizeWord;
import com.dataverse.dataverserentals.common.helpers.Normalizar;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateToponimoDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateToponimoDto;

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
@Table(name = "toponimos")
@Getter
@Setter
public class Toponimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_creacion_id", insertable = false, updatable = false)
    private Usuario usuarioCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    @Column(nullable = false)
    private LocalDateTime fechaModificacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_modificacion_id", insertable = false, updatable = false)
    private Usuario usuarioModificacion;

    @Column(name = "usuario_modificacion_id", nullable = false)
    private Long usuarioModificacionId;

    public static Toponimo from(CreateToponimoDto request, Long usuarioId) {
        Toponimo toponimo = new Toponimo();
        String nombreNormalizado = new CapitalizeWord(new Normalizar(request.nombre()).get()).get();
        toponimo.setNombre(nombreNormalizado);
        toponimo.setFechaCreacion(LocalDateTime.now());
        toponimo.setUsuarioCreacionId(usuarioId);
        toponimo.setFechaModificacion(LocalDateTime.now());
        toponimo.setUsuarioModificacionId(usuarioId);
        return toponimo;
    }

    public void updateWith(UpdateToponimoDto request, Long usuarioId) {
        String nombreNormalizado = new CapitalizeWord(new Normalizar(request.nombre()).get()).get();
        this.nombre = nombreNormalizado;
        this.fechaModificacion = LocalDateTime.now();
        this.usuarioModificacionId = usuarioId;
    }
}
