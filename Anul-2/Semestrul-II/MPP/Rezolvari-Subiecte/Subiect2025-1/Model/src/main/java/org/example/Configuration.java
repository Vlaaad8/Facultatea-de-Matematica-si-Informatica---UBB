package org.example;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="gamesettings")
public class Configuration extends org.example.Entity<Integer> {
    @Column(nullable = false)
    private int i;
    @Column(nullable = false)
    private int j;
    @Column(nullable = false)
    private String animal;
    @Column(nullable = false)
    private String animalLink;

    public Configuration(int i, int j, String animal, String animalLink) {
        this.i = i;
        this.j = j;
        this.animal = animal;
        this.animalLink = animalLink;
    }
    public Configuration() {

    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getAnimalLink() {
        return animalLink;
    }

    public void setAnimalLink(String animalLink) {
        this.animalLink = animalLink;
    }
}
