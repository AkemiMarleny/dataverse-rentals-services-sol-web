package com.dataverse.dataverserentals.gestionoperativa.valueobjects;

import java.math.BigDecimal;

public class CostoExcedente {
    private final BigDecimal costoExcedente;

    private CostoExcedente(BigDecimal costoExcedente) {
        this.costoExcedente = costoExcedente;
    }

    public BigDecimal intoBigDecimal() {
        return this.costoExcedente;
    }

    public static CostoExcedente from(
            BigDecimal costoGeneral,
            BigDecimal costoTotalUnidades) {

        BigDecimal costoExcedente = costoGeneral.subtract(costoTotalUnidades);

        return new CostoExcedente(costoExcedente);
    }
}
