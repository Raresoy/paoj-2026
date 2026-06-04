package com.pao.laboratory06.exercise2;

public abstract class Colaborator implements OperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;

    public abstract double calculeazaVenitNetAnual();
}