package com.pao.project.aplicatie_bancara.model.account;

import com.pao.project.aplicatie_bancara.exceptions.FonduriInsuficienteException;
import com.pao.project.aplicatie_bancara.model.card.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Cont {
    public enum Stare {
        ACTIV, INACTIV, BLOCAT;
    }

    private final IBAN iban;
    private double sold;
    private Stare stare;
    private final String moneda;
    private final List<Card> carduri;

    protected Cont(IBAN iban, double soldInitial, String moneda) {
        this.iban = Objects.requireNonNull(iban);
        if(soldInitial < 0)
            throw new IllegalArgumentException("Soldul initial nu poate fi negativ");
        this.sold = soldInitial;
        this.moneda = Objects.requireNonNull(moneda);
        this.stare = Stare.ACTIV;
        this.carduri = new ArrayList<>();
    }

    public abstract String getTipCont();

    public IBAN getIban() {
        return iban;
    }
    public double getSold() {
        return sold;
    }
    public Stare getStare() {
        return stare;
    }
    public String getMoneda() {
        return moneda;
    }
    public List<Card> getCarduri() {
        return Collections.unmodifiableList(carduri);
    }

    public void setStare(Stare stare) {
        this.stare = Objects.requireNonNull(stare);
    }

    public void adaugaCard(Card card) {
        Objects.requireNonNull(card);
        carduri.add(card);
    }

    public void depune(double suma) {
        if(suma <= 0) 
            throw new IllegalArgumentException("Suma de depus trebuie sa fie pozitiva");
        if(stare != Stare.ACTIV)
            throw new IllegalStateException("Contul nu este activ");
        sold += suma;
    }

    protected void scadeSold(double suma) {
        this.sold -= suma;
    }

    public void retrage(double suma) throws FonduriInsuficienteException {
        if(suma <= 0)
            throw new IllegalArgumentException("Suma de retras trebuie sa fie pozitiva");
        if(stare != Stare.ACTIV)
            throw new IllegalStateException("Contul nu este activ");
        if(sold < suma) {
            throw new FonduriInsuficienteException(
                "Fonduri insuficiente: sold=" + sold + " " + moneda + ", suma ceruta=" +
                suma + " " + moneda);
        }
        scadeSold(suma);
    }

    @Override 
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(!(o instanceof Cont))
            return false;
        return Objects.equals(iban, ((Cont) o).iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }

    @Override
    public String toString() {
        return getTipCont() + "{" +
                "iban=" + iban.getFormatat() +
                ", sold=" + String.format("%.2f", sold) + " " + moneda +
                ", stare=" + stare +
                '}';
    }

}