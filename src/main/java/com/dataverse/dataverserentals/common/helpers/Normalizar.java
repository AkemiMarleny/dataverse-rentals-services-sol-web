package com.dataverse.dataverserentals.common.helpers;

/**
 * Limpia texto removiendo caracteres especiales.
 * Ejemplo: "Mi@Empresa# SAC!" → "MiEmpresa SAC"
 */
public class Normalizar {

    private final String valor;

    public Normalizar(String texto) {
        this.valor = texto
                .replaceAll("[^ A-Za-zÁÉÍÓÚáéíóúÑñ,&-]", "")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }

    public String get() {
        return valor;
    }
}