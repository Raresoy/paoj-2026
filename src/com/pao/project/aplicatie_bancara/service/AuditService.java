package com.pao.project.aplicatie_bancara.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public class AuditService {

    private static AuditService instance;

    private static final String AUDIT_FILE = "audit.csv";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final ReentrantLock lock = new ReentrantLock();

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    // Fișierul se deschide în modul append la fiecare scriere — nu se suprascrie
    public void log(String numeActiune) {
        lock.lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.write(numeActiune + "," + LocalDateTime.now().format(FORMATTER));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("[AuditService] Eroare scriere audit.csv: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public static final String INREGISTREAZA_CLIENT    = "inregistreaza_client";
    public static final String DESCHIDE_CONT           = "deschide_cont";
    public static final String EMITE_CARD              = "emite_card";
    public static final String EFECTUEAZA_TRANZACTIE   = "efectueaza_tranzactie";
    public static final String GENEREAZA_EXTRAS        = "genereaza_extras";
    public static final String CAUTA_CLIENT            = "cauta_client";
    public static final String BLOCHEAZA_CARD          = "blocheaza_card";
    public static final String VERIFICA_SOLD           = "verifica_sold";
    public static final String LISTEAZA_CONTURI_CLIENT = "listeaza_conturi_client";
    public static final String INCHIDE_CONT            = "inchide_cont";
}