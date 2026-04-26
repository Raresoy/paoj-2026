# Aplicație Bancară — PAO Project

## 1.1 — Acțiuni / Interogări posibile în sistem

1. **Înregistrează un client nou** — adaugă un client (persoană fizică sau juridică) în sistem
2. **Deschide un cont bancar** — creează un cont curent sau de economii pentru un client
3. **Emite un card bancar** — asociază un card de debit sau credit unui cont existent
4. **Efectuează o tranzacție** — realizează transfer, depunere sau retragere de fonduri
5. **Generează un extras de cont** — listează toate tranzacțiile unui cont într-un interval de timp
6. **Caută client după CNP / CUI** — identifică un client în sistem după identificatorul unic
7. **Blochează / deblochează un card** — schimbă starea unui card bancar
8. **Verifică soldul unui cont** — returnează soldul curent al unui cont
9. **Listează toate conturile unui client** — afișează toate conturile asociate unui client
10. **Închide un cont bancar** — marchează un cont ca inactiv și realizează operațiunile aferente

## 1.2 — Tipuri de obiecte din domeniu

| Clasă | Descriere |
|-------|-----------|
| `Persoana` | Clasă abstractă de bază pentru orice persoană |
| `Client` | Persoană fizică, client al băncii |
| `AngajatBanca` | Angajat al băncii (extends Persoana) |
| `Cont` | Clasă abstractă pentru un cont bancar |
| `ContCurent` | Cont curent (extends Cont) |
| `ContEconomii` | Cont de economii cu dobândă (extends Cont) |
| `Card` | Card bancar (debit sau credit) asociat unui cont |
| `Tranzactie` | Înregistrare imutabilă a unei tranzacții |
| `ExtrasDecont` | Extras de cont pentru un interval de timp |
| `IBAN` | Identificator imutabil de cont bancar |

## 2. Structura proiectului

```
com.pao.project.aplicatie_bancara/
├── model/
│   ├── person/
│   │   ├── Persoana.java         ← clasă abstractă
│   │   ├── Client.java
│   │   └── AngajatBanca.java
│   ├── account/
│   │   ├── Cont.java             ← clasă abstractă
│   │   ├── ContCurent.java
│   │   ├── ContEconomii.java
│   │   └── IBAN.java             ← clasă imutabilă
│   ├── card/
│   │   └── Card.java
│   └── transaction/
│       ├── Tranzactie.java       ← clasă imutabilă
│       └── ExtrasDecont.java
├── service/
│   ├── ClientService.java        ← Singleton
│   ├── ContService.java          ← Singleton
│   ├── CardService.java          ← Singleton
│   └── TranzactieService.java    ← Singleton
├── exception/
│   ├── ContNegasitException.java
│   ├── FonduriInsuficienteException.java
│   ├── CardBlocatException.java
│   └── ClientNegasitException.java
└── Main.java
```
