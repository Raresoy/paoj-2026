package com.pao.project.aplicatie_bancara.model.transaction;

import com.pao.project.aplicatie_bancara.model.account.IBAN;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Tranzactie {
    public enum TipTranzactie {
        DEPUNERE, RETRAGERE, TRANSFER_TRIMIS, TRANSFER_PRIMIT, PLATA_CARD
    }

    private final String idTranzactie;
    private final TipTranzactie tipTranzactie;
    private final double suma;
    private final String moneda;
    private final IBAN ibanSursa;
    private final IBAN ibanDestinatie;
    private final LocalDateTime dataOra;
    private final String descriere;

    public Tranzactie(TipTranzactie tip, double suma, String moneda, IBAN ibanSursa, IBAN ibanDestinatie, String descriere) {
        this.idTranzactie = UUID.randomUUID().toString().substring(0,8).toUpperCase();
        this.tipTranzactie = Objects.requireNonNull(tip);
        if(suma <= 0)
            throw new IllegalArgumentException("Suma nu poate fi negativa");
        this.suma = suma;
        this.moneda = Objects.requireNonNull(moneda);
        this.ibanSursa = ibanSursa;
        this.ibanDestinatie = ibanDestinatie;
        this.descriere = descriere != null ? descriere : "";
        this.dataOra = LocalDateTime.now();
    }

    public String getIdTranzactie() {
        return idTranzactie;
    }
    public TipTranzactie getTip() {
        return tipTranzactie;
    }
    public double getSuma() {
        return suma;
    }
    public String getMoneda() {
        return moneda;
    }
    public IBAN getIbanSursa() {
        return ibanSursa;
    }
    public IBAN getIbanDestinatie() {
        return ibanDestinatie;
    }
    public LocalDateTime getDataOra() {
        return dataOra;
    }
    public String getDescriere() {
        return descriere;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tranzactie)) return false;
        return Objects.equals(idTranzactie, ((Tranzactie) o).idTranzactie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTranzactie);
    }

    @Override
    public String toString() {
        return "Tranzactie{" +
                "id='" + idTranzactie + '\'' +
                ", tip=" + tipTranzactie +
                ", suma=" + String.format("%.2f", suma) + " " + moneda +
                ", dataOra=" + dataOra +
                ", descriere='" + descriere + '\'' +
                '}';
    }
}
