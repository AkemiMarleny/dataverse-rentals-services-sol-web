package com.dataverse.dataverserentals.gestionoperativa.valueobjects;

import java.math.BigDecimal;

public class ConsumoKwh {
    private final BigDecimal consumoKwh;

    private ConsumoKwh(BigDecimal consumoKwh) {
        this.consumoKwh = consumoKwh;
    }

    public BigDecimal intoBigDeciaml() {
        return this.consumoKwh;
    }

    public static ConsumoKwh from(
            BigDecimal lecturaInicial,
            BigDecimal lecturaFinal) {
        BigDecimal consumoKwh = lecturaFinal.subtract(lecturaInicial);
        return new ConsumoKwh(consumoKwh);
    }
}
