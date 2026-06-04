package com.pao.project.aplicatie_bancara.service;

import com.pao.project.aplicatie_bancara.exceptions.ClientNegasitException;
import com.pao.project.aplicatie_bancara.model.person.Client;
import com.pao.project.aplicatie_bancara.repository.ClientRepository;
import java.sql.SQLException;
import java.util.*;


public class ClientService {

    private static ClientService instance;

    private final Map<String, Client> clientiDupaCnp;
    private final TreeSet<Client>     clientiSortati;
    private final ClientRepository    repository;
    private final AuditService        audit;

    private ClientService() {
        this.clientiDupaCnp = new HashMap<>();
        this.clientiSortati = new TreeSet<>();
        this.repository     = new ClientRepository();
        this.audit          = AuditService.getInstance();
    }

    public static ClientService getInstance() {
        if (instance == null) instance = new ClientService();
        return instance;
    }

    public void adauga(Client client) {
        Objects.requireNonNull(client, "Clientul nu poate fi null");
        if (clientiDupaCnp.containsKey(client.getCnp()))
            throw new IllegalArgumentException("Exista deja un client cu CNP-ul: " + client.getCnp());
        clientiDupaCnp.put(client.getCnp(), client);
        clientiSortati.add(client);
        try { repository.save(client); } catch (SQLException e) {
            System.err.println("[ClientService] DB save esuat: " + e.getMessage());
        }
        audit.log(AuditService.INREGISTREAZA_CLIENT);
        System.out.println("[ClientService] Client inregistrat: " + client.getNumeComplet());
    }

    public void sterge(String cnp) throws ClientNegasitException {
        Client client = cautaDupaCnp(cnp);
        clientiDupaCnp.remove(cnp);
        clientiSortati.remove(client);
        try { repository.delete(cnp); } catch (SQLException e) {
            System.err.println("[ClientService] DB delete esuat: " + e.getMessage());
        }
        System.out.println("[ClientService] Client eliminat: " + client.getNumeComplet());
    }

    public Client cautaDupaCnp(String cnp) throws ClientNegasitException {
        if (cnp == null || cnp.isBlank())
            throw new IllegalArgumentException("CNP-ul nu poate fi null sau gol");
        audit.log(AuditService.CAUTA_CLIENT);
        Client client = clientiDupaCnp.get(cnp);
        if (client == null)
            throw new ClientNegasitException("Nu exista niciun client cu CNP-ul: " + cnp);
        return client;
    }

    public List<Client> cautaDupaNume(String fragment) {
        audit.log(AuditService.CAUTA_CLIENT);
        if (fragment == null || fragment.isBlank()) return new ArrayList<>(clientiSortati);
        String lower = fragment.toLowerCase();
        List<Client> rezultate = new ArrayList<>();
        for (Client c : clientiSortati)
            if (c.getNumeComplet().toLowerCase().contains(lower)) rezultate.add(c);
        return rezultate;
    }

    public List<Client> listeazaToti() { return new ArrayList<>(clientiSortati); }
    public int getNrClienti()          { return clientiDupaCnp.size(); }
}