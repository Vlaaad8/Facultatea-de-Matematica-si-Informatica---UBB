package org.example;

import org.example.servers.AbstractServer;
import org.example.servers.ConcurrentServerRPC;

import java.util.Properties;

public class StartServerRPC {
    private static final int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProperties = new Properties();
        try {
            serverProperties.load(StartServerRPC.class.getResourceAsStream("/hibernate.properties"));

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        PlayerRepository playerRepository=new PlayerRepo();
        GameRepository gameRepository=new GameRepo();
        ConfigurationRepository configurationRepository=new ConfigurationRepo();
        IServices services = new ServicesImplementation(playerRepository,gameRepository,configurationRepository);
        int chatServerPort = defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProperties.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.out.println(nef);

        }
        System.out.println("Server running on port " + chatServerPort);
        AbstractServer server = new ConcurrentServerRPC(chatServerPort, services);
        try {
            System.out.println("Server started");
            ((AbstractServer) server).start();;
        } catch (Exception e) {
            System.out.println("A aparut o problema!");
            System.out.println(e);
        } finally {
            try {
                server.stop();
            } catch (Exception e) {
                System.out.println("A aparut o eroare in finnally");
                System.out.println(e);
            }
        }
    }
}
