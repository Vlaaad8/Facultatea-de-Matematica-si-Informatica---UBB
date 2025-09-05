package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServicesImplementation implements IServices {
    private final int defaultThreadsNo = 5;
    private PlayerRepository playerRepository;
    private GameRepository gameRepository;
    private ConfigurationRepository configurationRepository;
    private Map<String,IObserver> loggedClients;

    private static final Logger logger = LogManager.getLogger();

    public ServicesImplementation(PlayerRepository playerRepository, GameRepository gameRepository, ConfigurationRepository configurationRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.configurationRepository = configurationRepository;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public Player login(Player player, IObserver client) {
        Player playerRepo= playerRepository.login(player.getName()).orElse(null);
        if(playerRepo != null){
            if(loggedClients.get(player.getName())!=null) {
                logger.info("Deja inregistrat");
            }
            else{
                loggedClients.put(player.getName(),client);
            }
        }
        return playerRepo;
    }

    @Override
    public Configuration getRandomConfig() {
        logger.info("Am ajuns aici config!");
        List<Configuration> all= (List<Configuration>) configurationRepository.findAll();
        System.out.println(all.size());
        Random random = new Random();
        return all.get(random.nextInt(all.size()));
    }


    private final ExecutorService notifyPool =
            Executors.newFixedThreadPool(defaultThreadsNo, r -> {
                Thread t = new Thread(r, "notify-pool-" + r.hashCode());
                t.setDaemon(true);
                return t;
            });
    @Override
    public void saveGame(Game game) {
        gameRepository.add(game);

        List<IObserver> observers = new ArrayList<>(loggedClients.values());
        logger.info("Trimitem notificare la {} clienti", observers.size());

        for (IObserver client : observers) {
            notifyPool.execute(() -> {
                try {
                    client.gameFinished();
                } catch (Exception ex) {
                    // optional: scoate clientul că nu mai răspunde
                    // loggedClients.remove(clientId);
                }
            });
        }
    }

    // la oprirea aplicaţiei
    public void stop() {
        notifyPool.shutdown();
        try {
            if (!notifyPool.awaitTermination(10, TimeUnit.SECONDS)) {
                notifyPool.shutdownNow();
            }
        } catch (InterruptedException ie) {
            notifyPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Iterable<Game> getGames(){
        return gameRepository.findAll();
    }

    @Override
    public List<Game> getFinishedGamesByName(String name) {
        return gameRepository.findAllFinished(name);
    }
}
