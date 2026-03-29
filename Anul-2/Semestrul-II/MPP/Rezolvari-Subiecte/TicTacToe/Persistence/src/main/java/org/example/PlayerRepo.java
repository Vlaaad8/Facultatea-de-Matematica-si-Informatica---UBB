package org.example;

import org.hibernate.Session;

import java.util.Optional;

public class PlayerRepo implements PlayerRepository {
    @Override
    public Optional<Player> login(String user) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Player employee =session.createQuery("from Player where name = :user",Player.class)
                    .setParameter("user", user)
                    .getSingleResultOrNull();
            return Optional.of(employee);
        }
        catch(Exception e) {
            throw e;
        }
    }

    @Override
    public Optional<Player> add(Player entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Player> delete(Player entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Player> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<Player> findAll() {
        return null;
    }
}
