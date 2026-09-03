package com.dataverse.dataverserentals.gestionadministrativa.validators;

import org.springframework.stereotype.Component;

import com.dataverse.dataverserentals.common.exceptions.FieldConstraintException;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.CreatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.request.UpdatePersonDto;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoPersona;

@Component
public class PersonaValidator {

    public void validarConsistenciaDatos(CreatePersonDto dto) throws FieldConstraintException {
        validarCampos(dto.tipoPersonaCode(), dto.nombres(), dto.apellidos(),
                dto.nombreComercial(), dto.razonSocial());
    }

    public void validarConsistenciaDatos(UpdatePersonDto dto) throws FieldConstraintException {
        validarCampos(dto.tipoPersonaCode(), dto.nombres(), dto.apellidos(),
                dto.nombreComercial(), dto.razonSocial());
    }

    private void validarCampos(TipoPersona tipoPersonaCode, String nombres, String apellidos,
            String nombreComercial, String razonSocial) throws FieldConstraintException {

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
            if (nombres == null || nombres.isBlank()) {
                throw new FieldConstraintException("nombres",
                        "Los nombres son obligatorios para personas naturales con negocio.");
            }
            if (nombres.length() < 3) {
                throw new FieldConstraintException("nombres",
                        "Los nombres deben tener al menos 3 caracteres.");
            }
            if (apellidos == null || apellidos.isBlank()) {
                throw new FieldConstraintException("apellidos",
                        "Los apellidos son obligatorios para personas naturales con negocio.");
            }
            if (apellidos.length() < 3) {
                throw new FieldConstraintException("apellidos",
                        "Los apellidos deben tener al menos 3 caracteres.");
            }
            if (nombreComercial == null || nombreComercial.isBlank()) {
                throw new FieldConstraintException("nombreComercial",
                        "El nombre comercial es obligatorio para personas naturales con negocio.");
            }
            if (nombreComercial.length() < 3) {
                throw new FieldConstraintException("nombreComercial",
                        "El nombre comercial debe tener al menos 3 caracteres.");
            }
        }

        else {
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
        }
    }
}