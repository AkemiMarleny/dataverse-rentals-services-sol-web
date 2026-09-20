package com.dataverse.dataverserentals.common.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BusinessNameCapitalizer {

    private final String valor;

    private static final Set<String> PALABRAS_MINUSCULAS = new HashSet<>(
            Arrays.asList("de", "la", "del", "los", "y", "las", "al", "el", "con", "en", "por", "para"));

    public BusinessNameCapitalizer(String texto) {
        if (texto == null || texto.isBlank()) {
            this.valor = "";
        } else {
            String[] palabras = texto.toLowerCase().trim().split("\\s+");
            this.valor = transformar(palabras);
        }
    }

    private String transformar(String[] palabras) {
        List<String> contenido = new ArrayList<>();

        for (int i = 0; i < palabras.length; i++) {
            String palabra = palabras[i];

            if (i == 0) {
                contenido.add(new CapitalizeWord(palabra).get());
            } else if (TiposEmpresaJoiner.esTipoEmpresa(palabra)) {
                contenido.add(new TiposEmpresaJoiner(palabra).get());
            } else if (PALABRAS_MINUSCULAS.contains(palabra)) {
                contenido.add(palabra);
            } else {
                contenido.add(new CapitalizeWord(palabra).get());
            }
        }
        return String.join(" ", contenido);
    }

    public String get() {
        return valor;
    }
}