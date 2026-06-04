package com.pao.project.aplicatie_bancara.model.account;

public class ContEconomii extends Cont {
    private double rataDobanda;
    private int perioadaLunii;

    public ContEconomii(IBAN iban, double soldInitial, String moneda, double rataDobanda, int perioadaLunii) {
        super(iban, soldInitial, moneda);

        if(rataDobanda < 0)
            throw new IllegalArgumentException("Rata dobanzii nu poate fi negativa");
        if(perioadaLunii < 0)
            throw new IllegalArgumentException("Perioada lunii nu poate fi negativa");
        this.rataDobanda = rataDobanda;
        this.perioadaLunii = perioadaLunii;
    }

    @Override
    public String getTipCont() {
        return "ContEconomii";
    }

    public double getRataDobanda() {
        return rataDobanda;
    }
    public int getPerioadaLunii() {
        return perioadaLunii;
    }
    public void setRataDobanda(double rataDobanda) {
        if(rataDobanda < 0)
            throw new IllegalArgumentException("Rata dobanzii nu poate fi negativa");
        this.rataDobanda = rataDobanda;
    }

    public double calculeazaDobanda(int luni) {
        return getSold() * (rataDobanda / 100.0) * (luni / 12.0);
    }

    public void aplicaDobanda() {
        double dobanda = calculeazaDobanda(perioadaLunii);
        depune(dobanda);
        System.out.println("Dobanda aplicata: +" + String.format("%.2f", dobanda) + " " + getMoneda());
    }

    @Override
    public String toString() {
        return "ContEconomii{" +
                "iban=" + getIban().getFormatat() +
                ", sold=" + String.format("%.2f", getSold()) + " " + getMoneda() +
                ", rataDobanda=" + rataDobanda + "%" +
                ", perioadaLuni=" + perioadaLunii +
                ", stare=" + getStare() +
                '}';
    }
}