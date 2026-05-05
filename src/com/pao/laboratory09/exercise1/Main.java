package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

/*
3
101 150.00 2024-05-10 RO01SRCA RO01DSTB CREDIT
102 2300.50 2024-05-15 RO02SRCC RO02DSTD DEBIT
103 45.00 2024-06-01 RO03SRCE RO03DSTF CREDIT
LIST
FILTER 2024-05
FILTER 2024-07
NOTE 101
NOTE 999 */

public class Main {
    private static final String FILE_PATH = "/home/raresoi/paoj-2026/src/com/pao/laboratory09/exercise2/output/lab09_ex1.ser";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        new File("output").mkdirs();

        if (!sc.hasNextInt()) return;
        int n = sc.nextInt();
        List<Tranzactie> tranzactiiInitiale = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int id = sc.nextInt();
            double suma = sc.nextDouble();
            String data = sc.next();
            String sursa = sc.next();
            String destinatie = sc.next();
            TipTranzactie tip = TipTranzactie.valueOf(sc.next());

            Tranzactie t = new Tranzactie(id, suma, data, sursa, destinatie, tip);
            t.setNote("procesat");
            tranzactiiInitiale.add(t);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(tranzactiiInitiale);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        List<Tranzactie> tranzactiiRecuperate = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            tranzactiiRecuperate = (List<Tranzactie>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (tranzactiiRecuperate != null) {
            while (sc.hasNext()) {
                String comanda = sc.next();
                switch (comanda) {
                    case "LIST":
                        tranzactiiRecuperate.forEach(System.out::println);
                        break;

                    case "FILTER":
                        String prefix = sc.next();
                        boolean gasit = false;
                        for (Tranzactie t : tranzactiiRecuperate) {
                            if (t.getData().startsWith(prefix)) {
                                System.out.println(t);
                                gasit = true;
                            }
                        }
                        if (!gasit) System.out.println("Niciun rezultat.");
                        break;

                    case "NOTE":
                        int searchId = sc.nextInt();
                        Tranzactie gasita = null;
                        for (Tranzactie t : tranzactiiRecuperate) {
                            if (t.getId() == searchId) {
                                gasita = t;
                                break;
                            }
                        }
                        if (gasita != null) {
                            System.out.println("NOTE[" + searchId + "]: " + gasita.getNote());
                        } else {
                            System.out.println("NOTE[" + searchId + "]: not found");
                        }
                        break;
                }
            }
        }
        sc.close();
    }
}