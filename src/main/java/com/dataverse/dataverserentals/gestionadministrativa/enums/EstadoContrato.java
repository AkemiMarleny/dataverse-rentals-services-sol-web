package com.dataverse.dataverserentals.gestionadministrativa.enums;

public enum EstadoContrato {
    VIGENTE,
    POR_VENCER,
    VENCIDO,
    CERRADO;

    public String intoString() {
        return switch (this) {
            case VIGENTE -> "Vigente";
            case POR_VENCER -> "Por vencer";
            case VENCIDO -> "Vencido";
            case CERRADO -> "Cerrado";
        };
    }
}
