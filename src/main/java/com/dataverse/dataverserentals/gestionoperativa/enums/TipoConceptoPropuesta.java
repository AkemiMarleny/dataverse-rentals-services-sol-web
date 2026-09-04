package com.dataverse.dataverserentals.gestionoperativa.enums;

public enum TipoConceptoPropuesta {
    GASTOS_COMUNES,
    ELECTRICIDAD_GASTOS_COMUNES,
    AGUA_GASTOS_COMUNES;

    public String intoString() {
        return switch (this) {
            case GASTOS_COMUNES -> "Gastos comunes";
            case ELECTRICIDAD_GASTOS_COMUNES -> "Consumo Eléctrico Áreas Comunes";
            case AGUA_GASTOS_COMUNES -> "Consumo de Agua Áreas Comunes";
        };
    }
}
