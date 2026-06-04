DROP TABLE IF EXISTS tranzactii;
DROP TABLE IF EXISTS carduri;
DROP TABLE IF EXISTS conturi;
DROP TABLE IF EXISTS clienti;

CREATE TABLE clienti (
    cnp         VARCHAR(13)  PRIMARY KEY,
    nume        VARCHAR(100) NOT NULL,
    prenume     VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    telefon     VARCHAR(20),
    adresa      VARCHAR(255)
);

CREATE TABLE conturi (
    iban              VARCHAR(34)   PRIMARY KEY,
    tip_cont          VARCHAR(20)   NOT NULL,
    sold              DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    moneda            VARCHAR(3)    NOT NULL DEFAULT 'RON',
    stare             VARCHAR(10)   NOT NULL DEFAULT 'ACTIV',
    limita_descoperit DECIMAL(15,2) DEFAULT 0.00,
    rata_dobanda      DECIMAL(5,2)  DEFAULT 0.00,
    perioada_luni     INT           DEFAULT 0,
    client_cnp        VARCHAR(13)   NOT NULL,
    CONSTRAINT fk_cont_client FOREIGN KEY (client_cnp) REFERENCES clienti(cnp)
        ON DELETE CASCADE
);

CREATE TABLE carduri (
    numar_card     VARCHAR(20)   PRIMARY KEY,
    tip_card       VARCHAR(10)   NOT NULL,
    iban_cont      VARCHAR(34)   NOT NULL,
    nume_detinutor VARCHAR(200)  NOT NULL,
    data_expirare  DATE          NOT NULL,
    stare          VARCHAR(10)   NOT NULL DEFAULT 'ACTIV',
    limita_zilnica DECIMAL(15,2) DEFAULT 0.00,
    limita_lunara  DECIMAL(15,2) DEFAULT 0.00,
    CONSTRAINT fk_card_cont FOREIGN KEY (iban_cont) REFERENCES conturi(iban)
        ON DELETE CASCADE
);

CREATE TABLE tranzactii (
    id_tranzactie   VARCHAR(8)    PRIMARY KEY,
    tip             VARCHAR(20)   NOT NULL,
    suma            DECIMAL(15,2) NOT NULL,
    moneda          VARCHAR(3)    NOT NULL DEFAULT 'RON',
    iban_sursa      VARCHAR(34),
    iban_destinatie VARCHAR(34),
    data_ora        DATETIME      NOT NULL,
    descriere       VARCHAR(500),
    CONSTRAINT fk_tranzactie_sursa
        FOREIGN KEY (iban_sursa) REFERENCES conturi(iban) ON DELETE SET NULL,
    CONSTRAINT fk_tranzactie_dest
        FOREIGN KEY (iban_destinatie) REFERENCES conturi(iban) ON DELETE SET NULL
);