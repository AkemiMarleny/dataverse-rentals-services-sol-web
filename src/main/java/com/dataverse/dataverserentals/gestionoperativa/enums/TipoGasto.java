package com.dataverse.dataverserentals.gestionoperativa.enums;

public enum TipoGasto {
    MATERIAL,
    MANO_OBRA;

    public String intoString() {
        return switch (this) {
            case MATERIAL -> "Material";
            case MANO_OBRA -> "Mano de obra";
        };
    }
}
