package com.pao.project.aplicatie_bancara.repository;

import com.pao.project.aplicatie_bancara.model.account.IBAN;
import com.pao.project.aplicatie_bancara.model.card.Card;
import com.pao.project.aplicatie_bancara.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository extends BaseRepository implements Repository<Card, String> {


    @Override
    public void save(Card card) throws SQLException {
        String sql = """
                INSERT INTO carduri
                    (numar_card, tip_card, iban_cont, nume_detinutor, data_expirare,
                     stare, limita_zilnica, limita_lunara)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, card.getNumarCard());
            ps.setString(2, card.getTipCard().name());
            ps.setString(3, card.getIbanContAsociat().getValoare());
            ps.setString(4, card.getNumeDetinutor());
            ps.setDate(5, Date.valueOf(card.getDataExpirare()));
            ps.setString(6, card.getStare().name());
            ps.setDouble(7, card.getLimitaZilnica());
            ps.setDouble(8, card.getLimitaLunara());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Card> findById(String numarCard) throws SQLException {
        String sql = "SELECT * FROM carduri WHERE numar_card = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, numarCard);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Card> findAll() throws SQLException {
        String sql = "SELECT * FROM carduri ORDER BY iban_cont";
        List<Card> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultat.add(mapRow(rs));
            }
        }
        return rezultat;
    }

    public List<Card> findByIban(String iban) throws SQLException {
        String sql = "SELECT * FROM carduri WHERE iban_cont = ?";
        List<Card> rezultat = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, iban);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rezultat.add(mapRow(rs));
                }
            }
        }
        return rezultat;
    }

    @Override
    public void update(Card card) throws SQLException {
        String sql = "UPDATE carduri SET stare=?, limita_zilnica=?, limita_lunara=? WHERE numar_card=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, card.getStare().name());
            ps.setDouble(2, card.getLimitaZilnica());
            ps.setDouble(3, card.getLimitaLunara());
            ps.setString(4, card.getNumarCard());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String numarCard) throws SQLException {
        String sql = "DELETE FROM carduri WHERE numar_card = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, numarCard);
            ps.executeUpdate();
        }
    }

    private Card mapRow(ResultSet rs) throws SQLException {
        return new Card(
                rs.getString("numar_card"),
                Card.TipCard.valueOf(rs.getString("tip_card")),
                new IBAN(rs.getString("iban_cont")),
                rs.getString("nume_detinutor"),
                rs.getDate("data_expirare").toLocalDate(),
                rs.getDouble("limita_zilnica"),
                rs.getDouble("limita_lunara")
        );
    }
}
