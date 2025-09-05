package org.example.repository;

import org.example.domain.Friendship;
import org.example.domain.User;
import org.example.domain.validation.Validation;
import org.example.domain.validation.ValidationException;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDbRepository extends AbstractDbRepository<Long, Friendship> {
    public FriendshipDbRepository(Validation<Friendship> validator, String url, String username, String password) {
        super(url, username, password, validator);
    }
    @Override
    public Optional<Friendship> findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Friendship\" WHERE id_friendship = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long ID = resultSet.getLong("id_friendship");
                Long ID1 = resultSet.getLong("id_User1");
                String firstName1 = resultSet.getString("firstname_User1");
                String lastName1 = resultSet.getString("lastname_User1");
                Long ID2 = resultSet.getLong("id_User2");
                String firstName2 = resultSet.getString("firstname_User2");
                String lastName2 = resultSet.getString("lastname_User2");
                User user1 = new User(firstName1, lastName1);
                User user2 = new User(firstName2, lastName2);
                user1.setID(ID1);
                user2.setID(ID2);
                Friendship friendship = new Friendship(user1, user2);
                friendship.setID(ID);
                return Optional.of(friendship);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(),getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Friendship\"");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long ID = resultSet.getLong("id_friendship");
                Long ID1 = resultSet.getLong("id_user1");
                String firstName1 = resultSet.getString("firstname_user1");
                String lastName1 = resultSet.getString("lastname_user1");
                Long ID2 = resultSet.getLong("id_user2");
                String firstName2 = resultSet.getString("firstname_user2");
                String lastName2 = resultSet.getString("lastname_user2");
                User user1 = new User(firstName1, lastName1);
                User user2 = new User(firstName2, lastName2);
                user1.setID(ID1);
                user2.setID(ID2);
                Friendship friendship = new Friendship(user1, user2);
                friendship.setID(ID);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Friendship\" (id_user1,firstname_user1,lastname_user1,id_user2,firstname_user2,lastname_user2) VALUES (?,?,?,?,?,?)");
        ) {
            getValidator().validate(entity);
            statement.setLong(1, entity.getFirstFriend().getID());
            statement.setString(2, entity.getFirstFriend().getFirstName());
            statement.setString(3, entity.getFirstFriend().getLastName());
            statement.setLong(4, entity.getSecondFriend().getID());
            statement.setString(5, entity.getSecondFriend().getFirstName());
            statement.setString(6, entity.getSecondFriend().getLastName());
            rez = statement.executeUpdate();
        } catch (SQLException | ValidationException e) {
            e.getMessage();
        }
        if (rez > 0)
            return Optional.empty();
        else
            return Optional.of(entity);
    }

    @Override
    public Optional<Friendship> delete(Long id) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            Optional<Friendship> friendship = findOne(id);
            if (friendship.isEmpty()) {
                return Optional.empty();
            }
            PreparedStatement statement = connection.prepareStatement("DELETE FROM \"Friendship\" WHERE id_friendship = ?");
            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                return friendship;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Optional<Friendship> update(Friendship entity) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            Optional<Friendship> friendship = findOne(entity.getID());
            if (friendship.isEmpty()) {
                return Optional.of(entity);
            }
            getValidator().validate(entity);
            PreparedStatement statement = connection.prepareStatement("UPDATE Friendship SET id_user1=?,firstname_user1=?,lastname_user1=?,id_user2=?,firstname_user2=?,lastname_user2=? WHERE id_friendship = ? ");
            statement.setLong(1, entity.getFirstFriend().getID());
            statement.setString(2, entity.getFirstFriend().getFirstName());
            statement.setString(3, entity.getFirstFriend().getLastName());
            statement.setLong(4, entity.getSecondFriend().getID());
            statement.setString(5, entity.getSecondFriend().getFirstName());
            statement.setString(6, entity.getSecondFriend().getLastName());
            statement.setLong(7, entity.getID());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException | ValidationException e) {
            e.getMessage();
        }
        return Optional.empty();
    }

}
