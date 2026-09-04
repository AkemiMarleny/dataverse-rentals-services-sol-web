package com.dataverse.dataverserentals.gestionoperativa.valueobjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CodigoPropuestaLiquidacion {
    private final String codigo;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMdd-HHmmss");

    private CodigoPropuestaLiquidacion(String codigo) {
        this.codigo = codigo;
    }

    public String intoString() {
        return this.codigo;
    }

    public static CodigoPropuestaLiquidacion from(
            Long edificioId,
            LocalDateTime fecha) {
        String edificio = String.format("ED%02d", edificioId);
        String fechaStr = fecha.format(FORMATTER);
        String codigo = String.format("PROP-%s-%s", edificio, fechaStr);

        return new CodigoPropuestaLiquidacion(codigo);
    }
}