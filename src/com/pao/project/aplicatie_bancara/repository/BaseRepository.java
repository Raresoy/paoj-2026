package com.pao.project.aplicatie_bancara.repository;

import com.pao.project.aplicatie_bancara.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseRepository {

    /**
     * Returnează conexiunea activă.
     * @throws SQLException dacă BD nu este disponibilă
     */
    protected Connection getConn() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null) {
            throw new SQLException("Baza de date nu este disponibila");
        }
        return conn;
    }

    protected boolean isDbAvailable() {
        return DatabaseConnection.getInstance().isAvailable();
    }
}
