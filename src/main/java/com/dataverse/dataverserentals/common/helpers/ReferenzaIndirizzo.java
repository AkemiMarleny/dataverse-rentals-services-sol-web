package com.dataverse.dataverserentals.common.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReferenzaIndirizzo {
    private final String value;

    private static final Set<String> PAROLE_MAIUSCOLE = new HashSet<>(
            Arrays.asList("RENIEC", "SUNAT", "JNE", "MINSA", "MINEDU", "BCRP", "BCP", "BBVA", "SBS", "MTC", "ESSALUD",
                    "SENATI", "SIS", "INDECOPI", "SENCICO", "SUNARP", "PNP", "MPC", "MAC",
                    "COFOPRI", "INPE", "DIREJ", "SUTRAN", "CONADIS", "UPN", "UNC",
                    "ONPE", "DIRESA", "OSIPTEL"));

    private static final Set<String> TIPI_VIE_ABBREVIATE = new HashSet<>(
            Arrays.asList("AV", "JR", "URB", "VÍA", "PSJ", "PROL",
                    "BLVR", "DPTO", "MZ", "LT", "CP", "AH"));

    private static final Set<String> PAROLE_RISERVATE = new HashSet<>(
            Arrays.asList("BANCO", "NACIÓN", "INTERBANK", "CRÉDITO", "SCOTIABANK", "PERÚ", "BANBIF",
                    "INTERAMERICANO", "METRO", "PLAZA", "ARMAS", "SAN", "FRANCISCO", "RECOLETA", "CATEDRAL", "PEDRO",
                    "REAL",
                    "PLAZA", "MEGA", "OPEN", "INSTITUCIÓN", "EDUCATIVA", "INICIAL", "UNIVERSIDAD", "INSTITUTO",
                    "HOSPITAL", "CALLE", "RESIDENCIAL", "ALAMEDA", "ÓVALO", "LOTE",
                    "SEGURO", "HOSPITAL", "POSTA", "COOPERATIVA",
                    "FINANZAS", "PICHINCHA", "CAJA", "AREQUIPA", "PIURA", "TRUJILLO", "HUANCAYO", "COMPARTAMOS"));

    public String get() {
        return value;
    }

    public ReferenzaIndirizzo(String raw) {
        List<String> parole = new SeparatoreParole(raw).get();
        this.value = transform(parole);
    }

    private String transform(List<String> parole) {
        List<String> contenuto = new ArrayList<String>();
        for (int i = 0; i < parole.size(); i++) {
            String parola = parole.get(i);
            if (PAROLE_MAIUSCOLE.contains(parola.toUpperCase())) {
                contenuto.add(parola.toUpperCase());
            } else if (i == 0) {
                contenuto.add(new CapitalizeWord(parola).get());
            } else if (TIPI_VIE_ABBREVIATE.contains(parola.toUpperCase())) {
                contenuto.add(new CapitalizeWord(parola).get().concat("."));
            } else if (PAROLE_RISERVATE.contains(parola.toUpperCase())) {
                contenuto.add(new CapitalizeWord(parola).get());
            } else {
                contenuto.add(parola);
            }
        }
        return String.join("", contenuto);
    }

}
