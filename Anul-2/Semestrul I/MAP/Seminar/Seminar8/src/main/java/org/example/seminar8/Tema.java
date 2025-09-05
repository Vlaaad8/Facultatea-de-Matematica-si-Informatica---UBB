package org.example.seminar8;

public class Tema {
    private String id;
    private String desc;

    public Tema(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
