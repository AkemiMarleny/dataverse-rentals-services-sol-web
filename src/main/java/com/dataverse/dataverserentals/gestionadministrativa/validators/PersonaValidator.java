package com.dataverse.dataverserentals.gestionadministrativa.validators;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.enums.Genero;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

@Component
public class PersonaValidator {

    public void validarConsistenciaDatos(CreatePersonDto dto) throws FieldConstraintException {
        validarCampos(dto.tipoPersonaCode(), dto.nombres(), dto.apellidos(),
                dto.nombreComercial(), dto.razonSocial(), dto.generoCode(),
                dto.fechaNacimiento());
    }

    public void validarConsistenciaDatos(UpdatePersonDto dto) throws FieldConstraintException {
        validarCampos(dto.tipoPersonaCode(), dto.nombres(), dto.apellidos(),
                dto.nombreComercial(), dto.razonSocial(), dto.generoCode(),
                dto.fechaNacimiento());
    }

    private void validarCampos(TipoPersona tipoPersonaCode, String nombres, String apellidos,
            String nombreComercial, String razonSocial, Genero generoCode,
            LocalDate fechaNacimiento) throws FieldConstraintException {

        if (tipoPersonaCode == TipoPersona.JURIDICA) {
            if (razonSocial == null || razonSocial.isBlank()) {
                throw new FieldConstraintException("razonSocial",
                        "La razón social es obligatoria para personas jurídicas.");
            }
            if (razonSocial.length() < 3) {
                throw new FieldConstraintException("razonSocial",
                        "La razón social debe tener al menos 3 caracteres.");
            }
        }

        else if (tipoPersonaCode == TipoPersona.NATURAL_CON_NEGOCIO) {

            validarDatosPersonaNatural(nombres, apellidos, generoCode, fechaNacimiento);

            if (nombreComercial == null || nombreComercial.isBlank()) {
                throw new FieldConstraintException("nombreComercial",
                        "El nombre comercial es obligatorio para este tipo de persona con negocio.");
            }
            if (nombreComercial.length() < 3) {
                throw new FieldConstraintException("nombreComercial",
                        "El nombre comercial debe tener al menos 3 caracteres.");
            }
        }

        else {
            validarDatosPersonaNatural(nombres, apellidos, generoCode, fechaNacimiento);
        }
    }

    private void validarDatosPersonaNatural(String nombres, String apellidos,
            Genero generoCode, LocalDate fechaNacimiento) throws FieldConstraintException {

        if (nombres == null || nombres.isBlank()) {
            throw new FieldConstraintException("nombres",
                    "Los nombres son obligatorios para personas naturales.");
        }
        if (nombres.length() < 3) {
            throw new FieldConstraintException("nombres",
                    "Los nombres deben tener al menos 3 caracteres.");
        }
        if (apellidos == null || apellidos.isBlank()) {
            throw new FieldConstraintException("apellidos",
                    "Los apellidos son obligatorios para personas naturales.");
        }
        if (apellidos.length() < 3) {
            throw new FieldConstraintException("apellidos",
                    "Los apellidos deben tener al menos 3 caracteres.");
        }
        if (generoCode == null) {
            throw new FieldConstraintException("generoCode",
                    "El género es obligatorio para este tipo de persona.");
        }
        if (fechaNacimiento == null) {
            throw new FieldConstraintException("fechaNacimiento",
                    "La fecha de nacimiento es obligatoria para este tipo de persona.");
        }
    }
}