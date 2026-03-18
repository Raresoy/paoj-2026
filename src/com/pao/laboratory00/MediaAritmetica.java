package com.pao.laboratory00;
import java.util.Scanner;

/**
 * Exercitiul 1
 *
 * Cititi de la tastatura un sir cu n elemente intregi.
 *
 * 1. Afisati elementele sirului in doua modalitati.
 * 2. Afisati media aritmetica a elementelor sirului.
 *
 */

public class MediaAritmetica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;
        int[] array;
        n = scanner.nextInt();
        int sum = 0;
        array = new int[n];
        for(int i = 0; i < n; i++)
            array[i] = scanner.nextInt();
        
        for(int i = 0; i < n; i++) {
            sum += array[i];
        }
        System.out.println(sum / array.length);
    }
}