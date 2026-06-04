package com.pao.project.aplicatie_bancara.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    private boolean available = false;

    private static final String PROPERTIES_FILE = "/db.properties";

    private DatabaseConnection() {
        try {
            Properties props = new Properties();
            InputStream is = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE);
            if (is == null) {
                System.err.println("[DatabaseConnection] " + PROPERTIES_FILE + " negasit in classpath — BD dezactivata");
                return;
            }
            props.load(is);

            String url      = props.getProperty("db.url");
            String user     = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            this.connection = DriverManager.getConnection(url, user, password);
            this.available  = true;
            System.out.println("[DatabaseConnection] Conexiune stabilita la: " + url);
        } catch (IOException e) {
            System.err.println("[DatabaseConnection] Eroare citire properties: " + e.getMessage() + " — BD dezactivata");
        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] BD indisponibila (" + e.getMessage() + ") — se continua fara persistenta");
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        if (!available) return null;
        try {
            if (connection == null || connection.isClosed()) {
                instance  = new DatabaseConnection();
                return instance.connection;
            }
        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] Eroare la verificarea conexiunii: " + e.getMessage());
            return null;
        }
        return connection;
    }

    public boolean isAvailable() { return available; }

    public void closeConnection() {
        if (connection != null) {
            try { connection.close(); System.out.println("[DatabaseConnection] Conexiune inchisa."); }
            catch (SQLException e) { System.err.println("[DatabaseConnection] Eroare la inchidere: " + e.getMessage()); }
        }
    }
}
