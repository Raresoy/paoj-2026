package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    
    private Angajat[] angajati;

    private AngajatService() {
        angajati = new Angajat[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] newArr = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, newArr, 0, angajati.length);
        newArr[angajati.length] = a;
        angajati = newArr;
        System.out.println("Adaugat: " + a);
    }

    public void printAll() {
        for(Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);

        for(Angajat a : copy)
            System.out.println(a);
    }

    public void findByDepartament(String numeDep) {
        boolean ok = false;

        for(Angajat a : angajati) {
            if(a.getDepartament().nume().equalsIgnoreCase(numeDep)) {
                System.out.println(a);
                ok = true;
            }
        }

        if(!ok)
            System.out.println("Niciun angajat in acest departament");
    }
}