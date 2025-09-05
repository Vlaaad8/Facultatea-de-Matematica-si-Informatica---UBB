package com.example.guiex1.repository.dbrepo;

import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.Validator;
import com.example.guiex1.repository.Repository;


import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UtilizatorDbRepository implements Repository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;
    private Validator<Utilizator> validator;

    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    /**
     * @param id - long, the id of a user to found
     * @return Optional<User> - the user with the given id
     *                        -Optional.empty() otherwise
     */
    @Override
    public Optional<Utilizator> findOne(Long id) {
        Utilizator user;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            ResultSet resultSet = connection.createStatement().executeQuery(String.format("select * from \"User\" U where U.id_user = '%d'", id))) {
            if(resultSet.next()){
                user = createUserFromResultSet(resultSet);
                return Optional.ofNullable(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    private Utilizator createUserFromResultSet(ResultSet resultSet) {
        try {
            String firstName = resultSet.getString("firstname");
            String lastName = resultSet.getString("lastname");
            String email= resultSet.getString("email");

            Long idd = resultSet.getLong("id_user");
            Utilizator user = new Utilizator(firstName, lastName,email);
            user.setId(idd);
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"User\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id_user");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String email = resultSet.getString("email");

                Utilizator utilizator = new Utilizator(firstName, lastName,email);
                utilizator.setId(id);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        String sql = "insert into \"User\" (firstname, lastname,email) values (?, ?,?)";
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());

            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            return Optional.ofNullable(entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utilizator> delete(Long id) {
        String sql = "delete from \"User\" where id_user = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            Optional<Utilizator> user = findOne(id);
            if(!user.isEmpty()) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utilizator> update(Utilizator user) {
        if (user == null) {
            throw new IllegalArgumentException("User entity must not be null!");
        }
        validator.validate(user);

        String sql = "UPDATE \"User\" SET firstname = ?, lastname = ?, email = ? WHERE id_user = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setLong(4, user.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                return Optional.empty();
            } else {
                return Optional.of(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.of(user);
    }

}
