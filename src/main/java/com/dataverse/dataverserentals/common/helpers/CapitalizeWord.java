package com.dataverse.dataverserentals.common.helpers;

/**
 * Capitaliza una sola palabra.
 * Ejemplo: "empresa" -> "Empresa"
 */
public class CapitalizeWord {

    private final String value;

    public CapitalizeWord(String raw) {
        if (raw == null || raw.isBlank()) {
            this.value = raw;
            return;
        }
        this.value = raw.substring(0, 1).toUpperCase() + raw.substring(1).toLowerCase();
    }

    public String get() {
        return value;
    }
}