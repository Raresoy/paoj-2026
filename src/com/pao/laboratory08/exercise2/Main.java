package com.pao.laboratory08.exercise2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

public class Main {
    private static final String FILE_PATH = "/home/raresoi/paoj-2026/src/com/pao/laboratory08/exercise1/tests/studenti.txt";

    private static final String OUTPUT_PATH = "/home/raresoi/paoj-2026/src/com/pao/laboratory08/exercise1/tests/output.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă

        System.out.println("TODO: implementează exercițiul 2");
        List<Student> studenti = citesteStudenti();
        Scanner scanner = new Scanner(System.in);
        int prag = scanner.nextInt();
        List<Student> filtrati = filtreaza(studenti, prag);
        scrieInFisier(filtrati);
        afiseaza(filtrati, prag);
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

    private static List<Student> filtreaza(List<Student> studenti, int prag) {
        List<Student> rezultat = new ArrayList<>();
        for(Student s : studenti) {
            if(s.getVarsta() >= prag)
                rezultat.add(s);
        }
        return rezultat;
    }

    private static void scrieInFisier(List<Student> studenti) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_PATH))) {
            for(Student s : studenti) {
                bw.write(s.toString());
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void afiseaza(List<Student> studenti, int prag) {
        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + studenti.size() + " studenti");
        System.out.println();

        for(Student s : studenti) {
            System.out.println(s);
        }
    }
}

