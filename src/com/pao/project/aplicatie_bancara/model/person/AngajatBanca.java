package com.pao.project.aplicatie_bancara.model.person;

import java.util.Objects;

public class AngajatBanca extends Persoana {
    private final String idAngajat;
    private String departament;
    private double salariu;

    public AngajatBanca(String nume, String prenume, String email, String telefon, String idAngajat,
                        String departament, double salariu) {
        super(nume, prenume, email, telefon);
        this.idAngajat = Objects.requireNonNull(idAngajat);
        this.departament = departament;
        this.salariu = salariu;
    }

    @Override
    public String getRol() {
        return "Angajat - " + departament;
    }

    public String getIdAngajat() {
        return idAngajat;
    }
    public String getDepartament() {
        return departament;
    }
    public void setDepartament(String departament) {
        this.departament = departament;
    }
    public double getSalariu() {
        return salariu;
    }
    public void setSalariu(double salariu) {
        if(salariu < 0)
            throw new IllegalArgumentException("Salariul nu poate avea o valoare negativa");
        this.salariu = salariu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AngajatBanca)) return false;
        AngajatBanca a = (AngajatBanca) o;
        return Objects.equals(idAngajat, a.idAngajat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAngajat);
    }

    @Override
    public String toString() {
        return "AngajatBanca{" +
                "numeComplet='" + getNumeComplet() + '\'' +
                ", idAngajat='" + idAngajat + '\'' +
                ", departament='" + departament + '\'' +
                '}';
    }

}