package org.example.sem13.domain;

import java.util.Objects;

public class Angajat extends Entity<String>{
    private String nume;
    private Double venitPeOra;
    private Nivel nivel;

    public Angajat(String id, String nume, Double venitPeOra, Nivel nivel) {
        super(id);
        this.nume = nume;
        this.venitPeOra = venitPeOra;
        this.nivel = nivel;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Double getVenitPeOra() {
        return venitPeOra;
    }

    public void setVenitPeOra(Double venitPeOra) {
        this.venitPeOra = venitPeOra;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Angajat angajat = (Angajat) o;
        return Objects.equals(nume, angajat.nume) && Objects.equals(venitPeOra, angajat.venitPeOra) && nivel == angajat.nivel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, venitPeOra, nivel);
    }

    @Override
    public String toString() {
        return "Angajat{" +
                "nume='" + nume + '\'' +
                ", venitPeOra=" + venitPeOra +
                ", nivel=" + nivel +
                '}';
    }
}
