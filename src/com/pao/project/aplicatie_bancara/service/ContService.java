package com.pao.project.aplicatie_bancara.service;

import com.pao.project.aplicatie_bancara.exceptions.ContNegasitException;
import com.pao.project.aplicatie_bancara.exceptions.FonduriInsuficienteException;
import com.pao.project.aplicatie_bancara.model.account.Cont;
import com.pao.project.aplicatie_bancara.model.person.Client;
import com.pao.project.aplicatie_bancara.model.transaction.Tranzactie;
import com.pao.project.aplicatie_bancara.repository.ContRepository;
import com.pao.project.aplicatie_bancara.repository.TranzactieRepository;
import com.pao.project.aplicatie_bancara.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ContService {

    private static ContService instance;

    private final Map<String, Cont>             conturiDupaIban;
    private final Map<String, List<Tranzactie>> istoricTranzactii;
    private final ContRepository                contRepo;
    private final TranzactieRepository          tranzactieRepo;
    private final AuditService                  audit;

    private ContService() {
        this.conturiDupaIban   = new LinkedHashMap<>();
        this.istoricTranzactii = new HashMap<>();
        this.contRepo          = new ContRepository();
        this.tranzactieRepo    = new TranzactieRepository();
        this.audit             = AuditService.getInstance();
    }

    public static ContService getInstance() {
        if (instance == null) instance = new ContService();
        return instance;
    }

    public void deschideCont(Client client, Cont cont) {
        Objects.requireNonNull(client); Objects.requireNonNull(cont);
        String ibanVal = cont.getIban().getValoare();
        if (conturiDupaIban.containsKey(ibanVal))
            throw new IllegalArgumentException("Contul cu IBAN-ul " + ibanVal + " exista deja");
        conturiDupaIban.put(ibanVal, cont);
        istoricTranzactii.put(ibanVal, new ArrayList<>());
        client.adaugaCont(cont);
        try { contRepo.saveWithClient(cont, client.getCnp()); } catch (SQLException e) {
            System.err.println("[ContService] DB save esuat: " + e.getMessage());
        }
        audit.log(AuditService.DESCHIDE_CONT);
        System.out.println("[ContService] Cont deschis: " + cont.getIban().getFormatat() + " pentru " + client.getNumeComplet());
    }

    public void inchideCont(String ibanVal) throws ContNegasitException {
        Cont cont = cautaDupaIban(ibanVal);
        cont.setStare(Cont.Stare.INACTIV);
        try { contRepo.update(cont); } catch (SQLException e) {
            System.err.println("[ContService] DB update esuat: " + e.getMessage());
        }
        audit.log(AuditService.INCHIDE_CONT);
        System.out.println("[ContService] Cont inchis: " + cont.getIban().getFormatat());
    }

    public Cont cautaDupaIban(String ibanVal) throws ContNegasitException {
        if (ibanVal == null || ibanVal.isBlank())
            throw new IllegalArgumentException("IBAN-ul nu poate fi null sau gol");
        audit.log(AuditService.VERIFICA_SOLD);
        Cont cont = conturiDupaIban.get(ibanVal.replaceAll("\\s", "").toUpperCase());
        if (cont == null) throw new ContNegasitException("Nu exista niciun cont cu IBAN-ul: " + ibanVal);
        return cont;
    }

    public Tranzactie depune(String ibanVal, double suma, String descriere) throws ContNegasitException {
        Cont cont = cautaDupaIban(ibanVal);
        cont.depune(suma);
        Tranzactie t = new Tranzactie(Tranzactie.TipTranzactie.DEPUNERE, suma,
                cont.getMoneda(), null, cont.getIban(), descriere);
        inregistreazaTranzactie(ibanVal, t);
        try { contRepo.update(cont); tranzactieRepo.save(t); } catch (SQLException e) {
            System.err.println("[ContService] DB depune esuat: " + e.getMessage());
        }
        audit.log(AuditService.EFECTUEAZA_TRANZACTIE);
        System.out.printf("[ContService] Depunere: +%.2f %s in contul %s%n", suma, cont.getMoneda(), cont.getIban().getFormatat());
        return t;
    }

    public Tranzactie retrage(String ibanVal, double suma, String descriere)
            throws ContNegasitException, FonduriInsuficienteException {
        Cont cont = cautaDupaIban(ibanVal);
        cont.retrage(suma);
        Tranzactie t = new Tranzactie(Tranzactie.TipTranzactie.RETRAGERE, suma,
                cont.getMoneda(), cont.getIban(), null, descriere);
        inregistreazaTranzactie(ibanVal, t);
        try { contRepo.update(cont); tranzactieRepo.save(t); } catch (SQLException e) {
            System.err.println("[ContService] DB retrage esuat: " + e.getMessage());
        }
        audit.log(AuditService.EFECTUEAZA_TRANZACTIE);
        System.out.printf("[ContService] Retragere: -%.2f %s din contul %s%n", suma, cont.getMoneda(), cont.getIban().getFormatat());
        return t;
    }

    // Transfer atomic — tranzactie JDBC explicita cu commit/rollback
    public void transfer(String ibanSursa, String ibanDest, double suma, String descriere)
            throws ContNegasitException, FonduriInsuficienteException {
        Cont sursa = cautaDupaIban(ibanSursa);
        Cont dest  = cautaDupaIban(ibanDest);
        sursa.retrage(suma);
        dest.depune(suma);
        Tranzactie tTrimis = new Tranzactie(Tranzactie.TipTranzactie.TRANSFER_TRIMIS, suma,
                sursa.getMoneda(), sursa.getIban(), dest.getIban(), descriere);
        Tranzactie tPrimit = new Tranzactie(Tranzactie.TipTranzactie.TRANSFER_PRIMIT, suma,
                dest.getMoneda(), sursa.getIban(), dest.getIban(), descriere);
        inregistreazaTranzactie(ibanSursa, tTrimis);
        inregistreazaTranzactie(ibanDest, tPrimit);

        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
                contRepo.update(sursa);
                contRepo.update(dest);
                tranzactieRepo.save(tTrimis);
                tranzactieRepo.save(tPrimit);
                conn.commit();
            } catch (SQLException e) {
                try { conn.rollback(); } catch (SQLException ex) {
                    System.err.println("[ContService] Rollback esuat: " + ex.getMessage());
                }
                try { sursa.depune(suma); dest.retrage(suma); } catch (Exception ignored) {}
                throw new RuntimeException("Transfer esuat, rollback executat: " + e.getMessage(), e);
            } finally {
                try { conn.setAutoCommit(true); } catch (SQLException e) {
                    System.err.println("[ContService] setAutoCommit esuat: " + e.getMessage());
                }
            }
        } else {
            System.err.println("[ContService] Transfer persistat doar in memorie (BD indisponibila)");
        }
        audit.log(AuditService.EFECTUEAZA_TRANZACTIE);
        System.out.printf("[ContService] Transfer: %.2f %s de la %s la %s%n",
                suma, sursa.getMoneda(), sursa.getIban().getFormatat(), dest.getIban().getFormatat());
    }

    public List<Tranzactie> getIstoric(String ibanVal) throws ContNegasitException {
        cautaDupaIban(ibanVal);
        String iban = ibanVal.replaceAll("\\s", "").toUpperCase();
        return Collections.unmodifiableList(istoricTranzactii.getOrDefault(iban, Collections.emptyList()));
    }

    public List<Cont> listeazaToate() { return new ArrayList<>(conturiDupaIban.values()); }

    public List<String> listeazaConturiCuTranzactii() throws SQLException {
        return contRepo.conturiCuNrTranzactii();
    }

    private void inregistreazaTranzactie(String ibanVal, Tranzactie t) {
        String iban = ibanVal.replaceAll("\\s", "").toUpperCase();
        istoricTranzactii.computeIfAbsent(iban, k -> new ArrayList<>()).add(t);
    }
}