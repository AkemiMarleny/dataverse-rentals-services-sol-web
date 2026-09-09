package com.dataverse.dataverserentals.gestionfinanciera.enums;

public enum TipoConceptoRecibo {
    ELECTRICIDAD,
    AGUA,
    ALQUILER_MENSUAL,
    GASTOS_COMUNES,
    ELECTRICIDAD_GASTOS_COMUNES,
    AGUA_GASTOS_COMUNES;

    public String intoString() {
        return switch (this) {
            case ELECTRICIDAD -> "Electricidad";
            case AGUA -> "Agua";
            case ALQUILER_MENSUAL -> "Alquiler mensual";
            case GASTOS_COMUNES -> "Gastos comunes";
            case ELECTRICIDAD_GASTOS_COMUNES -> "Consumo Eléctrico Áreas Comunes";
            case AGUA_GASTOS_COMUNES -> "Consumo de Agua Áreas Comunes";
        };
    }
}
