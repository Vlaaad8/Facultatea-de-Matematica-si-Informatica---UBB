package org.example.seminar8;

import java.time.LocalDate;

public class Nota {
    private Student student;
    private Tema tema;
    private String profesor;
    private Double value;
    private LocalDate date;

    public Nota(Student student, Tema tema, Double value,LocalDate date,String profesor) {
        this.student = student;
        this.tema = tema;
        this.profesor = profesor;
        this.value = value;
        this.date = date;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
