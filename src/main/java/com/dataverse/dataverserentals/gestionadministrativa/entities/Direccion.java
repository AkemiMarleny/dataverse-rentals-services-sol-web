package com.dataverse.dataverserentals.gestionadministrativa.entities;

import java.time.LocalDateTime;

import com.dataverse.dataverserentals.common.helpers.Alphanumeric;
import com.dataverse.dataverserentals.common.helpers.IdentificatoreIndirizzo;
import com.dataverse.dataverserentals.common.helpers.NombreCapitalizar;
import com.dataverse.dataverserentals.common.helpers.Normalizar;
import com.dataverse.dataverserentals.common.helpers.ReferenzaIndirizzo;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateBuildingAddress;

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
@Table(name = "direcciones")
@Getter
@Setter
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "toponimo_id", insertable = false, updatable = false)
    private Toponimo toponimo;

    @Column(name = "toponimo_id", nullable = false)
    private Long toponimoId;

    private String nombre;

    private String identificador;

    private String referencia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "edificio_id", insertable = false, updatable = false)
    private Edificio edificio;

    @Column(name = "edificio_id", nullable = false)
    private Long edificioId;

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

    public static Direccion from(CreateBuildingAddress request) {
        Direccion direccion = new Direccion();
        direccion.setToponimoId(request.toponimoId());
        String nombreNormalizado = new NombreCapitalizar(new Normalizar(request.nombre()).get()).get();
        direccion.setNombre(nombreNormalizado);
        String identificadorNormalizado = new IdentificatoreIndirizzo(new Alphanumeric(request.identificador()).get())
                .get();
        direccion.setIdentificador(identificadorNormalizado);
        String referenciaNormalizado = new ReferenzaIndirizzo(new Alphanumeric(request.referencia()).get()).get();
        direccion.setReferencia(referenciaNormalizado);
        return direccion;
    }
}
