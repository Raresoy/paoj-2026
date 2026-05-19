package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/*
5
1 100.00 2024-01-01 CREDIT
2 200.00 2024-01-02 DEBIT
1 300.00 2024-01-03 CREDIT
3 400.00 2024-01-04 DEBIT
2 500.00 2024-01-05 CREDIT
UNIQUE_IDS
 */

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Tranzactie> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
            lista.add(new Tranzactie(id, suma, data, tip));
        }
        while (scanner.hasNext()) {
            String comanda = scanner.next();
            switch (comanda) {
                case "UNIQUE_IDS": {
                    Set<Integer> ids = new LinkedHashSet<>();
                    for (Tranzactie t : lista) {
                        ids.add(t.getId());
                    }
                    System.out.println("IDs unice (" + ids.size() + "): " + ids);
                    break;
                }
                case "MONTHLY_REPORT": {
                    TreeMap<String, double[]> raport = new TreeMap<>();
                    for (Tranzactie t : lista) {
                        String luna = t.getData().substring(0, 7);
                        raport.putIfAbsent(luna, new double[2]);
                        if (t.getTip() == TipTranzactie.CREDIT) {
                            raport.get(luna)[0] += t.getSuma();
                        } else {
                            raport.get(luna)[1] += t.getSuma();
                        }
                    }
                    for (String luna : raport.keySet()) {
                        double credit = raport.get(luna)[0];
                        double debit = raport.get(luna)[1];
                        System.out.printf(
                                "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                luna,
                                credit,
                                debit
                        );
                    }
                    break;
                }
                case "TOP": {
                    int topN = scanner.nextInt();
                    List<Tranzactie> copie = new ArrayList<>(lista);
                    copie.sort(
                            Comparator.comparingDouble(Tranzactie::getSuma)
                                    .reversed()
                    );
                    System.out.println("Top " + topN + ":");
                    for (int i = 0; i < Math.min(topN, copie.size()); i++) {
                        System.out.println(copie.get(i));
                    }
                    break;
                }
                case "SORT_ASC": {
                    lista.sort(
                            Comparator.comparingDouble(Tranzactie::getSuma)
                    );
                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SORT_DESC": {
                    lista.sort(
                            Comparator.comparingDouble(Tranzactie::getSuma)
                                    .reversed()
                    );
                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }
                    break;
                }
                case "REVERSE": {
                    Collections.reverse(lista);
                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }
                    break;
                }
                case "MIN_MAX": {
                    Tranzactie min = Collections.min(
                            lista,
                            Comparator.comparingDouble(Tranzactie::getSuma)
                    );
                    Tranzactie max = Collections.max(
                            lista,
                            Comparator.comparingDouble(Tranzactie::getSuma)
                    );
                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                    break;
                }
                case "CME_DEMO": {
                    try {
                        for (Tranzactie t : lista) {
                            lista.remove(t);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println(
                                "ConcurrentModificationException prins: modificare in iteratie detectata."
                        );
                    }
                    break;
                }
            }
        }
        scanner.close();
    }
}