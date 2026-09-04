package com.dataverse.dataverserentals.gestionadministrativa.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateUnidadDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateUnidadDto;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoAcabado;
import com.dataverse.dataverserentals.gestionfinanciera.valueobjects.CoeficienteUnidad;
import com.dataverse.dataverserentals.gestionfinanciera.valueobjects.MontoProrrateado;

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
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "unidades", uniqueConstraints = {
        @UniqueConstraint(name = "uk_units_edificio_puerta", columnNames = { "edificio_id", "numero_puerta" })
})
@Getter
@Setter
@Slf4j
public class Unidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "edificio_id", insertable = false, updatable = false)
    private Edificio edificio;

    @Column(name = "edificio_id", nullable = false)
    private Long edificioId;

    @Column(nullable = false)
    private Integer numeroPiso;

    @Column(length = 10, nullable = false)
    private String numeroPuerta;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal areaMetrosCuadrados;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoAcabado tipoAcabado;

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

    public static Unidad from(CreateUnidadDto request, Long usuarioCreacionId) {
        Unidad unidad = new Unidad();
        unidad.setEdificioId(request.edificioId());
        unidad.setNumeroPiso(request.numeroPiso());
        unidad.setNumeroPuerta(request.numeroPuerta());
        unidad.setAreaMetrosCuadrados(request.areaMetrosCuadrados());
        unidad.setTipoAcabado(request.tipoAcabado());
        unidad.setFechaCreacion(LocalDateTime.now());
        unidad.setUsuarioCreacionId(usuarioCreacionId);
        unidad.setFechaModificacion(LocalDateTime.now());
        unidad.setUsuarioModificacionId(usuarioCreacionId);
        return unidad;
    }

    public void updateWith(UpdateUnidadDto request, Long usuarioModificacionId) {
        this.edificioId = request.edificioId();
        this.numeroPiso = request.numeroPiso();
        this.numeroPuerta = request.numeroPuerta();
        this.areaMetrosCuadrados = request.areaMetrosCuadrados();
        this.tipoAcabado = request.tipoAcabado();
        this.fechaModificacion = LocalDateTime.now();
        this.usuarioModificacionId = usuarioModificacionId;
    }

    public MontoProrrateado prorratearMonto(
            BigDecimal areaArrendableEdificio,
            BigDecimal montoAProrratear) {

        BigDecimal areaMetrosCuadrados = this.getAreaMetrosCuadrados();

        log.info(
                "prorrateando monto [unidadId={}] [areaArrendableEdificio={}] [areaMetrosCuadrados={}] [montoAProrratear={}]",
                this.getId(),
                areaArrendableEdificio,
                areaMetrosCuadrados,
                montoAProrratear);

        CoeficienteUnidad coeficienteUnidad = CoeficienteUnidad.from(
                areaArrendableEdificio,
                areaMetrosCuadrados);

        MontoProrrateado montoGastosComunes = MontoProrrateado.from(
                coeficienteUnidad,
                montoAProrratear);

        return montoGastosComunes;
    }

}
