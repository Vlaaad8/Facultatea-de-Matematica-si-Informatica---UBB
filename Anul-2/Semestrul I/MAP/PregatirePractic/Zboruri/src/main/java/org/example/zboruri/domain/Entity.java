package org.example.zboruri.domain;

public class Entity {
    private Long ID;


    public Entity(Long ID) {
        this.ID = ID;
    }
    public Entity(){

    }

    public Long getId() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
}
