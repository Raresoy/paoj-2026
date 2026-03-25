package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {

        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afișează toți angajații");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int opt = scanner.nextInt();
            scanner.nextLine(); // consumă newline

            switch (opt) {
                case 1:
                    System.out.print("Nume angajat: ");
                    String nume = scanner.nextLine();

                    System.out.print("Departament: ");
                    String numeDept = scanner.nextLine();

                    System.out.print("Locație departament: ");
                    String locatie = scanner.nextLine();

                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();
                    scanner.nextLine();

                    Departament dept = new Departament(numeDept, locatie);
                    Angajat angajat = new Angajat(nume, dept, salariu);

                    service.addAngajat(angajat);
                    break;

                case 2:
                    System.out.println("\n--- Sortați după salariu ---");
                    service.listBySalary();
                    break;

                case 3:
                    System.out.print("Introdu numele departamentului: ");
                    String deptCautat = scanner.nextLine();
                    service.findByDepartament(deptCautat);
                    break;

                case 4:
                    System.out.println("\n--- Toți angajații ---");
                    service.printAll();
                    break;

                case 0:
                    System.out.println("Ieșire...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opțiune invalidă!");
            }
        }
    }
}
