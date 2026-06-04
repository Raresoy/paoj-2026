package com.pao.project.aplicatie_bancara.repository;

import com.pao.project.aplicatie_bancara.model.account.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContRepository extends BaseRepository implements Repository<Cont, String> {

    @Override
    public void save(Cont cont) throws SQLException {
        saveWithClient(cont, null);
    }

    public void saveWithClient(Cont cont, String clientCnp) throws SQLException {
        String sql = """
                INSERT INTO conturi
                    (iban, tip_cont, sold, moneda, stare, limita_descoperit, rata_dobanda, perioada_luni, client_cnp)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cont.getIban().getValoare());
            ps.setString(2, cont.getTipCont());
            ps.setDouble(3, cont.getSold());
            ps.setString(4, cont.getMoneda());
            ps.setString(5, cont.getStare().name());
            if (cont instanceof ContCurent cc) {
                ps.setDouble(6, cc.getLimitaDescoperit());
                ps.setDouble(7, 0.0);
                ps.setInt(8, 0);
            } else if (cont instanceof ContEconomii ce) {
                ps.setDouble(6, 0.0);
                ps.setDouble(7, ce.getRataDobanda());
                ps.setInt(8, ce.getPerioadaLunii());
            } else {
                ps.setDouble(6, 0.0); ps.setDouble(7, 0.0); ps.setInt(8, 0);
            }
            ps.setString(9, clientCnp);
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Cont> findById(String iban) throws SQLException {
        String sql = "SELECT * FROM conturi WHERE iban = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, iban);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Cont> findAll() throws SQLException {
        String sql = "SELECT * FROM conturi ORDER BY client_cnp, iban";
        List<Cont> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) rezultat.add(mapRow(rs));
        }
        return rezultat;
    }

    public List<Cont> findByClientCnp(String cnp) throws SQLException {
        String sql = "SELECT * FROM conturi WHERE client_cnp = ?";
        List<Cont> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rezultat.add(mapRow(rs));
            }
        }
        return rezultat;
    }

    @Override
    public void update(Cont cont) throws SQLException {
        String sql = "UPDATE conturi SET sold=?, stare=?, limita_descoperit=?, rata_dobanda=? WHERE iban=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setDouble(1, cont.getSold());
            ps.setString(2, cont.getStare().name());
            ps.setDouble(3, cont instanceof ContCurent cc ? cc.getLimitaDescoperit() : 0.0);
            ps.setDouble(4, cont instanceof ContEconomii ce ? ce.getRataDobanda() : 0.0);
            ps.setString(5, cont.getIban().getValoare());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String iban) throws SQLException {
        String sql = "DELETE FROM conturi WHERE iban = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, iban);
            ps.executeUpdate();
        }
    }

    // JOIN 2: conturi cu detalii proprietar, ordonate dupa sold
    public List<String> conturiCuDetaliiClient() throws SQLException {
        String sql = """
                SELECT ct.iban, ct.tip_cont, ct.sold, ct.moneda, ct.stare,
                       c.prenume, c.nume, c.email
                FROM conturi ct
                INNER JOIN clienti c ON c.cnp = ct.client_cnp
                ORDER BY ct.sold DESC
                """;
        List<String> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultat.add(String.format("[%s] %s — %s %.2f %s | Proprietar: %s %s",
                        rs.getString("tip_cont"), rs.getString("iban"),
                        rs.getString("stare"), rs.getDouble("sold"), rs.getString("moneda"),
                        rs.getString("prenume"), rs.getString("nume")));
            }
        }
        return rezultat;
    }

    // JOIN 3: conturi cu numarul total de tranzactii
    public List<String> conturiCuNrTranzactii() throws SQLException {
        String sql = """
                SELECT ct.iban, ct.tip_cont, ct.sold, ct.moneda,
                       c.prenume, c.nume,
                       COUNT(t.id_tranzactie) AS nr_tranzactii
                FROM conturi ct
                INNER JOIN clienti c ON c.cnp = ct.client_cnp
                LEFT JOIN tranzactii t ON t.iban_sursa = ct.iban OR t.iban_destinatie = ct.iban
                GROUP BY ct.iban, ct.tip_cont, ct.sold, ct.moneda, c.prenume, c.nume
                ORDER BY nr_tranzactii DESC
                """;
        List<String> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultat.add(String.format("%-34s | %s %s | %.2f %s | Tranzactii: %d",
                        rs.getString("iban"),
                        rs.getString("prenume"), rs.getString("nume"),
                        rs.getDouble("sold"), rs.getString("moneda"),
                        rs.getInt("nr_tranzactii")));
            }
        }
        return rezultat;
    }

    private Cont mapRow(ResultSet rs) throws SQLException {
        IBAN iban   = new IBAN(rs.getString("iban"));
        String tip  = rs.getString("tip_cont");
        double sold = rs.getDouble("sold");
        String mon  = rs.getString("moneda");
        Cont.Stare stare = Cont.Stare.valueOf(rs.getString("stare"));
        Cont cont;
        if ("ContCurent".equals(tip)) {
            cont = new ContCurent(iban, sold, mon, rs.getDouble("limita_descoperit"));
        } else {
            cont = new ContEconomii(iban, sold, mon, rs.getDouble("rata_dobanda"), rs.getInt("perioada_luni"));
        }
        cont.setStare(stare);
        return cont;
    }
}