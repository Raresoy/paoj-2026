package com.pao.laboratory10.exercise1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/*
ENQUEUE 1 500.00 2024-01-10 CREDIT
ENQUEUE 2 300.00 2024-01-15 DEBIT
SIZE
PRINT
DEQUEUE
DEQUEUE


 */

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (scanner.hasNext()) {
            String comanda = scanner.next();
            switch (comanda) {
                case "ENQUEUE": {
                    int id = scanner.nextInt();
                    double suma = scanner.nextDouble();
                    String data = scanner.next();
                    TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                    coada.addLast(new Tranzactie(id, suma, data, tip));
                    break;
                }
                case "DEQUEUE": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = coada.removeFirst();
                        System.out.println("Procesat: " + t);
                    }
                    break;
                }
                case "PUSH": {
                    int id = scanner.nextInt();
                    double suma = scanner.nextDouble();
                    String data = scanner.next();
                    TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
                    coada.addFirst(new Tranzactie(id, suma, data, tip));
                    break;
                }
                case "POP": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = coada.removeFirst();
                        System.out.println("Extras: " + t);
                    }
                    break;
                }
                case "REMOVE_DEBIT": {
                    Iterator<Tranzactie> itr = coada.iterator();
                    int count = 0;
                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getTip() == TipTranzactie.DEBIT) {
                            itr.remove();
                            count++;
                        }
                    }
                    System.out.println("Eliminat " + count + " tranzactii DEBIT.");
                    break;
                }
                case "REMOVE_BELOW": {
                    double prag = scanner.nextDouble();
                    Iterator<Tranzactie> itr = coada.iterator();
                    int count = 0;
                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getSuma() < prag) {
                            itr.remove();
                            count++;
                        }
                    }
                    System.out.printf("Eliminat %d tranzactii sub %.2f RON.%n",
                            count, prag);
                    break;
                }
                case "PRINT": {
                    for (Tranzactie t : coada) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SIZE": {
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
                }
            }
        }
        scanner.close();
    }
}
