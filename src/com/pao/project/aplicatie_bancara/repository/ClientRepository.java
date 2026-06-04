package com.pao.project.aplicatie_bancara.repository;

import com.pao.project.aplicatie_bancara.model.person.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository extends BaseRepository implements Repository<Client, String> {

    @Override
    public void save(Client client) throws SQLException {
        String sql = "INSERT INTO clienti (cnp, nume, prenume, email, telefon, adresa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, client.getCnp());
            ps.setString(2, client.getNume());
            ps.setString(3, client.getPrenume());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getTelefon());
            ps.setString(6, client.getAdresa());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Client> findById(String cnp) throws SQLException {
        String sql = "SELECT * FROM clienti WHERE cnp = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT * FROM clienti ORDER BY nume, prenume";
        List<Client> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) rezultat.add(mapRow(rs));
        }
        return rezultat;
    }

    @Override
    public void update(Client client) throws SQLException {
        String sql = "UPDATE clienti SET nume=?, prenume=?, email=?, telefon=?, adresa=? WHERE cnp=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, client.getNume());
            ps.setString(2, client.getPrenume());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getTelefon());
            ps.setString(5, client.getAdresa());
            ps.setString(6, client.getCnp());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String cnp) throws SQLException {
        String sql = "DELETE FROM clienti WHERE cnp = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            ps.executeUpdate();
        }
    }

    public List<Client> findByName(String fragment) throws SQLException {
        String sql = "SELECT * FROM clienti WHERE LOWER(CONCAT(prenume,' ',nume)) LIKE LOWER(?)";
        List<Client> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, "%" + fragment + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rezultat.add(mapRow(rs));
            }
        }
        return rezultat;
    }

    // JOIN 1: clienti cu numarul de conturi active
    public List<String> clientiCuNrConturi() throws SQLException {
        String sql = """
                SELECT c.cnp, c.prenume, c.nume, COUNT(ct.iban) AS nr_conturi
                FROM clienti c
                LEFT JOIN conturi ct ON ct.client_cnp = c.cnp AND ct.stare = 'ACTIV'
                GROUP BY c.cnp, c.prenume, c.nume
                ORDER BY nr_conturi DESC
                """;
        List<String> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultat.add(String.format("%-30s | CNP: %s | Conturi active: %d",
                        rs.getString("prenume") + " " + rs.getString("nume"),
                        rs.getString("cnp"),
                        rs.getInt("nr_conturi")));
            }
        }
        return rezultat;
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        return new Client(
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("cnp"),
                rs.getString("email"),
                rs.getString("telefon"),
                rs.getString("adresa")
        );
    }
}