package org.example;

public interface IServices {
    Player login(Player player, IObserver client);
    Configuration getRandomConfig();
    void saveGame(Game game);
    Iterable<Game> getGames();
    void saveConfig(Configuration configuration);
}
