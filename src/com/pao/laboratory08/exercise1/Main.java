package com.pao.laboratory08.exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "/home/raresoi/paoj-2026/src/com/pao/laboratory08/exercise1/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        // 3. Execută comanda:
        //    - PRINT → afișează toți studenții
        //    - SHALLOW <nume> → shallow clone + modifică orașul clonei la "MODIFICAT" + afișează
        //    - DEEP <nume> → deep clone + modifică orașul clonei la "MODIFICAT" + afișează

        System.out.println("TODO: implementează exercițiul 1");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        List<Student> studenti = citesteStudenti();
        String[] parts = input.split(" ", 2);
        String comanda = parts[0].toUpperCase();

        try {
            switch(comanda) {
                case "PRINT":
                    for(Student s : studenti) {
                        System.out.println(s);
                    }
                    break;
                case "SHALLOW": {
                    String nume = parts[1];
                    Student original = gasesteStudent(studenti, nume);
                    Student clona = original.shallowClone();
                    clona.getAdresa().setOras("Modificat");
                    System.out.println("Original " + original);
                    System.out.println("Clona " + clona);
                    break;
                }
                case "DEEP": {
                    String nume = parts[1];
                    Student original = gasesteStudent(studenti, nume);
                    Student clona = original.deepClone();
                    clona.getAdresa().setOras("Modificat");
                    System.out.println("Original " + original);
                    System.out.println("Clona" + clona);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static List<Student> citesteStudenti() {
        List<Student> lista = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linie;
            while((linie = br.readLine()) != null) {
                String[] parts = linie.split(",");
                String nume = parts[0].trim();
                int varsta = Integer.parseInt(parts[1].trim());
                String oras = parts[2].trim();
                String strada = parts[3].trim();
                Adresa adresa = new Adresa(oras, strada);
                Student student = new Student(nume, varsta, adresa);
                lista.add(student);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private static Student gasesteStudent(List<Student> studenti, String nume) {
        for(Student s : studenti) {
            if(s.getNume().equals(nume))
                return s;
        }
        throw new RuntimeException("Studentul nu a fost gasit");
    }
}
