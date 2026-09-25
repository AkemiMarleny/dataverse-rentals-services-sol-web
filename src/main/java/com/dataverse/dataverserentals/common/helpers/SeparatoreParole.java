package com.dataverse.dataverserentals.common.helpers;

import java.util.ArrayList;
import java.util.List;

public class SeparatoreParole {

    private final List<String> paroleInterne;

    public List<String> get() {
        return new ArrayList<>(paroleInterne);
    }

    public SeparatoreParole(String raw) {
        this.paroleInterne = separe(raw);
    }

    private List<String> separe(String raw) {
        List<String> risultato = new ArrayList<>();
        StringBuilder possibileParola = new StringBuilder();
        StringBuilder possibileNumero = new StringBuilder();

        for (char carattere : raw.toCharArray()) {
            if (Character.isLetter(carattere)) {
                if (possibileNumero.length() > 0) {
                    aggiungiParolaSiPresente(risultato, possibileParola, possibileNumero);
                }
                possibileParola.append(carattere);
            } else if (Character.isDigit(carattere)) {
                if (possibileParola.length() > 0) {
                    aggiungiParolaSiPresente(risultato, possibileParola, possibileNumero);
                }
                possibileNumero.append(carattere);
            } else {
                aggiungiParolaSiPresente(risultato, possibileParola, possibileNumero);
                risultato.add(String.valueOf(carattere));
            }
        }

        aggiungiParolaSiPresente(risultato, possibileParola, possibileNumero);
        return risultato;

    }

    private void aggiungiParolaSiPresente(List<String> risultato, StringBuilder possibileParola,
            StringBuilder possibileNumero) {
        if (possibileParola.length() > 0) {
            risultato.add(possibileParola.toString());
            possibileParola.setLength(0);
        }
        if (possibileNumero.length() > 0) {
            risultato.add(possibileNumero.toString());
            possibileNumero.setLength(0);
        }
    }

}
