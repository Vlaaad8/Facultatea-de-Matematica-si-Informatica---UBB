package org.example.DTO;

import java.io.Serializable;

public class PlayerDTO implements Serializable {
    private int id;
    private String name;

    public PlayerDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PlayerDTO [id=" + id + ", name=" + name + "]";
    }
}
