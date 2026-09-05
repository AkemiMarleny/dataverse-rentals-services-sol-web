package com.dataverse.dataverserentals.gestionfinanciera.valueobjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CodigoRecibo {
    private final String codigo;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMdd-HHmmss");

    private CodigoRecibo(String codigo) {
        this.codigo = codigo;
    }

    public String intoString() {
        return this.codigo;
    }

    public static CodigoRecibo from(
            Long contratoId,
            LocalDateTime fecha) {
        String contrato = String.format("C%03d", contratoId);
        String fechaStr = fecha.format(FORMATTER);
        String codigo = String.format("%s-REC-%s", contrato, fechaStr);

        return new CodigoRecibo(codigo);
    }
}