package org.example.practic2.repository;

import org.example.practic2.domain.Person;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class RepositoryPerson extends AbstractDBRepository{
    public RepositoryPerson(String username, String password, String url) {
        super(username, password, url);
    }
    public Person save(Person entity)  {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"person\" (firstname,lastname,username,password,town,street,streetnumber,telephone) VALUES (?, ?,?,?,?,?,?,?)")){
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getUsername());
            statement.setString(4, entity.getPassword());
            statement.setString(5, entity.getTown());
            statement.setString(6, entity.getStreet());
            statement.setString(7,entity.getNumberStreet());
            statement.setString(8,entity.getTelephone());

            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
        if (rez > 0)
            return null;
        else
            return entity;
    }

    public Iterable<Person> findAll() throws SQLException {
        Set<Person> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM person");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long serialnumberuid = resultSet.getLong("serialversionuid");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String town = resultSet.getString("town");
                String street = resultSet.getString("street");
                String streetnumber = resultSet.getString("streetnumber");
                String telephone = resultSet.getString("telephone");
                Person person=new Person(firstname,lastname,username,password,town,street,streetnumber,telephone);
                person.setId(id);
                person.setSerialVersionUID(serialnumberuid);
                tables.add(person);
            }
        }
        return tables;
    }

    public Person findOne(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"person\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long serialnumberuid = resultSet.getLong("serialversionuid");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String town = resultSet.getString("town");
                String street = resultSet.getString("street");
                String streetnumber = resultSet.getString("streetnumber");
                String telephone = resultSet.getString("telephone");
                Person person=new Person(firstname,lastname,username,password,town,street,streetnumber,telephone);
                person.setId(id);
                person.setSerialVersionUID(serialnumberuid);
                return person;
            }
        }
        return null;
    }

}
