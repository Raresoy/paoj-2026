package com.pao.project.aplicatie_bancara.model.card;

import com.pao.project.aplicatie_bancara.model.account.IBAN;
import java.time.LocalDate;
import java.util.Objects;

public class Card {
    public enum TipCard {
        DEBIT, CREDIT
    }
    public enum StareCard {
        ACTIV, BLOCAT, EXPIRAT
    }

    private final String numarCard;
    private final TipCard tipCard;
    private final IBAN iban;
    private final String numeDetinator;
    private final LocalDate dataExpirare;
    private StareCard stare;
    private double limitaZilnica;
    private double limitaLunara;

    public Card(String numarCard, TipCard tipCard, IBAN iban, String numeDetinator, LocalDate dataExpirare,
        double limitaZilnica, double LimitaLunara
    ) {
        this.numarCard = Objects.requireNonNull(numarCard);
        this.tipCard = Objects.requireNonNull(tipCard);
        this.iban = Objects.requireNonNull(iban);
        this.numeDetinator = Objects.requireNonNull(numeDetinator);
        this.dataExpirare = Objects.requireNonNull(dataExpirare);
        this.stare = StareCard.ACTIV;
        this.limitaZilnica = limitaZilnica;
        this.limitaLunara = limitaLunara;
    }

    public String getNumarCard() { return numarCard; }
    public TipCard getTipCard() { return tipCard; }
    public IBAN getIbanContAsociat() { return iban; }
    public String getNumeDetinutor() { return numeDetinator; }
    public LocalDate getDataExpirare() { return dataExpirare; }
    public StareCard getStare() { return stare; }
    public double getLimitaZilnica() { return limitaZilnica; }
    public double getLimitaLunara() { return limitaLunara; }

    public void setStare(StareCard stare) { 
        this.stare = Objects.requireNonNull(stare); 
    }
    public void setLimitaZilnica(double limita) {
        if (limita < 0)
            throw new IllegalArgumentException("Limita zilnica nu poate fi negativa");
        this.limitaZilnica = limita;
    }
    public void setLimitaLunara(double limita) {
        if (limita < 0)
            throw new IllegalArgumentException("Limita lunara nu poate fi negativa");
        this.limitaLunara = limita;
    }

    public boolean esteActiv() {
        return stare == StareCard.ACTIV && dataExpirare.isAfter(LocalDate.now());
    }

    public String getNumarMascat() {
        if(numarCard.length() < 10)
            return numarCard;
        return numarCard.substring(0, 6) + "******" + numarCard.substring(numarCard.length() - 4);
    }

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        return Objects.equals(numarCard, ((Card) o).numarCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numarCard);
    }

    @Override
    public String toString() {
        return "Card{" +
                "numar=" + getNumarMascat() +
                ", tip=" + tipCard +
                ", detinutor='" + numeDetinator + '\'' +
                ", expira=" + dataExpirare +
                ", stare=" + stare +
                '}';
    }


}