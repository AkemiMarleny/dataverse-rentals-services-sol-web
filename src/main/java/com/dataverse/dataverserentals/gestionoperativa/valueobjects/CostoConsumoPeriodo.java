package com.dataverse.dataverserentals.gestionoperativa.valueobjects;

import java.math.BigDecimal;

public class CostoConsumoPeriodo {
    private final static BigDecimal PRECIO_KWH = new BigDecimal("0.80");

    private final BigDecimal costoConsumo;

    private CostoConsumoPeriodo(BigDecimal costoConsumo) {
        this.costoConsumo = costoConsumo;
    }

    public BigDecimal intoBigDecimal() {
        return this.costoConsumo;
    }

    public static CostoConsumoPeriodo from(
            ConsumoKwh consumoKwh) {
        BigDecimal costoConsumo = consumoKwh.intoBigDeciaml()
                .multiply(PRECIO_KWH);
        return new CostoConsumoPeriodo(costoConsumo);
    }
}
