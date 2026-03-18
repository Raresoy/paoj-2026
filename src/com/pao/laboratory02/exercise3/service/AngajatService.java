package com.pao.laboratory02.exercise3.service;

import com.pao.laboratory02.exercise3.model.Angajat;
import java.util.ArrayList;
import java.util.List;

public class AngajatService {
    private List<Angajat> angajati;

    public AngajatService() {
        this.angajati = new ArrayList<>();
    }

    public void addAngajat(Angajat a) {
        angajati.add(a);
        System.out.println("Angajat adaugat: " + a.getName());
    }

    public void listAll() {
        if(angajati.isEmpty())
            System.out.println("Lista este goala ");
        else {
            for(int i = 0; i < angajati.size(); i++) {
                System.out.println(i+1 + ". " + angajati.get(i));
            }
        }
    }

    public double totalSalarii() {
        int s = 0;
        for(int i = 0; i < angajati.size(); i++) {
            s += angajati.get(i).salariuTotal();
        }
        return s;
    }
}