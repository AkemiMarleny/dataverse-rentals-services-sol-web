package com.dataverse.dataverserentals.gestionadministrativa.entities;

import java.time.LocalDate;

import com.dataverse.dataverserentals.common.helpers.NombreCapitalizar;
import com.dataverse.dataverserentals.common.helpers.Normalizar;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.enums.EstadoCivil;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;
import com.dataverse.dataverserentals.gestionadministrativa.valueobjects.PersonaJuridicaChecksum;
import com.dataverse.dataverserentals.gestionadministrativa.valueobjects.PersonaNaturalChecksum;
import com.dataverse.dataverserentals.gestionadministrativa.valueobjects.PersonaNaturalNegocioChecksum;

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
@Table(name = "personas", uniqueConstraints = {
        @UniqueConstraint(name = "uk_people_num_doc", columnNames = { "tipoDocumentoCode", "numeroDocumento" }) })
@Getter
@Setter
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPersona tipoPersonaCode;

    @Column(length = 50)
    private String nombres;

    @Column(length = 50)
    private String apellidos;

    @Enumerated(EnumType.STRING)
    private Genero generoCode;

    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivilCode;

    private LocalDate fechaNacimiento;

    @Column(length = 150)
    private String razonSocial;

    @Column(length = 150)
    private String nombreComercial;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumentoCode;

    @Column(length = 20, nullable = false)
    private String numeroDocumento;

    @Column(length = 15, unique = true)
    private String numeroCelular;

    @Column(length = 100, unique = true)
    private String correo;

    @Column(length = 64)
    private String personaNaturalChecksum;

    @Column(length = 64)
    private String personaNaturalNegocioChecksum;

    @Column(length = 64)
    private String personaJuridicaChecksum;

    public static Persona from(CreatePersonDto request) {
        Persona persona = new Persona();
        persona.setTipoPersonaCode(request.tipoPersonaCode());
        persona.setTipoDocumentoCode(request.tipoDocumentoCode());
        persona.setNumeroDocumento(request.numeroDocumento());
        persona.setNumeroCelular(request.numeroCelular());
        persona.setCorreo(request.correo());

        if (request.tipoPersonaCode() == TipoPersona.NATURAL_SIN_NEGOCIO) {
            String nombresNormalizado = new NombreCapitalizar(new Normalizar(request.nombres()).get()).get();
            persona.setNombres(nombresNormalizado);
            String apellidosNormalizado = new NombreCapitalizar(new Normalizar(request.apellidos()).get()).get();
            persona.setApellidos(apellidosNormalizado);
            persona.setGeneroCode(request.generoCode());
            persona.setEstadoCivilCode(request.estadoCivilCode());
            persona.setFechaNacimiento(request.fechaNacimiento());
            persona.setPersonaNaturalChecksum(PersonaNaturalChecksum.fromRaw(nombresNormalizado, apellidosNormalizado,
                    request.tipoDocumentoCode(), request.numeroDocumento()).get());
        }

        else if (request.tipoPersonaCode() == TipoPersona.NATURAL_CON_NEGOCIO) {
            String nombresNormalizado = new NombreCapitalizar(new Normalizar(request.nombres()).get()).get();
            persona.setNombres(nombresNormalizado);
            String apellidosNormalizado = new NombreCapitalizar(new Normalizar(request.apellidos()).get()).get();
            persona.setApellidos(apellidosNormalizado);
            String nombreComercialNormalizado = new NombreCapitalizar(new Normalizar(request.nombreComercial()).get())
                    .get();
            persona.setNombreComercial(nombreComercialNormalizado);
            persona.setGeneroCode(request.generoCode());
            persona.setEstadoCivilCode(request.estadoCivilCode());
            persona.setFechaNacimiento(request.fechaNacimiento());
            persona.setPersonaNaturalNegocioChecksum(PersonaNaturalNegocioChecksum
                    .fromRaw(nombresNormalizado, apellidosNormalizado, nombreComercialNormalizado,
                            request.tipoDocumentoCode(), request.numeroDocumento())
                    .get());
        }

        else {
            String razonSocialNormalizado = new NombreCapitalizar(new Normalizar(request.razonSocial()).get()).get();
            persona.setRazonSocial(razonSocialNormalizado);
            persona.setPersonaJuridicaChecksum(
                    PersonaJuridicaChecksum
                            .fromRaw(razonSocialNormalizado, request.tipoDocumentoCode(), request.numeroDocumento())
                            .get());
        }

        return persona;
    }

    public void updateWith(UpdatePersonDto request) {
        this.tipoPersonaCode = request.tipoPersonaCode();
        this.tipoDocumentoCode = request.tipoDocumentoCode();
        this.numeroDocumento = request.numeroDocumento();
        this.numeroCelular = request.numeroCelular();
        this.correo = request.correo();

        if (request.tipoPersonaCode() == TipoPersona.NATURAL_SIN_NEGOCIO) {
            String nombresNormalizado = new NombreCapitalizar(new Normalizar(request.nombres()).get()).get();
            this.nombres = nombresNormalizado;
            String apellidosNormalizado = new NombreCapitalizar(new Normalizar(request.apellidos()).get()).get();
            this.apellidos = apellidosNormalizado;
            this.generoCode = request.generoCode();
            this.estadoCivilCode = request.estadoCivilCode();
            this.fechaNacimiento = request.fechaNacimiento();
            this.personaNaturalChecksum = PersonaNaturalChecksum.fromRaw(
                    nombresNormalizado, apellidosNormalizado,
                    request.tipoDocumentoCode(), request.numeroDocumento()).get();
        }

        else if (request.tipoPersonaCode() == TipoPersona.NATURAL_CON_NEGOCIO) {
            String nombresNormalizado = new NombreCapitalizar(new Normalizar(request.nombres()).get()).get();
            this.nombres = nombresNormalizado;
            String apellidosNormalizado = new NombreCapitalizar(new Normalizar(request.apellidos()).get()).get();
            this.apellidos = apellidosNormalizado;
            String nombreComercialNormalizado = new NombreCapitalizar(new Normalizar(request.nombreComercial()).get())
                    .get();
            this.nombreComercial = nombreComercialNormalizado;
            this.generoCode = request.generoCode();
            this.estadoCivilCode = request.estadoCivilCode();
            this.fechaNacimiento = request.fechaNacimiento();
            this.personaNaturalNegocioChecksum = PersonaNaturalNegocioChecksum.fromRaw(
                    nombresNormalizado, apellidosNormalizado, nombreComercialNormalizado,
                    request.tipoDocumentoCode(), request.numeroDocumento()).get();
        }

        else {
            String razonSocialNormalizado = new NombreCapitalizar(new Normalizar(request.razonSocial()).get()).get();
            this.razonSocial = razonSocialNormalizado;
            this.personaJuridicaChecksum = PersonaJuridicaChecksum.fromRaw(
                    razonSocialNormalizado, request.tipoDocumentoCode(), request.numeroDocumento()).get();
        }
    }

    public String getChecksum() {
        return switch (tipoPersonaCode) {
            case NATURAL_SIN_NEGOCIO -> personaNaturalChecksum;
            case NATURAL_CON_NEGOCIO -> personaNaturalNegocioChecksum;
            case JURIDICA -> personaJuridicaChecksum;
        };
    }
}