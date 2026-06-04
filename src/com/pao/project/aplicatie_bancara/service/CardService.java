package com.pao.project.aplicatie_bancara.service;

import com.pao.project.aplicatie_bancara.exceptions.CardBlocatException;
import com.pao.project.aplicatie_bancara.model.account.Cont;
import com.pao.project.aplicatie_bancara.model.card.Card;
import java.util.*;

public class CardService {
    private static CardService instance;
    private final Map<String, Card> cardNr;

    private CardService() {
        this.cardNr = new LinkedHashMap<>();
    }

    public static CardService getInstance() {
        if(instance == null) 
            instance = new CardService();
        return instance;
    }

    public void emiteCard(Card card, Cont cont) {
        Objects.requireNonNull(card);
        Objects.requireNonNull(cont);
        if(cardNr.containsKey(card.getNumarCard()))
            throw new IllegalArgumentException("Acest card exista deja");
        cardNr.put(card.getNumarCard(), card);
        cont.adaugaCard(card);
        System.out.println("[CardService] Card emis: " + card.getNumarMascat() + " pentru contul " + cont.getIban().getFormatat());
    }

    public Card cautaDupaNr(String numarCard){
        if(numarCard == null || numarCard.isBlank())
            throw new IllegalArgumentException("Numarul cardului nu poate fi gol");
        Card card = cardNr.get(numarCard);
        if(card == null)
            throw new NoSuchElementException("Nu exista card cu acest numar");
        return card;
    }

    public void blocheazaCard(String numarCard) throws CardBlocatException {
        Card card = cautaDupaNr(numarCard);
        if(card.getStare() == Card.StareCard.BLOCAT)
            throw new CardBlocatException("Cardul este deja blocat");
        card.setStare(Card.StareCard.BLOCAT);
        System.out.println("[CardService] Card blocat: " + card.getNumarMascat());
    }

    public void deblocheazaCard(String numarCard) {
        Card card = cautaDupaNr(numarCard);
        if(card == null) {
            System.out.println("Card negasit");
            return;
        }
        card.setStare(Card.StareCard.ACTIV);
        System.out.println("[CardService] Card deblocat: " + card.getNumarMascat());
    }

    public void verificaCardActiv(String numarCard) throws CardBlocatException {
        Card card = cautaDupaNr(numarCard);
        if(!card.esteActiv()) {
            throw new CardBlocatException("Cardul nu este activ");
        }
    }

    public List<Card> listeazaToate() {
        return new ArrayList<>(cardNr.values());
    }

    public void sterge(String numarCard) {
        Card card = cardNr.remove(numarCard);
        if(card != null) {
            card.setStare(Card.StareCard.BLOCAT);
            System.out.println("[CardService] Card dezactivat: " + card.getNumarMascat());
        }
    }

    public int getNrCarduri() {
        return cardNr.size();
    }
}