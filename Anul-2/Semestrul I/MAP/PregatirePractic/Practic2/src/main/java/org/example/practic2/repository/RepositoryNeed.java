package org.example.practic2.repository;

import org.example.practic2.domain.Need;
import org.example.practic2.domain.Person;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class RepositoryNeed extends AbstractDBRepository{
    public RepositoryNeed(String username, String password, String url) {
        super(username, password, url);
    }

    public Iterable<Need> findAll() throws SQLException {
        Set<Need> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM need");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long serialnumberuid = resultSet.getLong("serialversionuid");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                LocalDateTime deadline=resultSet.getTimestamp("deadline").toLocalDateTime();
                Long peopleInNeed = resultSet.getLong("personinneed");
                Long persontosave = resultSet.getLong("persontosave");
                String status = resultSet.getString("status");
                Need person=new Need(title,description,deadline,peopleInNeed,persontosave,status);

                person.setId(id);
                person.setSerialVersionUID(serialnumberuid);
                tables.add(person);
            }
        }
        return tables;
    }

    public Need update(Need entity) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(),getPassword())) {
            PreparedStatement statement = connection.prepareStatement("UPDATE need SET persontosave=?, status=? WHERE id = ?");
            statement.setLong(1, entity.getPersontosave());
            statement.setString(2, entity.getStatus());
            statement.setLong(3, entity.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return entity;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return null;
    }

    public Need save(Need entity)  {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"need\" (title,description,deadline,personinneed,status) VALUES (?, ?,?,?,?)")){
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDeadline()));
            statement.setLong(4, entity.getPersoninneed());
            statement.setString(5, entity.getStatus());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
            System.out.println(e.getMessage());
        }
        if (rez > 0)
            return null;
        else
            return entity;
    }
}
