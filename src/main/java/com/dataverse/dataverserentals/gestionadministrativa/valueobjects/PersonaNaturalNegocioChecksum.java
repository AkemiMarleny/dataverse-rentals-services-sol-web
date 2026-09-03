package com.dataverse.dataverserentals.gestionadministrativa.valueobjects;

import com.dataverse.dataverserentals.common.helpers.Checksum;
import com.dataverse.dataverserentals.gestionadministrativa.enums.TipoDocumento;

public class PersonaNaturalNegocioChecksum {
    private final String value;

    private PersonaNaturalNegocioChecksum(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    public static PersonaNaturalNegocioChecksum fromRaw(
            String nombres,
            String apellidos,
            String nombreComercial,
            TipoDocumento tipoDocumentoCode,
            String numeroDocumento) {

        String contentCodificated = String.format("%s.%s.%s.%s.%s",
                nombres.trim().toLowerCase(),
                apellidos.trim().toLowerCase(),
                nombreComercial.trim().toLowerCase(),
                tipoDocumentoCode,
                numeroDocumento.trim().toLowerCase());

        return new PersonaNaturalNegocioChecksum(new Checksum(contentCodificated).get());
    }
}