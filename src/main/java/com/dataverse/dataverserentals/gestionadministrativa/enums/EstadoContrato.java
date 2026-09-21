package com.dataverse.dataverserentals.gestionadministrativa.enums;

public enum EstadoContrato {
    VIGENTE,
    POR_VENCER,
    VENCIDO,
    CULMINADO;

    public String intoString() {
        return switch (this) {
            case VIGENTE -> "Vigente";
            case POR_VENCER -> "Por vencer";
            case VENCIDO -> "Vencido";
            case CULMINADO -> "Culminado";
        };
    }
}
