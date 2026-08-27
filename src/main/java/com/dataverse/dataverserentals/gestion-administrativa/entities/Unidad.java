package com.dataverse.dataverserentals.administrations.entities;

import java.math.BigDecimal;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreateUnidadDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.request.UpdateUnidadDto;
import com.dataverse.dataverserentals.administrations.enums.TipoAcabado;

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
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "units", uniqueConstraints = {
        @UniqueConstraint(name = "uk_units_edificio_puerta", columnNames = { "edificio_id", "numero_puerta" })
})
@Getter
@Setter
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
    @Positive
    private BigDecimal areaMetrosCuadrados;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoAcabado tipoAcabado;

    public static Unidad from(CreateUnidadDto request) {
        Unidad unidad = new Unidad();
        unidad.setEdificioId(request.edificioId());
        unidad.setNumeroPiso(request.numeroPiso());
        unidad.setNumeroPuerta(request.numeroPuerta());
        unidad.setAreaMetrosCuadrados(request.areaMetrosCuadrados());
        unidad.setTipoAcabado(request.tipoAcabado());
        return unidad;
    }

    public void updateWith(UpdateUnidadDto request) {
        this.edificioId = request.edificioId();
        this.numeroPiso = request.numeroPiso();
        this.numeroPuerta = request.numeroPuerta();
        this.areaMetrosCuadrados = request.areaMetrosCuadrados();
        this.tipoAcabado = request.tipoAcabado();
    }

}
