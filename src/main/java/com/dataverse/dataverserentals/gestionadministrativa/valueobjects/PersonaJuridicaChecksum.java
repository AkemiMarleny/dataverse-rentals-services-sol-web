package com.dataverse.dataverserentals.gestionadministrativa.valueobjects;

import com.dataverse.dataverserentals.common.helpers.Checksum;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;

public class PersonaJuridicaChecksum {

    private final String contentCodificated;

    private PersonaJuridicaChecksum(String content) {
        this.contentCodificated = content;
    }

    public String get() {
        return contentCodificated;
    }

    public static PersonaJuridicaChecksum fromRaw(
            String razonSocial,
            TipoDocumento tipoDocumentoCode,
            String numeroDocumento) {

        String contentCodificated = String.format("%s.%s.%s", 
            razonSocial.trim().toLowerCase(), 
            tipoDocumentoCode.name(),
            numeroDocumento.trim().toLowerCase());

        return new PersonaJuridicaChecksum(new Checksum(contentCodificated).get());
    }
}