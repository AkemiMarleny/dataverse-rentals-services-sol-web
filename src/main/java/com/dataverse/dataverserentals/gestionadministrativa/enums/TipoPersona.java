package com.dataverse.dataverserentals.gestionadministrativa.enums;

public enum TipoPersona {
    NATURAL_CON_NEGOCIO,
    NATURAL_SIN_NEGOCIO,
    JURIDICA;

    public String intoString() {
        return switch (this) {
            case NATURAL_CON_NEGOCIO -> "Natural con negocio";
            case NATURAL_SIN_NEGOCIO -> "Natural sin negocio";
            case JURIDICA -> "Jurídica";
        };
    }
}