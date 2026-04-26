package com.pao.project.aplicatie_bancara.service;

import com.pao.project.aplicatie_bancara.exceptions.ContNegasitException;
import com.pao.project.aplicatie_bancara.exceptions.FonduriInsuficienteException;
import com.pao.project.aplicatie_bancara.model.account.Cont;
import com.pao.project.aplicatie_bancara.model.person.Client;
import com.pao.project.aplicatie_bancara.model.transaction.Tranzactie;
import java.util.*;

public class ContService {
    private static ContService instance;
    private final Map<String, Cont> contIban;
    private final Map<String, List<Tranzactie>> istoricTranzactii;

    private ContService() {
        this.contIban = new LinkedHashMap<>();
        this.istoricTranzactii = new HashMap<>();
    }

    public static ContService getInstance() {
        if(instance == null)
            instance = new ContService();
        return instance;
    }

    public Cont cautaDupaIban(String ibanVal) throws ContNegasitException {
        if(ibanVal == null || ibanVal.isBlank()) 
            throw new IllegalArgumentException("IBAN-ul nu poate fi null");
        Cont cont = contIban.get(ibanVal.replaceAll("\\s", "").toUpperCase());
        if(cont == null)
            throw new ContNegasitException("Nu exista un cont cu acest IBAN");
        return cont;
    }

    public void deschideCont(Client client, Cont cont) {
        Objects.requireNonNull(client);
        Objects.requireNonNull(cont);
        String ibanVal = cont.getIban().getValoare();
        if(contIban.containsKey(ibanVal))
            throw new IllegalArgumentException("Exita deja un cont cu acest IBAN");
        contIban.put(ibanVal, cont);
        istoricTranzactii.put(ibanVal, new ArrayList<>());
        client.adaugaCont(cont);
        System.out.println("[ContService] Cont deschis: " + cont.getIban().getFormatat() + " pentru " + client.getNumeComplet());
    }

    public void inchideCont(String ibanVal) throws ContNegasitException {
        Cont cont = cautaDupaIban(ibanVal);
        cont.setStare(Cont.Stare.INACTIV);
        System.out.println("[ContService] Cont inchis: " + cont.getIban().getFormatat());
    }

    private void inregistreazaTranzactie(String ibanVal, Tranzactie t) {
        String iban = ibanVal.replaceAll("\\s", "").toUpperCase();
        istoricTranzactii.computeIfAbsent(iban, k -> new ArrayList<>()).add(t);
    }

    public Tranzactie depune(String ibanVal, double suma, String descriere) throws ContNegasitException {
        Cont cont = cautaDupaIban(ibanVal);
        cont.depune(suma);
        Tranzactie t = new Tranzactie(Tranzactie.TipTranzactie.DEPUNERE, suma, cont.getMoneda(), null, cont.getIban(), descriere);
        inregistreazaTranzactie(ibanVal, t);
        System.out.printf("[ContService] Depunere: +%.2f %s in contul %s%n", suma, cont.getMoneda(), cont.getIban().getFormatat());
        return t;
    }

    public Tranzactie retrage(String ibanVal, double suma, String descriere) throws ContNegasitException, FonduriInsuficienteException {
        Cont cont = cautaDupaIban(ibanVal);
        cont.retrage(suma);
        Tranzactie t = new Tranzactie(Tranzactie.TipTranzactie.RETRAGERE, suma, cont.getMoneda(), cont.getIban(), null, descriere);
        inregistreazaTranzactie(ibanVal, t);
        System.out.printf("[ContService] Retragere: -%.2f %s din contul %s%n", suma, cont.getMoneda(), cont.getIban().getFormatat());
        return t;
    }

    public void transfer(String ibanSursa, String ibanDestinatie, double suma, String descriere) throws ContNegasitException, FonduriInsuficienteException{
        Cont sursa = cautaDupaIban(ibanSursa);
        Cont destinatie = cautaDupaIban(ibanDestinatie);
        sursa.retrage(suma);
        destinatie.depune(suma);

        Tranzactie tTrimis = new Tranzactie(Tranzactie.TipTranzactie.TRANSFER_TRIMIS, suma, sursa.getMoneda(), sursa.getIban(), destinatie.getIban(), descriere);
        Tranzactie tPrimit = new Tranzactie(Tranzactie.TipTranzactie.TRANSFER_PRIMIT, suma, destinatie.getMoneda(), sursa.getIban(), destinatie.getIban(), descriere);
        inregistreazaTranzactie(ibanSursa, tTrimis);
        inregistreazaTranzactie(ibanDestinatie, tPrimit);
        System.out.printf("[ContService] Transfer: %.2f %s de la %s la %s%n", suma, sursa.getMoneda(), sursa.getIban().getFormatat(), destinatie.getIban().getFormatat());
    }

    public List<Tranzactie> getIstoric(String ibanVal) throws ContNegasitException {
        cautaDupaIban(ibanVal);
        String iban = ibanVal.replaceAll("\\s", "").toUpperCase();
        return Collections.unmodifiableList(istoricTranzactii.getOrDefault(iban, Collections.emptyList()));
    }

    public List<Cont> listeazaToate() {
        return new ArrayList<>(contIban.values());
    }

}