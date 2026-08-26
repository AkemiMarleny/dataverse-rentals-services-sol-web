package com.dataverse.dataverserentals.administrations.valueobjects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.dataverse.dataverserentals.administrations.enums.TipoDocumento;
import com.dataverse.dataverserentals.common.helpers.Checksum;

public class PersonaChecksum {
    private final String contentCodificated;

    private PersonaChecksum(String content) {
        this.contentCodificated = content;
    }

    public String get() {
        return contentCodificated;
    }

    public static PersonaChecksum fromRaw(String nombres, String apellidos, TipoDocumento tipoDocumentoCode,
            String numeroDocumento, LocalDate fechaNacimiento) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaNacimientoFormatted = fechaNacimiento.format(formatter);

        String contentCodificated = String.format("%s.%s.%s.%s.%s",
                nombres.trim().toLowerCase(),
                apellidos.trim().toLowerCase(),
                tipoDocumentoCode,
                numeroDocumento.trim().toLowerCase(),
                fechaNacimientoFormatted);

        return new PersonaChecksum(new Checksum(contentCodificated).get());
    }

}
