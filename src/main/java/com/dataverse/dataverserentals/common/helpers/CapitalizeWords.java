package com.dataverse.dataverserentals.common.helpers;

/**
 * Capitaliza cada palabra.
 * Ejemplo: "mi empresa sac" → "Mi empresa sac"
 */
public class CapitalizeWords {

    private final String capitalizado;

    public CapitalizeWords(String texto) {
        if (texto == null || texto.isBlank()) {
            this.capitalizado = texto;
            return;
        }
        
        String[] palabras = texto.toLowerCase().trim().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        
        for (String palabra : palabras) {
            if (resultado.length() > 0) {
                resultado.append(" ");
            }
            resultado.append(primeraLetraMayuscula(palabra));
        }
        
        this.capitalizado = resultado.toString();
    }

    private String primeraLetraMayuscula(String palabra) {
        if (palabra == null || palabra.isEmpty()) {
            return palabra;
        }
        return palabra.substring(0, 1).toUpperCase() + palabra.substring(1).toLowerCase();
    }

    public String get() {
        return capitalizado;
    }
}