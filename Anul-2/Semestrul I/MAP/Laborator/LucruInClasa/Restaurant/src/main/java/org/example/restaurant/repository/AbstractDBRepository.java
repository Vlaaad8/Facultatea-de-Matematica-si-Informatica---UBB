package org.example.restaurant.repository;

import java.sql.SQLException;

public abstract class AbstractDBRepository <E>{
    private final String url;
    private final String username;
    private final String password;
    public AbstractDBRepository(String username, String password,String url) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public abstract Iterable<E> findAll() throws SQLException;

    public abstract E findOne(int id) throws SQLException;

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
