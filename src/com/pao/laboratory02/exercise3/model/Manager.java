package com.pao.laboratory02.exercise3.model;

public class Manager extends Angajat {
    
    private int nrSubordonati;

    public Manager(String name, double salariuBaza, int nrSubordonati) {
        super(name, salariuBaza);
        this.nrSubordonati = nrSubordonati;
    }

    @Override
    public double salariuTotal() {
        return getSalariuBaza() * 2 + nrSubordonati * 100;
    }

}