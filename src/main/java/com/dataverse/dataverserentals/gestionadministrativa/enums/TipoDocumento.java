package com.dataverse.dataverserentals.gestionadministrativa.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TipoDocumento {
    DNI,
    RUC,
    CARNET_EXTRANJERIA;

    public String intoString() {
        return switch (this) {
            case DNI -> "Dni";
            case RUC -> "Ruc";
            case CARNET_EXTRANJERIA -> "Carnet de extranjería";
        };
    }

}
