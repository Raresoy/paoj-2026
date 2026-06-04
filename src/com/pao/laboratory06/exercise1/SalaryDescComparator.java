package com.pao.laboratory06.exercise1;

import java.util.Comparator;

public class SalaryDescComparator implements Comparator<Angajat> {
    @Override
    public int compare(Angajat a1, Angajat a2) {
        return Double.compare(a2.getSalariu(), a1.getSalariu());
    }
}