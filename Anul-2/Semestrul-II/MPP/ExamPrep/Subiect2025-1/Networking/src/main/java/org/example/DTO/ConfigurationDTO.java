package org.example.DTO;

import java.io.Serializable;
import java.security.SecureRandomParameters;

public class ConfigurationDTO implements Serializable {
    private int id;
    private int i;
    private int j;
    private String animal;
    private String animalLink;


    public ConfigurationDTO(int id, int i, int j, String animal, String animalLink) {
        this.id = id;
        this.i = i;
        this.j = j;
        this.animal = animal;
        this.animalLink = animalLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
