package com.pao.project.aplicatie_bancara.repository;

import com.pao.project.aplicatie_bancara.model.account.IBAN;
import com.pao.project.aplicatie_bancara.model.transaction.Tranzactie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TranzactieRepository extends BaseRepository implements Repository<Tranzactie, String> {

    @Override
    public void save(Tranzactie t) throws SQLException {
        String sql = """
                INSERT INTO tranzactii
                    (id_tranzactie, tip, suma, moneda, iban_sursa, iban_destinatie, data_ora, descriere)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, t.getIdTranzactie());
            ps.setString(2, t.getTip().name());
            ps.setDouble(3, t.getSuma());
            ps.setString(4, t.getMoneda());
            ps.setString(5, t.getIbanSursa()      != null ? t.getIbanSursa().getValoare()      : null);
            ps.setString(6, t.getIbanDestinatie() != null ? t.getIbanDestinatie().getValoare() : null);
            ps.setTimestamp(7, Timestamp.valueOf(t.getDataOra()));
            ps.setString(8, t.getDescriere());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Tranzactie> findById(String id) throws SQLException {
        String sql = "SELECT * FROM tranzactii WHERE id_tranzactie = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Tranzactie> findAll() throws SQLException {
        String sql = "SELECT * FROM tranzactii ORDER BY data_ora DESC";
        List<Tranzactie> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) rezultat.add(mapRow(rs));
        }
        return rezultat;
    }

    public List<Tranzactie> findByIban(String iban) throws SQLException {
        String sql = "SELECT * FROM tranzactii WHERE iban_sursa = ? OR iban_destinatie = ? ORDER BY data_ora DESC";
        List<Tranzactie> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, iban);
            ps.setString(2, iban);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rezultat.add(mapRow(rs));
            }
        }
        return rezultat;
    }

    @Override
    public void update(Tranzactie t) {
        throw new UnsupportedOperationException("Tranzactiile sunt imutabile si nu pot fi modificate");
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM tranzactii WHERE id_tranzactie = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    // JOIN 4: transferuri cu detalii expeditor si beneficiar
    public List<String> transferuriCuDetaliiConturi() throws SQLException {
        String sql = """
                SELECT t.id_tranzactie, t.suma, t.moneda, t.data_ora, t.descriere,
                       cl_s.prenume AS pren_sursa, cl_s.nume AS nume_sursa,
                       cl_d.prenume AS pren_dest,  cl_d.nume AS nume_dest
                FROM tranzactii t
                INNER JOIN conturi cs   ON cs.iban  = t.iban_sursa
                INNER JOIN clienti cl_s ON cl_s.cnp = cs.client_cnp
                INNER JOIN conturi cd   ON cd.iban  = t.iban_destinatie
                INNER JOIN clienti cl_d ON cl_d.cnp = cd.client_cnp
                WHERE t.tip = 'TRANSFER_TRIMIS'
                ORDER BY t.data_ora DESC
                """;
        List<String> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultat.add(String.format("[%s] %.2f %s | %s %s -> %s %s | %s",
                        rs.getString("id_tranzactie"),
                        rs.getDouble("suma"), rs.getString("moneda"),
                        rs.getString("pren_sursa"), rs.getString("nume_sursa"),
                        rs.getString("pren_dest"),  rs.getString("nume_dest"),
                        rs.getString("descriere")));
            }
        }
        return rezultat;
    }

    // JOIN 5: istoricul complet al unui client pe toate conturile sale
    public List<String> istoricCompletClient(String cnp) throws SQLException {
        String sql = """
                SELECT t.id_tranzactie, t.tip, t.suma, t.moneda, t.data_ora, t.descriere,
                       ct.iban, ct.tip_cont
                FROM tranzactii t
                JOIN conturi ct ON ct.iban = t.iban_sursa OR ct.iban = t.iban_destinatie
                JOIN clienti c  ON c.cnp = ct.client_cnp
                WHERE c.cnp = ?
                ORDER BY t.data_ora DESC
                """;
        List<String> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rezultat.add(String.format("[%s] %s %.2f %s | %s | %s",
                            rs.getString("id_tranzactie"),
                            rs.getString("tip"),
                            rs.getDouble("suma"), rs.getString("moneda"),
                            rs.getString("iban"),
                            rs.getString("descriere")));
                }
            }
        }
        return rezultat;
    }

    private Tranzactie mapRow(ResultSet rs) throws SQLException {
        String s = rs.getString("iban_sursa");
        String d = rs.getString("iban_destinatie");
        return new Tranzactie(
                Tranzactie.TipTranzactie.valueOf(rs.getString("tip")),
                rs.getDouble("suma"),
                rs.getString("moneda"),
                s != null ? new IBAN(s) : null,
                d != null ? new IBAN(d) : null,
                rs.getString("descriere")
        );
    }
}