package com.pao.project.aplicatie_bancara.model.person;

import com.pao.project.aplicatie_bancara.model.account.Cont;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Client extends Persoana implements Comparable<Client> {
    private final String cnp;
    private String adresa;
    private final List<Cont> conturi;
    
    public Client(String nume, String prenume, String cnp, String email, String telefon, String adresa) {
        super(nume, prenume, email, telefon);
        if(cnp == null || cnp.length() != 13)
            throw new IllegalArgumentException("CNP-ul trebuie sa contina 13 cifre");
        this.cnp = cnp;
        this.adresa = adresa;
        this.conturi = new ArrayList<>();
    }

    @Override
    public String getRol() {
        return "Client";
    }

    public String getCnp() {
        return cnp;
    }
    public List<Cont> getConturi() {
        return conturi;
    }

    public String getAdresa() {
        return adresa;
    }
    public void setAdresa() {
        this.adresa = Objects.requireNonNull(adresa);
    }

    public void adaugaCont(Cont cont) {
        Objects.requireNonNull(cont);
        conturi.add(cont);
    }

    public void eliminaCont(Cont cont) {
        conturi.remove(cont);
    }

    @Override
    public int compareTo(Client other) {
        return this.getNume().compareToIgnoreCase(other.getNume());
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof Client))
            return false;
        Client c = (Client) o;
        return Objects.equals(cnp, c.cnp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnp);
    }

    @Override
    public String toString() {
        return "Client{" +
                "numeComplet='" + getNumeComplet() + '\'' +
                ", cnp='" + cnp + '\'' +
                ", email='" + getEmail() + '\'' +
                ", nrConturi=" + conturi.size() +
                '}';
    }


}