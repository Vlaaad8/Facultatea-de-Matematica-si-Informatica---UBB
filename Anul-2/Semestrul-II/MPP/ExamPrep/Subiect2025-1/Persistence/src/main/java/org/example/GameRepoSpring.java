package org.example;

import java.util.Optional;

public class GameRepoSpring implements GameRepository {

    @Override
    public Optional<Game> add(Game entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Game> delete(Game entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Game> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<Game> findAll() {
        return null;
    }
}
