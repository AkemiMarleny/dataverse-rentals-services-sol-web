package com.dataverse.dataverserentals.gestioncampo.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.entities.Medidor;
import com.dataverse.dataverserentals.gestionadministrativa.entities.Persona;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.CreateLecturaHistoricaDto;
import com.dataverse.dataverserentals.gestioncampo.controllers.dtos.request.UpdateLecturaHistoricaDto;

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
@Table(name = "lecturas_historicas")
@Getter
@Setter
public class LecturaHistorica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medidor_id", insertable = false, updatable = false)
    private Medidor medidor;

    @Column(name = "medidor_id", nullable = false)
    private Long medidorId;

    @Column(name = "fecha_lectura", nullable = false)
    private LocalDateTime fechaLectura;

    @Column(name = "valor_lectura", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorLectura;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_creacion_id", insertable = false, updatable = false)
    private Persona usuarioCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    @Column(nullable = false)
    private LocalDateTime fechaModificacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_modificacion_id", insertable = false, updatable = false)
    private Persona usuarioModificacion;

    @Column(name = "usuario_modificacion_id", nullable = false)
    private Long usuarioModificacionId;

    public static LecturaHistorica from(Long medidorId, CreateLecturaHistoricaDto request, Long usuarioCreacionId) {
        LecturaHistorica lecturaHistorica = new LecturaHistorica();
        lecturaHistorica.setMedidorId(medidorId);
        lecturaHistorica.setFechaLectura(LocalDateTime.now());
        lecturaHistorica.setValorLectura(request.valorLectura());
        lecturaHistorica.setFechaCreacion(LocalDateTime.now());
        lecturaHistorica.setUsuarioCreacionId(usuarioCreacionId);
        lecturaHistorica.setFechaModificacion(LocalDateTime.now());
        lecturaHistorica.setUsuarioModificacionId(usuarioCreacionId);

        return lecturaHistorica;
    }

    public void updateWith(UpdateLecturaHistoricaDto request, Long usuarioModificacionId) {
        this.fechaLectura = LocalDateTime.now();
        this.valorLectura = request.valorLectura();
        this.fechaModificacion = LocalDateTime.now();
        this.usuarioModificacionId = usuarioModificacionId;

    }
}
