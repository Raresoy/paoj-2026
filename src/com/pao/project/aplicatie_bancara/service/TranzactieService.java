package com.pao.project.aplicatie_bancara.service;

import com.pao.project.aplicatie_bancara.exceptions.CardBlocatException;
import com.pao.project.aplicatie_bancara.exceptions.ContNegasitException;
import com.pao.project.aplicatie_bancara.exceptions.FonduriInsuficienteException;
import com.pao.project.aplicatie_bancara.model.account.Cont;
import com.pao.project.aplicatie_bancara.model.card.Card;
import com.pao.project.aplicatie_bancara.model.transaction.ExtrasDeCont;
import com.pao.project.aplicatie_bancara.model.transaction.Tranzactie;
import java.time.LocalDate;
import java.util.*;

public class TranzactieService {
    private static TranzactieService instance;
    private final ContService contService;
    private final CardService cardService;

    private TranzactieService() {
        this.contService = ContService.getInstance();
        this.cardService = CardService.getInstance();
    }

    public static TranzactieService getInstance() {
        if(instance == null)
            instance = new TranzactieService();
        return instance;
    } 

    public Tranzactie plataCuCard(String numarCard, double suma, String descriere) throws CardBlocatException, ContNegasitException, FonduriInsuficienteException {
        cardService.verificaCardActiv(numarCard);
        Card card = cardService.cautaDupaNr(numarCard);

        String ibanVal = card.getIbanContAsociat().getValoare();
        Cont cont = contService.cautaDupaIban(ibanVal);
        cont.retrage(suma);

        Tranzactie t = new Tranzactie(Tranzactie.TipTranzactie.PLATA_CARD, suma, cont.getMoneda(), cont.getIban(), null, "Plata card " + descriere);
        contService.getIstoric(ibanVal);
        System.out.printf("[TranzactieService] Plata card -%.2f %s cu cardul %s (%s)%n", suma, cont.getMoneda(), card.getNumarMascat(), descriere);
        return t;
    }

    public ExtrasDeCont genereazaExtras(String ibanVal, LocalDate dataInceput, LocalDate dataSfarsit) throws ContNegasitException {
        Cont cont = contService.cautaDupaIban(ibanVal);
        List<Tranzactie> tranzactii = contService.getIstoric(ibanVal);
        double soldInitialEstimat = cont.getSold();

        ExtrasDeCont extras = new ExtrasDeCont(cont.getIban(), dataInceput, dataSfarsit, soldInitialEstimat, tranzactii);
        System.out.println("[TranzactieService] Extras generat pentru " + cont.getIban().getFormatat());
        return extras;
    }

    public double calculeazaRulajTotal(String ibanVal) throws ContNegasitException {
        List<Tranzactie> tranazctii = contService.getIstoric(ibanVal);
        return tranazctii.stream().mapToDouble(Tranzactie::getSuma).sum();
    }

    public Map<Tranzactie.TipTranzactie, List<Tranzactie>> grupeazaDupaTip(String ibanVal) throws ContNegasitException {
        List<Tranzactie> tranzactii = contService.getIstoric(ibanVal);
        Map<Tranzactie.TipTranzactie, List<Tranzactie>> grupate = new EnumMap<>(Tranzactie.TipTranzactie.class);
        for(Tranzactie t : tranzactii) {
            grupate.computeIfAbsent(t.getTip(), k -> new ArrayList<>()).add(t);
        }
        return grupate;
    }
}