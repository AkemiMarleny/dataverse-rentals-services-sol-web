package com.dataverse.dataverserentals.gestionfinanciera.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CoeficienteUnidad {
    private final BigDecimal coeficiente;

    private CoeficienteUnidad(BigDecimal coeficiente) {
        this.coeficiente = coeficiente;
    }

    public BigDecimal intoBigDecimal() {
        return this.coeficiente;
    }

    public static CoeficienteUnidad from(
            BigDecimal areaArrendable,
            BigDecimal areaUnidad) {

        BigDecimal coeficiente = areaUnidad
                .divide(areaArrendable, 4, RoundingMode.HALF_UP);

        return new CoeficienteUnidad(coeficiente);
    }
}
