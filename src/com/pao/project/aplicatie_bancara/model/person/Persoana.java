package com.pao.project.aplicatie_bancara.model.person;

import java.util.Objects;

public abstract class Persoana {
    private String nume;
    private String prenume;
    private String email;
    private String telefon;

    protected Persoana(String nume, String prenume, String email, String telefon) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.telefon = telefon;
    }

    public abstract String getRol();

    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = Objects.requireNonNull(nume);
    }

    public String getPrenume() {
        return prenume;
    }
    public void setPrenume(String prenume) {
        this.prenume = Objects.requireNonNull(prenume);
    }
    public String getNumeComplet() {
        return prenume + " " + nume;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email);
    }

    public String getTelefon() {
        return telefon;
    }
    public void setTelefon(String telefon) {
        this.telefon = Objects.requireNonNull(telefon);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "numeComplet='" + getNumeComplet() + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + getRol() + '\'' +
                '}';
    }

    @Override 
    public boolean equals(Object o) {
        if(this == o) 
            return true;
        if(!(o instanceof Persoana))
            return false;
        Persoana p = (Persoana) o;
        return Objects.equals(email, p.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}