package com.dataverse.dataverserentals.gestionadministrativa.entities;

import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonaJuridicaDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdatePersonaJuridicaDto;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;
import com.dataverse.dataverserentals.gestionadministrativa.valueobjects.PersonaJuridicaChecksum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "personas_juridicas")
@Getter
@Setter
public class PersonaJuridica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personaJuridicaId;

    private String razonSocial;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumentoCode;

    private String numeroDocumento;

    private String correo;

    private String telefono;

    private String checksum;

    public static PersonaJuridica from(CreatePersonaJuridicaDto request) {
        PersonaJuridica personaJuridica = new PersonaJuridica();
        personaJuridica.setRazonSocial(request.razonSocial());
        personaJuridica.setTipoDocumentoCode(request.tipoDocumentoCode());
        personaJuridica.setNumeroDocumento(request.numeroDocumento());
        personaJuridica.setCorreo(request.correo());
        personaJuridica.setTelefono(request.telefono());
        personaJuridica.setChecksum(PersonaJuridicaChecksum.fromRaw(
                request.razonSocial(),
                request.tipoDocumentoCode(),
                request.numeroDocumento()).get());
        return personaJuridica;
    }

    public void updateWith(UpdatePersonaJuridicaDto request) {
        this.razonSocial = request.razonSocial();
        this.tipoDocumentoCode = request.tipoDocumentoCode();
        this.numeroDocumento = request.numeroDocumento();
        this.correo = request.correo();
        this.telefono = request.telefono();
        this.checksum = PersonaJuridicaChecksum.fromRaw(
                this.razonSocial,
                this.tipoDocumentoCode,
                this.numeroDocumento).get();
    }
}