package com.dataverse.dataverserentals.common.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class NombreCapitalizar {

    private final String valor;
    private static final Set<String> PREPOSICIONES = new HashSet<>(
            Arrays.asList("de", "la", "del", "los", "y", "las", "al", "el"));

    public String get() {
        return valor;
    }

    public NombreCapitalizar(String texto) {
        String[] palabras = texto.toLowerCase().trim().split("\\s+");
        this.valor = transformar(palabras);
    }

    private String transformar(String[] palabras) {
        List<String> contenido = new ArrayList<>();
        for (int i = 0; i < palabras.length; i++) {
            String palabra = palabras[i];
            if (i == 0) {
                contenido.add(new CapitalizeWord(palabra).get());
            } else if (palabra.contains("-")) {
                contenido.add(capitalizarPalabraCompuesta(palabra, "-", true));
            } else if (palabraEsPreposicion(palabra)) {
                contenido.add(palabra);
            } else if (palabra.contains("/")) {
                contenido.add(capitalizarPalabraCompuesta(palabra, "/", false));
            } else {
                contenido.add(new CapitalizeWord(palabra).get());
            }
        }
        return String.join(" ", contenido);
    }

    private String capitalizarPalabraCompuesta(String palabra, String separador, boolean verificarPreposiciones) {
        String[] subPalabras = palabra.split(Pattern.quote(separador));
        List<String> subContenido = new ArrayList<>();

        for (String subPalabra : subPalabras) {
            if (verificarPreposiciones && palabraEsPreposicion(subPalabra)) {
                subContenido.add(subPalabra);
            } else {
                subContenido.add(new CapitalizeWord(subPalabra).get());
            }
        }

        if (subContenido.isEmpty()) {
            return separador;
        }

        return String.join(separador, subContenido);
    }

    private boolean palabraEsPreposicion(String palabra) {
        return PREPOSICIONES.contains(palabra);
    }
}