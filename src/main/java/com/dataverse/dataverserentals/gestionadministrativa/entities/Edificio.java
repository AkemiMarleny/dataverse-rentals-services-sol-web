package com.dataverse.dataverserentals.gestionadministrativa.entities;

import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreateEdificioDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdateEdificioDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "buildings")
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

    public static Edificio from(CreateEdificioDto request) {
        Edificio edificio = new Edificio();
        edificio.setNombre(request.nombre());
        edificio.setDireccion(request.direccion());
        edificio.setNumeroPisos(request.numeroPisos());
        return edificio;
    }

    public void updateWith(UpdateEdificioDto request) {
        this.nombre = request.nombre();
        this.direccion = request.direccion();
        this.numeroPisos = request.numeroPisos();
    }

}
