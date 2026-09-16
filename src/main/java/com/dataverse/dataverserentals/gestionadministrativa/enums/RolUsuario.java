package com.dataverse.dataverserentals.gestionadministrativa.enums;

public enum RolUsuario {
    ADMINISTRADOR,
    ANALISTA_FINANZAS,
    OPERARIO,
    INQUILINO,
    SYSTEM;

    public String intoString() {
        return switch (this) {
            case ADMINISTRADOR -> "Administrador";
            case ANALISTA_FINANZAS -> "Analista de finanzas";
            case OPERARIO -> "Operario";
            case INQUILINO -> "Inquilino";
            case SYSTEM -> "Sistema";
        };
    }
}
