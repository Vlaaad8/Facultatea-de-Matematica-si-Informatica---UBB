package org.example;

import java.util.List;

public interface IServices {
    Player login(Player player, IObserver client);
    Configuration getRandomConfig();
    void saveGame(Game game);
    Iterable<Game> getGames();
}
