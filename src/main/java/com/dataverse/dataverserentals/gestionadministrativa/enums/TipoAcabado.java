package com.dataverse.dataverserentals.gestionadministrativa.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TipoAcabado {
    CASCO_GRIS,
    BASICO,
    ESTANDAR,
    SEMI_LUJO,
    LUJO;

    public String intoString() {
        return switch (this) {
            case CASCO_GRIS -> "Casco gris";
            case BASICO -> "Básico";
            case ESTANDAR -> "Estándar";
            case SEMI_LUJO -> "Semi lujo";
            case LUJO -> "Lujo";
        };

    }
}
