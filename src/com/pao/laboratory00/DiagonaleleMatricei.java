package com.pao.laboratory00;

import java.util.Scanner;

public class DiagonaleleMatricei {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;
        float[][] array;
        n = scanner.nextInt();
        array = new float[n][n];
        int sum = 0, prod = 1;

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                array[i][j] = scanner.nextFloat();
                if(i == j)
                    sum += array[i][j];
                if(i + j == n)
                    prod *= array[i][j];
            }
        }
        System.out.println(sum);
        System.out.println(prod);
        
    }
}