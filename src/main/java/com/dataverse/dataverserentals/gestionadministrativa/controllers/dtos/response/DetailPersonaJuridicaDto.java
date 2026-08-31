package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionadministrativa.entities.PersonaJuridica;

public record DetailPersonaJuridicaDto(
        Long personaJuridicaId,
        String razonSocial,
        String tipoDocumentoCode,
        String numeroDocumento,
        String correo,
        String telefono) {

    public static DetailPersonaJuridicaDto fromEntity(PersonaJuridica personaJuridica) {
        return new DetailPersonaJuridicaDto(
                personaJuridica.getPersonaJuridicaId(),
                personaJuridica.getRazonSocial(),
                personaJuridica.getTipoDocumentoCode().name(),
                personaJuridica.getNumeroDocumento(),
                personaJuridica.getCorreo(),
                personaJuridica.getTelefono());
    }
}