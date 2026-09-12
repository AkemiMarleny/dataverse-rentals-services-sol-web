package com.dataverse.dataverserentals.gestionadministrativa.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dataverse.dataverserentals.common.helpers.NombreCapitalizar;
import com.dataverse.dataverserentals.common.helpers.Normalizar;
import com.dataverse.dataverserentals.common.helpers.TiposEmpresaJoiner;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdatePersonDto;
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
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "personas", indexes = {
        @Index(name = "idx_personas_usuario_creacion", columnList = "usuarioCreacionId"),
        @Index(name = "idx_personas_usuario_modificacion", columnList = "usuarioModificacionId"),
})
@Getter
@Setter
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPersona tipoPersonaCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumentoCode;

    @Column(length = 20, nullable = false)
    private String numeroDocumento;

    @Column(length = 20, nullable = false, unique = true)
    private String numeroCelular;

    @Column(length = 100, nullable = false, unique = true)
    private String correo;

    @Column(length = 100, nullable = true)
    private String nombres;

    @Column(length = 100, nullable = true)
    private String apellidos;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Genero generoCode;

    @Column(nullable = true)
    private LocalDate fechaNacimiento;

    @Column(length = 100, nullable = true)
    private String nombreComercial;

    @Column(length = 200, nullable = true)
    private String razonSocial;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "usuario_creacion_id", nullable = false)
    private Long usuarioCreacionId;

    @Column(nullable = false)
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_modificacion_id", nullable = false)
    private Long usuarioModificacionId;

    @Column(length = 64)
    private String personaNaturalChecksum;

    @Column(length = 64)
    private String personaNaturalNegocioChecksum;

    @Column(length = 64)
    private String personaJuridicaChecksum;

    public static Persona from(CreatePersonDto request, Long usuarioCreacionId) {
        Persona persona = new Persona();
        persona.setTipoPersonaCode(request.tipoPersonaCode());
        persona.setTipoDocumentoCode(request.tipoDocumentoCode());
        persona.setNumeroDocumento(request.numeroDocumento());
        persona.setNumeroCelular(request.numeroCelular());
        persona.setCorreo(request.correo());
        persona.setFechaCreacion(LocalDateTime.now());
        persona.setUsuarioCreacionId(usuarioCreacionId);
        persona.setFechaModificacion(LocalDateTime.now());
        persona.setUsuarioModificacionId(usuarioCreacionId);

        if (request.tipoPersonaCode() == TipoPersona.NATURAL_SIN_NEGOCIO
                || request.tipoPersonaCode() == TipoPersona.NATURAL_CON_NEGOCIO) {
            String nombresNormalizado = new NombreCapitalizar(new Normalizar(request.nombres()).get()).get();
            persona.setNombres(nombresNormalizado);
            String apellidosNormalizado = new NombreCapitalizar(new Normalizar(request.apellidos()).get()).get();
            persona.setApellidos(apellidosNormalizado);
            persona.setGeneroCode(request.generoCode());
            persona.setFechaNacimiento(request.fechaNacimiento());

            if (request.tipoPersonaCode() == TipoPersona.NATURAL_SIN_NEGOCIO) {
                persona.setPersonaNaturalChecksum(
                        PersonaNaturalChecksum.fromRaw(nombresNormalizado, apellidosNormalizado,
                                request.tipoDocumentoCode(), request.numeroDocumento()).get());
            } else {
                String nombreComercialNormalizado = new NombreCapitalizar(
                        new Normalizar(request.nombreComercial()).get())
                        .get();
                persona.setNombreComercial(nombreComercialNormalizado);
                persona.setPersonaNaturalNegocioChecksum(PersonaNaturalNegocioChecksum
                        .fromRaw(nombresNormalizado, apellidosNormalizado, nombreComercialNormalizado,
                                request.tipoDocumentoCode(), request.numeroDocumento())
                        .get());
            }
        }

        else {
            String razonSocialNormalizado = new NombreCapitalizar(
                    new TiposEmpresaJoiner(new Normalizar(request.razonSocial()).get()).get()).get();
            persona.setRazonSocial(razonSocialNormalizado);
            persona.setPersonaJuridicaChecksum(
                    PersonaJuridicaChecksum
                            .fromRaw(razonSocialNormalizado, request.tipoDocumentoCode(), request.numeroDocumento())
                            .get());
        }

        return persona;
    }

    public void updateWith(UpdatePersonDto request, Long usuarioModificacionId) {
        this.tipoPersonaCode = request.tipoPersonaCode();
        this.tipoDocumentoCode = request.tipoDocumentoCode();
        this.numeroDocumento = request.numeroDocumento();
        this.numeroCelular = request.numeroCelular();
        this.correo = request.correo();
        this.fechaModificacion = LocalDateTime.now();
        this.usuarioModificacionId = usuarioModificacionId;

        if (request.tipoPersonaCode() == TipoPersona.NATURAL_SIN_NEGOCIO
                || request.tipoPersonaCode() == TipoPersona.NATURAL_CON_NEGOCIO) {
            String nombresNormalizado = new NombreCapitalizar(new Normalizar(request.nombres()).get()).get();
            this.nombres = nombresNormalizado;
            String apellidosNormalizado = new NombreCapitalizar(new Normalizar(request.apellidos()).get()).get();
            this.apellidos = apellidosNormalizado;
            this.generoCode = request.generoCode();
            this.fechaNacimiento = request.fechaNacimiento();

            if (request.tipoPersonaCode() == TipoPersona.NATURAL_SIN_NEGOCIO) {
                this.personaNaturalChecksum = PersonaNaturalChecksum.fromRaw(
                        nombresNormalizado, apellidosNormalizado,
                        request.tipoDocumentoCode(), request.numeroDocumento()).get();
            } else {
                String nombreComercialNormalizado = new NombreCapitalizar(
                        new Normalizar(request.nombreComercial()).get())
                        .get();
                this.nombreComercial = nombreComercialNormalizado;
                this.personaNaturalNegocioChecksum = PersonaNaturalNegocioChecksum.fromRaw(
                        nombresNormalizado, apellidosNormalizado, nombreComercialNormalizado,
                        request.tipoDocumentoCode(), request.numeroDocumento()).get();
            }
        }

        else {
            String razonSocialNormalizado = new NombreCapitalizar(
                    new TiposEmpresaJoiner(new Normalizar(request.razonSocial()).get()).get()).get();
            this.razonSocial = razonSocialNormalizado;
            this.personaJuridicaChecksum = PersonaJuridicaChecksum.fromRaw(
                    razonSocialNormalizado, request.tipoDocumentoCode(), request.numeroDocumento()).get();
        }
    }

    public String getDenominacion() {
        return switch (this.getTipoPersonaCode()) {
            case NATURAL_SIN_NEGOCIO, NATURAL_CON_NEGOCIO ->
                String.format("%s %s", this.getNombres(), this.getApellidos());
            case JURIDICA -> this.getRazonSocial();
        };
    }
}