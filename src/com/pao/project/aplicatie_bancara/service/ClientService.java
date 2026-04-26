package com.pao.project.aplicatie_bancara.service;

import com.pao.project.aplicatie_bancara.exceptions.ClientNegasitException;
import com.pao.project.aplicatie_bancara.model.person.Client;
import java.util.*;

public class ClientService {
    private static ClientService instance;
    private final Map<String, Client> clientiCnp;
    private final TreeSet<Client> clientiSortati;

    private ClientService() {
        this.clientiCnp = new HashMap<>();
        this.clientiSortati = new TreeSet<>();
    }

    public static ClientService getInstance() {
        if(instance == null)
            instance = new ClientService();
        return instance;
    }

    public void adauga(Client client) {
        Objects.requireNonNull(client);
        if(clientiCnp.containsKey(client.getCnp())){
            throw new IllegalArgumentException("Exista deja un client cu acest CNP");
        }
        clientiCnp.put(client.getCnp(), client);
        clientiSortati.add(client);
        System.out.println("[ClientService] Client inregistrat: " + client.getNumeComplet());
    }

    public Client cautaDupaCnp(String cnp) throws ClientNegasitException {
        if(cnp == null || cnp.isBlank())
            throw new IllegalArgumentException("CNP-ul nu poate fi gol");
        Client client = clientiCnp.get(cnp);
        if(client == null)
            throw new ClientNegasitException("Nu exista niciun client cu acest CNP");
        return client;
    }

    public List<Client> cautaDupaNume(String fragment) {
        if(fragment == null || fragment.isBlank())
            return new ArrayList<>(clientiSortati);
        String lower = fragment.toLowerCase();
        List<Client> rezultate = new ArrayList<>();
        for(Client c : clientiSortati) {
            if(c.getNumeComplet().toLowerCase().contains(lower))
                rezultate.add(c);
        }
        return rezultate;
    }

    public void sterge(String cnp) throws ClientNegasitException {
        Client client = cautaDupaCnp(cnp);
        clientiCnp.remove(cnp);
        clientiSortati.remove(client);
        System.out.println("[ClientService] Client eliminat: " + client.getNumeComplet());
    }

    public List<Client> listeazaTot() {
        return new ArrayList<>(clientiSortati);
    }

    public int getNrClienti() {
        return clientiCnp.size();
    }
}