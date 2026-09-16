package com.dataverse.dataverserentals.gestionadministrativa.enums;

public enum EstadoUsuario {
    ACTIVO,
    INACTIVO,
    BLOQUEADO;

    public String intoString() {
        return switch (this) {
            case ACTIVO -> "Activo";
            case INACTIVO -> "Inactivo";
            case BLOQUEADO -> "Bloqueado";
        };
    }
}
