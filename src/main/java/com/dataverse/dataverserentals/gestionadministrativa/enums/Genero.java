package com.dataverse.dataverserentals.gestionadministrativa.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Genero {
    FEMENINO,
    MASCULINO;

    public String intoString() {
        return switch (this) {
            case FEMENINO -> "Femenino";
            case MASCULINO -> "Masculino";
        };
    }
}
