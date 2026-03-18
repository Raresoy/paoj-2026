package com.pao.laboratory03.exercise.model;

public enum Subject {
    PAOJ("Programare Avansata Orientata pe Obiect", 6), 
    BD("Baze de date", 4), 
    SO("Sisteme de Operare", 5),
    RC("Retele de calculatoare", 5);

    private String fullName;
    private int credits;

    private Subject(String fullName, int credits) {
        this.fullName = fullName;
        this.credits = credits;
    }

    public String getName() {
        return fullName;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return this.name() + " (" + fullName + ", " + credits + ")";
    }

};