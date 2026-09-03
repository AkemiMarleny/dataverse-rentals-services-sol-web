package com.dataverse.dataverserentals.gestionfinanciera.valueobjects;

import java.math.BigDecimal;

public class MontoProrrateado {

    private final BigDecimal montoProrrateado;

    private MontoProrrateado(BigDecimal montoProrrateado) {
        this.montoProrrateado = montoProrrateado;
    }

    public BigDecimal intoBigDecimal() {
        return this.montoProrrateado;
    }

    public static MontoProrrateado from(
            CoeficienteUnidad coeficienteUnidad,
            BigDecimal montoAProrratear) {

        BigDecimal montoProrrateado = coeficienteUnidad.intoBigDecimal()
                .multiply(montoAProrratear);

        return new MontoProrrateado(montoProrrateado);
    }
}
