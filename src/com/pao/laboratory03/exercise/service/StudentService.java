package com.pao.laboratory03.exercise.service;


import com.pao.laboratory03.exercise.exceptions.InvalidGradeException;
import com.pao.laboratory03.exercise.exceptions.InvalidStudentException;
import com.pao.laboratory03.exercise.exceptions.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;
import java.util.*;


public class StudentService {
    
    private List<Student> students;
    private static StudentService instance;

    private StudentService() {
        students = new ArrayList<>();
    }


    public static StudentService getInstance() {
        if(instance == null)
            instance = new StudentService();
        return instance;
    }

    public void addStudent(String name, int age) {
        for(Student s : students) {
            if(s.getName().equalsIgnoreCase(name))
                throw new RuntimeException("Studentul exista deja");
        }
        try {
            Student newStudent = new Student(name, age);
            students.add(newStudent);
        } catch(InvalidStudentException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Student findByName(String name) throws StudentNotFoundException {
        for(Student s : students) {
            if(s.getName().equalsIgnoreCase(name))
                return s;
        }
        throw new StudentNotFoundException("Studentul nu a fost gasit");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        try {
            Student student = findByName(studentName);
            student.addGrade(subject, grade);
        } catch(StudentNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        } catch(InvalidGradeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void printAllStudents() {
        for(Student s : students) {
            System.out.println(s);
            if (!s.getGrades().isEmpty()) {
                for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                    System.out.println("   " + entry.getKey() + " -> " + entry.getValue());
                }
            }
        }
    }

    public void printTopStudents() {
        List<Student> sortedStudents = new ArrayList<>(students);
        Collections.sort(sortedStudents, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                if (s2.getAverage() > s1.getAverage()) return 1;
                else if (s2.getAverage() < s1.getAverage()) return -1;
                else return 0;
            }
        });
        for (Student s : sortedStudents) {
            System.out.println(s);
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, List<Double>> tempMap = new HashMap<>();
        for (Student s : students) {
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                tempMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(entry.getValue());
            }
        }
        Map<Subject, Double> averages = new HashMap<>();
        for (Map.Entry<Subject, List<Double>> entry : tempMap.entrySet()) {
            List<Double> grades = entry.getValue();
            double sum = 0;
            for (double g : grades) sum += g;
            averages.put(entry.getKey(), sum / grades.size());
        }

        return averages;
    }
}