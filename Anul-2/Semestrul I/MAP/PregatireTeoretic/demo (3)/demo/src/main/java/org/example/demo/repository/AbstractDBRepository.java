package org.example.demo.repository;

public class AbstractDBRepository {
    private final String url;
    private final String username;
    private final String password;
    public AbstractDBRepository(String username, String password,String url) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}