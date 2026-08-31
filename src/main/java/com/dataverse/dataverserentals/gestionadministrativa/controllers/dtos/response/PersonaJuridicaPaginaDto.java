package com.dataverse.dataverserentals.gestionadministrativa.controllers.dtos.response;

import com.dataverse.dataverserentals.gestionadministrativa.entities.PersonaJuridica;

public record PersonaJuridicaPaginaDto(
        Long personaJuridicaId,
        String razonSocial,
        String numeroDocumento,
        String correo,
        String telefono) {

    public static PersonaJuridicaPaginaDto fromEntity(PersonaJuridica personaJuridica) {
        return new PersonaJuridicaPaginaDto(
                personaJuridica.getPersonaJuridicaId(),
                personaJuridica.getRazonSocial(),
                personaJuridica.getNumeroDocumento(),
                personaJuridica.getCorreo(),
                personaJuridica.getTelefono());
    }
}