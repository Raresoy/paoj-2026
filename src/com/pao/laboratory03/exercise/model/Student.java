package com.pao.laboratory03.exercise.model;


import com.pao.laboratory03.exercise.exceptions.InvalidGradeException;
import com.pao.laboratory03.exercise.exceptions.InvalidStudentException;
import java.util.HashMap;
import java.util.Map;

public class Student {
    private String name;
    private int age;
    private Map<Subject, Double> grades;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
        if(age < 18 || age > 60)
            throw new InvalidStudentException("Varsta trebuie sa fie intre 18 si 60");
        grades = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Map<Subject, Double> getGrades() {
        return grades;
    }

    public void addGrade(Subject subject, Double grade) {
        if(grade < 1 || grade > 10)
            throw new InvalidGradeException("Nota trebuie sa fie intre 1 si 10");
        grades.put(subject, grade);
    }

    public double getAverage() {
        if(grades.isEmpty())
            return 0;

        double s = 0;
        for(double grade : grades.values()) {
            s += grade;
        }

        return s / grades.size();
    }

    @Override
    public String toString() {
        return "Student " + this.name + ", " + this.age + ", " + getAverage();
    }

}