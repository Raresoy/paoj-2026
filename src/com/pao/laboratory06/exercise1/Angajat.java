package com.pao.laboratory06.exercise1;

import java.util.Scanner;

public class Angajat implements Comparable<Angajat>{
    private String nume;
    private double salariu;

    public Angajat(String nume, double salariu) {
        this.nume = nume;
        this.salariu = salariu;
    }

    public String getNume() {
        return nume;
    }

    public double getSalariu() {
        return salariu;
    }

    public static Angajat citeste(Scanner scanner) {
        String nume = scanner.next();
        double salariu = scanner.nextDouble();
        return new Angajat(nume, salariu);
    }

    @Override
    public int compareTo(Angajat val) {
        return Double.compare(this.salariu, val.salariu);
    }

    @Override
    public String toString() {
        return nume + " " + salariu;
    }
}
