package com.dataverse.dataverserentals.gestionadministrativa.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TipoMedidor {
    GENERAL_ELECTRICIDAD,
    GENERAL_AGUA,
    ELECTRICIDAD,
    AGUA;

    public String intoString() {
        return switch (this) {
            case GENERAL_ELECTRICIDAD -> "General electrecidad";
            case GENERAL_AGUA -> "General agua";
            case ELECTRICIDAD -> "Electrecidad";
            case AGUA -> "Agua";
        };
    }
}
