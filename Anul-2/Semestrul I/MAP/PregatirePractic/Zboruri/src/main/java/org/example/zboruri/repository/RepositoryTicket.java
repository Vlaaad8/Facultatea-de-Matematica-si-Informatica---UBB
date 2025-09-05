package org.example.zboruri.repository;

import org.example.zboruri.domain.Ticket;

import java.sql.*;
import java.util.Optional;

public class RepositoryTicket extends AbstractDBRepository{
    public RepositoryTicket(String username, String password, String url) {
        super(username, password, url);
    }

    @Override
    public Iterable findAll() throws SQLException {
        return null;
    }

    @Override
    public Object findOne(int id) throws SQLException {
        return null;
    }

    public Ticket save(Ticket entity)  {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"ticket\" (username,flightid,purchasetime) VALUES (?, ?,?)")){
            statement.setString(1, entity.getUsername());
            statement.setLong(2, entity.getFlightId());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getPurchaseTime()));
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
        if (rez > 0)
            return null;
        else
            return entity;
    }


}
