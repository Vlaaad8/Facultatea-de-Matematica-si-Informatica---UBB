package org.example.zboruri.domain;

public class Client extends Entity{
    private String username;
    private String name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client(Long id, String username, String name) {
        super(id);
        this.username = username;
        this.name = name;
    }
}
