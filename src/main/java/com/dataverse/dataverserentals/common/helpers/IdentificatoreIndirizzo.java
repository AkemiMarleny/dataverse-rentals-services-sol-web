package com.dataverse.dataverserentals.common.helpers;

import java.util.ArrayList;
import java.util.List;

public class IdentificatoreIndirizzo {
    private final String value;

    public String get() {
        return value;
    }

    public IdentificatoreIndirizzo(String raw) {
        List<String> parole = new SeparatoreParole(raw).get();
        this.value = transform(parole);

    }

    private String transform(List<String> parole) {
        List<String> risultato = new ArrayList<String>();

        for (int i = 0; i < parole.size(); i++) {
            String parola = parole.get(i);
            String parolaMinuscola = parola.toLowerCase();

            if (parolaMinuscola.equals("manzana")) {
                String prossimaParola = parole.get(i + 1);
                if (prossimaParola.equals(".")) {
                    risultato.add("Mz");
                } else {
                    risultato.add("Mz.");
                }
            } else if (parolaMinuscola.equals("lote")) {
                String prossimaParola = parole.get(i + 1);
                if (prossimaParola.equals(".")) {
                    risultato.add("Lt");
                } else {
                    risultato.add("Lt.");
                }
            } else {
                risultato.add(parola);
            }
        }
        return String.join("", risultato);
    }
}
