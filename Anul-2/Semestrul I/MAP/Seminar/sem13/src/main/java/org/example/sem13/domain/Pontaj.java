package org.example.sem13.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Pontaj extends Entity<String>{
    private Angajat angajat;
    private Sarcina sarcina;
    private LocalDateTime data;

    public Pontaj(Angajat angajat, Sarcina sarcina, LocalDateTime data) {
        super(angajat.getId() + "," + sarcina.getId());
        this.angajat = angajat;
        this.sarcina = sarcina;
        this.data = data;
    }

    public Angajat getAngajat() {
        return angajat;
    }

    public void setAngajat(Angajat angajat) {
        this.angajat = angajat;
    }

    public Sarcina getSarcina() {
        return sarcina;
    }

    public void setSarcina(Sarcina sarcina) {
        this.sarcina = sarcina;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pontaj angajat = (Pontaj) o;
        return this.getId().equals(angajat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pontaj{" +
                "angajat=" + angajat +
                ", sarcina=" + sarcina +
                ", data=" + data +
                ", id=" + getId() +
                '}';
    }
}
