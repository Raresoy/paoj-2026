package com.pao.laboratory02.exercise3.model;

public abstract class Angajat {
    private String name;
    private double salariuBaza;

    public Angajat(String name, double salariuBaza) {
        this.name = name;
        this.salariuBaza = salariuBaza;
    }

    public String getName() { return name; }
    public double getSalariuBaza() { return salariuBaza; }

    public abstract double salariuTotal();
    
    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{name='" + name + "', salariuBaza=" + salariuBaza +
                ", salariuTotal=" + String.format("%.2f", salariuTotal()) + "}";
    }
}