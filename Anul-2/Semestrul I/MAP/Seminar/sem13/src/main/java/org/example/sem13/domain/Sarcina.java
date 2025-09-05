package org.example.sem13.domain;

import java.util.Objects;

public class Sarcina extends Entity<String>{
    private Dificultate dificultate;
    private Integer numarOreEstimate;

    public Sarcina(String id, Dificultate dificultate, Integer numarOreEstimate) {
        super(id);
        this.dificultate = dificultate;
        this.numarOreEstimate = numarOreEstimate;
    }

    public Dificultate getDificultate() {
        return dificultate;
    }

    public void setDificultate(Dificultate dificultate) {
        this.dificultate = dificultate;
    }

    public Integer getNumarOreEstimate() {
        return numarOreEstimate;
    }

    public void setNumarOreEstimate(Integer numarOreEstimate) {
        this.numarOreEstimate = numarOreEstimate;
    }

    @Override
    public String toString() {
        return "Sarcina{" +
                "dificultate=" + dificultate +
                ", numarOreEstimate=" + numarOreEstimate +
                ", id=" + getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sarcina angajat = (Sarcina) o;
        return this.getId().equals(angajat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
