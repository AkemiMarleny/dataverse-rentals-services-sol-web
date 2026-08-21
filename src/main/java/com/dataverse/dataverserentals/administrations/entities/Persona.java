package com.dataverse.dataverserentals.administrations.entities;

import java.time.LocalDate;

import com.dataverse.dataverserentals.administrations.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.administrations.controllers.dtos.request.UpdatePersonDto;
import com.dataverse.dataverserentals.administrations.enums.EstadoCivil;
import com.dataverse.dataverserentals.administrations.enums.Genero;
import com.dataverse.dataverserentals.administrations.enums.TipoDocumento;
import com.dataverse.dataverserentals.administrations.exceptions.FieldConstraintException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "people", uniqueConstraints = {
        @UniqueConstraint(name = "uk_people_num_doc", columnNames = { "tipoDocumentoCode", "numeroDocumento" }) })
@Getter
@Setter
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nombres;

    @Column(length = 50, nullable = false)
    private String apellidos;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumentoCode;

    @Column(length = 20, nullable = false)
    private String numeroDocumento;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genero generoCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivilCode;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false, length = 15)
    private String numeroCelular;

    @Column(nullable = false, length = 100, unique = true)
    private String correo;

    public static Persona from(CreatePersonDto request) throws FieldConstraintException {
        Persona persona = new Persona();
        persona.setNombres(request.nombres());
        persona.setApellidos(request.apellidos());
        persona.setTipoDocumentoCode(request.tipoDocumentoCode());
        persona.setNumeroDocumento(request.numeroDocumento());
        persona.setGeneroCode(request.generoCode());
        persona.setEstadoCivilCode(request.estadoCivilCode());
        persona.setFechaNacimiento(request.fechaNacimiento());
        persona.setNumeroCelular(request.numero_celular());
        persona.setCorreo(request.correo());
        return persona;
    }

    public void updateWith(UpdatePersonDto request) {
        this.nombres = request.nombres();
        this.apellidos = request.apellidos();
        this.tipoDocumentoCode = request.tipoDocumentoCode();
        this.numeroDocumento = request.numeroDocumento();
        this.generoCode = request.generoCode();
        this.estadoCivilCode = request.estadoCivilCode();
        this.fechaNacimiento = request.fechaNacimiento();
        this.numeroCelular = request.numero_celular();
        this.correo = request.correo();
    }

}
