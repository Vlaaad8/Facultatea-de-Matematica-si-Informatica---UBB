package org.example;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImplementation implements IServices {
    private final int defaultThreadsNo = 5;
    private PlayerRepository playerRepository;
    private GameRepository gameRepository;
    private ConfigurationRepository configurationRepository;
    private Map<String,IObserver> loggedClients;

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
                System.out.println("Deja inregistrat");
            }
            else{
                loggedClients.put(player.getName(),client);
            }
        }
        return playerRepo;
    }

    @Override
    public Configuration getRandomConfig(){
        System.out.println("Am ajuns aici config!");
        List<Configuration> configs= (List<Configuration>) configurationRepository.findAll();
        System.out.println(configs.size());
        Random random = new Random();
        return configs.get(random.nextInt(configs.size()));

    }
    @Override
    public void saveGame(Game game){
        gameRepository.add(game);
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        System.out.println("In observer avem "+ loggedClients.size());
        for(IObserver client : loggedClients.values()) {
            executor.execute(() -> {
                ;
                try {
                    client.gameFinished();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executor.shutdown();
    }
    @Override
    public Iterable<Game> getGames(){
        return gameRepository.findAll();
    }
}
