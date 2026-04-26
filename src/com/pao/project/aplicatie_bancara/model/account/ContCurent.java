package com.pao.project.aplicatie_bancara.model.account;

public class ContCurent extends Cont {
    private double limitaDescoperit;

    public ContCurent(IBAN iban, double soldInitial, String moneda, double limitaDescoperit) {
        super(iban, soldInitial, moneda);
        if(limitaDescoperit < 0)
            throw new IllegalArgumentException("Limita nu poate fi negativa");

        this.limitaDescoperit = limitaDescoperit;
    }

    public ContCurent(IBAN iban, double soldInitial, String moneda) {
        super(iban, soldInitial, moneda);
    }

    @Override
    public String getTipCont() {
        return "ContCurent";
    }

    public double getLimitaDescoperit() {
        return limitaDescoperit;
    }
    public void setLimitaDescoperit(double limita) {
        if(limita < 0)
            throw new IllegalArgumentException("Limita nu poate fi negativa");
        this.limitaDescoperit = limita;
    }

    @Override
    public void retrage(double suma) throws com.pao.project.aplicatie_bancara.exceptions.FonduriInsuficienteException {
        if(suma <= 0)
            throw new IllegalArgumentException("Suma de retras trebuie sa fie mai mare decat 0");
        if(getStare() != Stare.ACTIV)
            throw new IllegalStateException("Contul nu este activ");
        if(getSold() + limitaDescoperit < suma) {
            throw new com.pao.project.aplicatie_bancara.exceptions.FonduriInsuficienteException(
                "Fonduri insuficiente chiar si cu descoperit: disponibil=" + 
                (getSold() + limitaDescoperit) + " " + getMoneda());
        }

        scadeSold(suma);
    }

    @Override
    public String toString() {
        return "ContCurent{" +
                "iban=" + getIban().getFormatat() +
                ", sold=" + String.format("%.2f", getSold()) + " " + getMoneda() +
                ", limitaDescoperit=" + String.format("%.2f", limitaDescoperit) +
                ", stare=" + getStare() +
                '}';
    }

}