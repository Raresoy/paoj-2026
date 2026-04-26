package com.pao.project.aplicatie_bancara.model.account;

import java.util.Objects;

public final class IBAN {
    private final String valoare;
    private final String codTara;
    private final String codBanca;

    public IBAN(String valoare) {
        Objects.requireNonNull(valoare);
        String curatata = valoare.replaceAll("\\s", "").toUpperCase();
        if(curatata.length() < 15 || curatata.length() > 34)
            throw new IllegalArgumentException("IBAN invalid");
        this.valoare = curatata;
        this.codTara = curatata.substring(0, 2);
        this.codBanca = curatata.length >= 8 ? curatata.substring(4, 8) : "";
    }

    public String getValoare() {
        return valoare;
    }
    public String getCodTara() {
        return codTara;
    }
    public String getCodBanca() {
        return codBanca;
    }

    public String getFormatat() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < valoare.length(); i++) {
            if(i > 0 && i % 4 == 0)
                sb.append(' ');
            sb.append(valoare.charAt(i));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IBAN)) return false;
        return Objects.equals(valoare, ((IBAN) o).valoare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valoare);
    }

    @Override
    public String toString() {
        return "IBAN{" + getFormatat() + "}";
    }

}