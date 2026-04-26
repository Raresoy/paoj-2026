package com.pao.project.aplicatie_bancara.model.transaction;

import com.pao.project.aplicatie_bancara.model.account.IBAN;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExtrasDeCont {
    private final IBAN iban;
    private final LocalDate dataInceput;
    private final LocalDate dataSfarsit;
    private final LocalDateTime dataGenerare;
    private final List<Tranzactie> tranzactii;
    private final double soldInitial;
    private double soldFinal;

    public ExtrasDeCont(IBAN iban, LocalDate dataInceput, LocalDate dataSfarsit, double soldInitial, List<Tranzactie> toateTranzactiile) {
        this.iban = iban;
        this.dataInceput = dataInceput;
        this.dataSfarsit = dataSfarsit;
        this.dataGenerare = LocalDateTime.now();
        this.soldInitial = soldInitial;

        this.tranzactii = toateTranzactiile.stream().filter(t -> {
            LocalDate data = t.getDataOra().toLocalDate();
            return !data.isBefore(dataInceput) && !data.isAfter(dataSfarsit);
        }).collect(Collectors.toList());

        this.soldFinal = soldInitial;
        for(Tranzactie t : this.tranzactii) {
            switch(t.getTip()) {
                case DEPUNERE:
                case TRANSFER_PRIMIT:
                    soldFinal += t.getSuma();
                    break;
                case RETRAGERE:
                case TRANSFER_TRIMIS:
                case PLATA_CARD:
                    soldFinal -= t.getSuma();
                    break;
            }
        }
    }

    public IBAN getIban() { 
        return iban; 
    }
    public LocalDate getDataInceput() { 
        return dataInceput; 
    }
    public LocalDate getDataSfarsit() { 
        return dataSfarsit; 
    }
    public LocalDateTime getDataGenerare() { 
        return dataGenerare; 
    }
    public List<Tranzactie> getTranzactii() { 
        return Collections.unmodifiableList(tranzactii); 
    }
    public double getSoldInitial() { 
        return soldInitial; 
    }
    public double getSoldFinal() { 
        return soldFinal; 
    }
    public int getNrTranzactii() { 
        return tranzactii.size(); 
    }

    public void afiseaza() {
        System.out.println("=".repeat(60));
        System.out.println("  EXTRAS DE CONT");
        System.out.println("  IBAN: " + iban.getFormatat());
        System.out.println("  Perioada: " + dataInceput + " — " + dataSfarsit);
        System.out.println("  Generat la: " + dataGenerare);
        System.out.println("=".repeat(60));
        System.out.printf("  Sold initial: %.2f%n", soldInitial);
        System.out.println("-".repeat(60));
        for (Tranzactie t : tranzactii) {
            System.out.println("  " + t);
        }
        System.out.println("-".repeat(60));
        System.out.printf("  Sold final:   %.2f%n", soldFinal);
        System.out.println("=".repeat(60));
    }

    @Override
    public String toString() {
        return "ExtrasDecont{" +
                "iban=" + iban.getFormatat() +
                ", perioada=[" + dataInceput + ", " + dataSfarsit + "]" +
                ", nrTranzactii=" + tranzactii.size() +
                ", soldFinal=" + String.format("%.2f", soldFinal) +
                '}';
    }

}