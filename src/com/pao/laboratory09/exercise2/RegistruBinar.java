package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

/*
3
101 2500.00 2024-05-10 CREDIT
102 45.99 2024-05-11 DEBIT
103 120.00 2024-05-12 DEBIT
UPDATE 0 PROCESSED
READ 0
UPDATE 2 REJECTED
PRINT_ALL */

public class RegistruBinar {
    private static final String FILE_PATH = "/home/raresoi/paoj-2026/src/com/pao/laboratory09/exercise2/output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) {
        File dir = new File("output");
        if (!dir.exists()) dir.mkdirs();

        try (Scanner sc = new Scanner(System.in)) {
            int n = Integer.parseInt(sc.nextLine());
            
            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                for (int i = 0; i < n; i++) {
                    String[] line = sc.nextLine().split(" ");
                    int id = Integer.parseInt(line[0]);
                    double suma = Double.parseDouble(line[1]);
                    String data = line[2];
                    TipTranzactie tip = TipTranzactie.valueOf(line[3]);

                    fos.write(serializeTransaction(id, suma, data, tip));
                }
            }

            try (RandomAccessFile raf = new RandomAccessFile(FILE_PATH, "rw")) {
                while (sc.hasNext()) {
                    String command = sc.next();
                    if (command.equals("READ")) {
                        int idx = sc.nextInt();
                        System.out.println(readRecord(raf, idx));
                    } else if (command.equals("UPDATE")) {
                        int idx = sc.nextInt();
                        String statusStr = sc.next();
                        updateStatus(raf, idx, statusStr);
                        System.out.println("Updated [" + idx + "]: " + statusStr);
                    } else if (command.equals("PRINT_ALL")) {
                        long numRecords = raf.length() / RECORD_SIZE;
                        for (int i = 0; i < numRecords; i++) {
                            System.out.println(readRecord(raf, i));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] serializeTransaction(int id, double suma, String data, TipTranzactie tip) {
        ByteBuffer buffer = ByteBuffer.allocate(RECORD_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        
        buffer.putInt(id);           
        buffer.putDouble(suma);    
        
        byte[] dataBytes = new byte[10];
        byte[] originalData = data.getBytes();
        System.arraycopy(originalData, 0, dataBytes, 0, Math.min(originalData.length, 10));
        for (int i = originalData.length; i < 10; i++) dataBytes[i] = (byte) ' ';
        buffer.put(dataBytes);       

        buffer.put((byte) (tip == TipTranzactie.CREDIT ? 0 : 1)); 
        buffer.put((byte) 0);        
        
        return buffer.array();
    }

    private static String readRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);
        byte[] record = new byte[RECORD_SIZE];
        raf.readFully(record);

        ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
        int id = buffer.getInt();
        double suma = buffer.getDouble();
        
        byte[] dataBytes = new byte[10];
        buffer.get(dataBytes);
        String data = new String(dataBytes).trim();

        int tipInt = buffer.get();
        String tip = (tipInt == 0) ? "CREDIT" : "DEBIT";

        int statusInt = buffer.get();
        String status = statusInt == 0 ? "PENDING" : (statusInt == 1 ? "PROCESSED" : "REJECTED");

        return String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s", idx, id, data, tip, suma, status);
    }

    private static void updateStatus(RandomAccessFile raf, int idx, String statusStr) throws IOException {
        int statusCode = statusStr.equals("PENDING") ? 0 : (statusStr.equals("PROCESSED") ? 1 : 2);
        raf.seek((long) idx * RECORD_SIZE + 23);
        raf.write(statusCode);
    }
}