package org.example.zboruri.repository;

import org.example.zboruri.domain.Client;
import org.example.zboruri.domain.Flight;
import org.example.zboruri.domain.Ticket;
import org.example.zboruri.paging.Page;
import org.example.zboruri.paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RepositoryFlight extends AbstractDBRepository<Flight> {
    public RepositoryFlight(String username, String password, String url) {
        super(username, password, url);
    }

    @Override
    public Iterable<Flight> findAll() throws SQLException {
        Set<Flight> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM flight");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id=resultSet.getLong("id");
                String to =resultSet.getString("to");
                String from =resultSet.getString("from");
                LocalDateTime departure = resultSet.getTimestamp("departuretime").toLocalDateTime();
                LocalDateTime landingTime=resultSet.getTimestamp("landingtime").toLocalDateTime();
                int seats=resultSet.getInt("seats");
                int leftSeats=resultSet.getInt("avaibleseats");

                tables.add(new Flight(id, to, from, departure, landingTime, seats,leftSeats));
            }
        }
        return tables;
    }
    @Override
    public Flight findOne(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"flight\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id");
                String to = resultSet.getString("to");
                String from = resultSet.getString("from");
                LocalDateTime departure = resultSet.getTimestamp("departuretime").toLocalDateTime();
                LocalDateTime landingTime=resultSet.getTimestamp("landingtime").toLocalDateTime();
                int seats=resultSet.getInt("seats");
                int leftSeats=resultSet.getInt("avaibleseats");
                return new Flight(id1, to, from, departure, landingTime, seats,leftSeats);
            }
        }
        return null;
    }

    public Flight update(Flight entity) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(),getPassword())) {
            PreparedStatement statement = connection.prepareStatement("UPDATE flight SET avaibleseats=? WHERE id = ?");
            statement.setInt(1, entity.getAvaibleseats());
            statement.setLong(2, entity.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return entity;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return null;
    }

    public Page<Flight> findAllOnPage(Pageable pageable,String from1,String to1,LocalDateTime time) throws SQLException {
        Set<Flight> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement pageStatement = connection.prepareStatement("SELECT * FROM \"flight\" WHERE  \"from\"=? and \"to\"=? and departuretime=?  LIMIT ? OFFSET ?");
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM \"flight\"  WHERE \"from\"=? AND \"to\"=? and departuretime=?")) {
            pageStatement.setString(1, from1);
            pageStatement.setString(2, to1);
            pageStatement.setTimestamp(3,Timestamp.valueOf(time));
            pageStatement.setInt(4, pageable.getPageSize());
            pageStatement.setInt(5, pageable.getPageNumber() * pageable.getPageSize());
            countStatement.setString(1, from1);
            countStatement.setString(2, to1);
            countStatement.setTimestamp(3,Timestamp.valueOf(time));
            try (
                    ResultSet pageSet = pageStatement.executeQuery();
                    ResultSet countSet = countStatement.executeQuery()) {
                int count = 0;
                if (countSet.next()) {
                    count = countSet.getInt("count");
                }

                while (pageSet.next()) {
                    Long ID = pageSet.getLong("id");
                    String from=pageSet.getString("from");
                    String to =pageSet.getString("to");
                    LocalDateTime departure = pageSet.getTimestamp("departuretime").toLocalDateTime();
                    LocalDateTime landingTime=pageSet.getTimestamp("landingtime").toLocalDateTime();
                    int seats=pageSet.getInt("seats");
                    int leftSeats=pageSet.getInt("avaibleseats");
                    Flight flight=new Flight(ID, to, from, departure, landingTime, seats,leftSeats);
                    friendships.add(flight);
                }
                return new Page<>(friendships, count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
