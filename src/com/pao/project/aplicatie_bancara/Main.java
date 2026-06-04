package com.pao.project.aplicatie_bancara;

import com.pao.project.aplicatie_bancara.exceptions.*;
import com.pao.project.aplicatie_bancara.model.account.*;
import com.pao.project.aplicatie_bancara.model.card.Card;
import com.pao.project.aplicatie_bancara.model.person.*;
import com.pao.project.aplicatie_bancara.model.transaction.*;
import com.pao.project.aplicatie_bancara.service.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        ClientService clientService = ClientService.getInstance();
        ContService contService = ContService.getInstance();
        CardService cardService = CardService.getInstance();
        TranzactieService tranzactieService = TranzactieService.getInstance();

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║          SISTEM BANCAR — Demonstratie Completa           ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        
        System.out.println("━━━ ACTIUNEA 1: Inregistrare clienti noi ━━━");
        Client ion = new Client("Ionescu", "Ion", "1900101123456", "ion@email.ro", "0700111222", "Str. Florilor 1, Bucuresti");
        Client maria = new Client("Popescu", "Maria", "2850215654321", "maria@email.ro", "0700333444", "Bd. Unirii 5, Cluj");
        Client andrei = new Client("Dumitru", "Andrei", "1950320789012", "andrei@email.ro", "0700555666", "Str. Eminescu 12, Iasi");

        clientService.adauga(ion);
        clientService.adauga(maria);
        clientService.adauga(andrei);

        AngajatBanca angajat = new AngajatBanca("Constantin", "Elena", "elena@banca.ro",
                "0700999888", "EMP001", "Retail Banking", 5500.0);
        System.out.println("  Angajat: " + angajat + "\n");

        
        System.out.println("━━━ ACTIUNEA 2: Deschidere conturi bancare ━━━");
        IBAN iban1 = new IBAN("RO49AAAA1B31007593840000");
        IBAN iban2 = new IBAN("RO49AAAA1B31007593840001");
        IBAN iban3 = new IBAN("RO49AAAA1B31007593840002");

        ContCurent contIon = new ContCurent(iban1, 1000.0, "RON", 500.0);
        ContEconomii contMaria = new ContEconomii(iban2, 5000.0, "RON", 4.5, 12);
        ContCurent contAndrei = new ContCurent(iban3, 2000.0, "EUR");

        contService.deschideCont(ion, contIon);
        contService.deschideCont(maria, contMaria);
        contService.deschideCont(andrei, contAndrei);
        System.out.println("  Cont Ion: " + contIon);
        System.out.println("  Cont Maria: " + contMaria + "\n");

        
        System.out.println("━━━ ACTIUNEA 3: Emitere carduri bancare ━━━");
        Card cardIon = new Card("4539578763621486", Card.TipCard.DEBIT, iban1,
                ion.getNumeComplet(), LocalDate.of(2028, 12, 31), 3000.0, 10000.0);
        Card cardMaria = new Card("5425233430109903", Card.TipCard.CREDIT, iban2,
                maria.getNumeComplet(), LocalDate.of(2027, 6, 30), 5000.0, 20000.0);

        cardService.emiteCard(cardIon, contIon);
        cardService.emiteCard(cardMaria, contMaria);
        System.out.println("  Card Ion: " + cardIon);
        System.out.println("  Card Maria: " + cardMaria + "\n");

        
        System.out.println("━━━ ACTIUNEA 4: Efectuare tranzactii ━━━");
        try {
            contService.depune(iban1.getValoare(), 500.0, "Depunere numerar");
            contService.retrage(iban1.getValoare(), 200.0, "Retragere ATM");
            contService.transfer(iban1.getValoare(), iban2.getValoare(), 300.0, "Imprumut prietenului");
            System.out.println("  Sold dupa tranzactii - Ion: " + String.format("%.2f RON", contIon.getSold()));
            System.out.println("  Sold dupa tranzactii - Maria: " + String.format("%.2f RON", contMaria.getSold()) + "\n");
        } catch (ContNegasitException | FonduriInsuficienteException e) {
            System.out.println("  EROARE tranzactie: " + e.getMessage());
        }

        
        System.out.println("━━━ ACTIUNEA 5: Generare extras de cont ━━━");
        try {
            ExtrasDeCont extras = tranzactieService.genereazaExtras(
                    iban1.getValoare(),
                    LocalDate.now().minusDays(30),
                    LocalDate.now());
            extras.afiseaza();
            System.out.println();
        } catch (ContNegasitException e) {
            System.out.println("  EROARE: " + e.getMessage());
        }

        
        System.out.println("━━━ ACTIUNEA 6: Cautare client dupa CNP ━━━");
        try {
            Client gasit = clientService.cautaDupaCnp("1900101123456");
            System.out.println("  Client gasit: " + gasit);
            System.out.println("  Conturi: " + gasit.getConturi().size() + " cont(uri)\n");
        } catch (ClientNegasitException e) {
            System.out.println("  EROARE: " + e.getMessage());
        }

        
        try {
            clientService.cautaDupaCnp("9999999999999");
        } catch (ClientNegasitException e) {
            System.out.println("  [Exceptie tratata] " + e.getMessage() + "\n");
        }

        
        System.out.println("━━━ ACTIUNEA 7: Blocare / deblocare card ━━━");
        try {
            cardService.blocheazaCard("4539578763621486");
            System.out.println("  Stare card Ion: " + cardIon.getStare());

            
            cardService.blocheazaCard("4539578763621486");
        } catch (CardBlocatException e) {
            System.out.println("  [Exceptie tratata] " + e.getMessage());
        }

        cardService.deblocheazaCard("4539578763621486");
        System.out.println("  Stare card Ion dupa deblocare: " + cardIon.getStare() + "\n");

        
        System.out.println("━━━ ACTIUNEA 8: Verificare sold conturi ━━━");
        try {
            Cont contVizualizat = contService.cautaDupaIban(iban1.getValoare());
            System.out.printf("  Sold %s: %.2f %s%n",
                    contVizualizat.getIban().getFormatat(),
                    contVizualizat.getSold(),
                    contVizualizat.getMoneda());

            
            contService.retrage(iban1.getValoare(), 99999.0, "Test fonduri");
        } catch (ContNegasitException e) {
            System.out.println("  EROARE cont: " + e.getMessage());
        } catch (FonduriInsuficienteException e) {
            System.out.println("  [Exceptie tratata] " + e.getMessage() + "\n");
        }

        System.out.println("━━━ ACTIUNEA 9: Listare conturi client ━━━");
        System.out.println("  Conturi ale lui " + ion.getNumeComplet() + ":");
        for (Cont c : ion.getConturi()) {
            System.out.println("    • " + c);
        }

        System.out.println("\n  Toti clientii (sortati alfabetic):");
        for (Client c : clientService.listeazaToti()) {
            System.out.println("    " + c);
        }
        System.out.println();

        
        try {
            Map<Tranzactie.TipTranzactie, List<Tranzactie>> grupate =
                    tranzactieService.grupeazaDupaTip(iban1.getValoare());
            System.out.println("  Tranzactii grupate pe tip pentru " + iban1.getFormatat() + ":");
            grupate.forEach((tip, lista) ->
                    System.out.println("    " + tip + ": " + lista.size() + " tranzactie(i)"));
            System.out.println();
        } catch (ContNegasitException e) {
            System.out.println("  EROARE: " + e.getMessage());
        }

        
        System.out.println("━━━ ACTIUNEA 10: Inchidere cont bancar ━━━");
        try {
            System.out.println("  Stare inainte: " + contAndrei.getStare());
            contService.inchideCont(iban3.getValoare());
            System.out.println("  Stare dupa: " + contAndrei.getStare());

            
            contService.depune(iban3.getValoare(), 100.0, "Test cont inchis");
        } catch (ContNegasitException e) {
            System.out.println("  EROARE: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("  [Exceptie tratata] Cont inactiv: " + e.getMessage());
        }

        
        System.out.println("\n━━━ BONUS: Aplicare dobanda cont economii ━━━");
        System.out.printf("  Sold inainte: %.2f RON%n", contMaria.getSold());
        contMaria.aplicaDobanda();
        System.out.printf("  Sold dupa:    %.2f RON%n", contMaria.getSold());

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║        Demonstratie finalizata cu succes!                ║");
        System.out.printf("║  Clienti: %-3d  Conturi: %-3d  Carduri: %-3d             ║%n",
                clientService.getNrClienti(),
                contService.listeazaToate().size(),
                cardService.getNrCarduri());
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }
}
