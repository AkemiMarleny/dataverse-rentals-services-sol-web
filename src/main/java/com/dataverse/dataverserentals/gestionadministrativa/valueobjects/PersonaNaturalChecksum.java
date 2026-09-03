package com.dataverse.dataverserentals.gestionadministrativa.valueobjects;

import com.dataverse.dataverserentals.common.helpers.Checksum;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;

public class PersonaNaturalChecksum {
    private final String value;

    private PersonaNaturalChecksum(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    public static PersonaNaturalChecksum fromRaw(
            String nombres,
            String apellidos,
            TipoDocumento tipoDocumentoCode,
            String numeroDocumento) {

        String contentCodificated = String.format("%s.%s.%s.%s",
                nombres.trim().toLowerCase(),
                apellidos.trim().toLowerCase(),
                tipoDocumentoCode,
                numeroDocumento.trim().toLowerCase());

        return new PersonaNaturalChecksum(new Checksum(contentCodificated).get());
    }
}