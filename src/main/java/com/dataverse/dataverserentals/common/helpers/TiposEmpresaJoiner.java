package com.dataverse.dataverserentals.common.helpers;

import java.util.regex.Pattern;

/**
 * Clase que normaliza las siglas de tipos de empresa.
 * Convierte "S. A." a "S.A." o "SAC" a "S.A.C."
 */
public class TiposEmpresaJoiner {

    private static final Pattern PATRON_TIPO_EMPRESA = Pattern.compile(
            "^(S\\.?\\s*A\\.?\\s*C\\.?|S\\.?\\s*A\\.?\\s*A\\.?|S\\.?\\s*R\\.?\\s*L\\.?|E\\.?\\s*I\\.?\\s*R\\.?\\s*L\\.?|S\\.?\\s*A\\.?)$",
            Pattern.CASE_INSENSITIVE);

    private final String textoProcesado;

    public TiposEmpresaJoiner(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            this.textoProcesado = "";
            return;
        }

        String procesada = raw.trim().replaceAll("\\s+", " ");

        procesada = procesada.replaceAll("(?i)\\bE[\\s\\.]*I[\\s\\.]*R[\\s\\.]*L\\b[\\.]*", "E.I.R.L.");
        procesada = procesada.replaceAll("(?i)\\bS[\\s\\.]*A[\\s\\.]*C\\b[\\.]*", "S.A.C.");
        procesada = procesada.replaceAll("(?i)\\bS[\\s\\.]*A[\\s\\.]*A\\b[\\.]*", "S.A.A.");
        procesada = procesada.replaceAll("(?i)\\bS[\\s\\.]*R[\\s\\.]*L\\b[\\.]*", "S.R.L.");
        procesada = procesada.replaceAll("(?i)\\bS[\\s\\.]*A\\b[\\.]*", "S.A.");

        this.textoProcesado = procesada;
    }

    public static boolean esTipoEmpresa(String palabra) {
        return palabra != null && PATRON_TIPO_EMPRESA.matcher(palabra.trim()).matches();
    }

    public String get() {
        return this.textoProcesado;
    }
}