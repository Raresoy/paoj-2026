package com.pao.laboratory02.exercise3.model;

public class Programator extends Angajat {

    private String limbajPreferat;

    public Programator(String name, int salariuBaza, String limbajPreferat) {
        super(name, salariuBaza);
        this.limbajPreferat = limbajPreferat;
    }

    @Override
    public double salariuTotal() {
        return getSalariuBaza() * 1.5;
    }
}