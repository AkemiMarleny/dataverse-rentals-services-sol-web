package com.dataverse.dataverserentals.common.helpers;

public class Alphanumeric {

    private final String value;

    public String get() {
        return value;
    }

    public Alphanumeric(String raw) {
        this.value = raw
                .replaceAll("[^ A-Za-zÁÉÍÓÚáéíóúÑñ0-9-./,()]", "")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }
}
