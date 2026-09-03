package com.dataverse.dataverserentals.gestionadministrativa.valueobjects;

import com.dataverse.dataverserentals.common.helpers.Checksum;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;

public class PersonaJuridicaChecksum {
    private final String value;

    private PersonaJuridicaChecksum(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    public static PersonaJuridicaChecksum fromRaw(
            String razonSocial,
            TipoDocumento tipoDocumentoCode,
            String numeroDocumento) {

        String contentCodificated = String.format("%s.%s.%s",
                razonSocial.trim().toLowerCase(),
                tipoDocumentoCode,
                numeroDocumento.trim().toLowerCase());

        return new PersonaJuridicaChecksum(new Checksum(contentCodificated).get());
    }
}