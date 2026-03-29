package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.DTO.ConfigurationDTO;
import org.example.DTO.DTOUtils;
import org.example.DTO.GameDTO;
import org.example.DTO.PlayerDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProxyRPC implements IServices{
    private String host;
    private int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private BlockingQueue<Response> qResponses;
    private volatile boolean finished;

    private static final Logger logger = LogManager.getLogger();
    public ProxyRPC(String host, int port) {
        this.host = host;
        this.port = port;
        qResponses = new LinkedBlockingQueue<>();
    }

    private void initializeConnection() throws IOException {
        try {
            socket = new Socket(this.host, this.port);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
            finished = false;
            startReader();
            logger.info("Connection initialized successfully");
        } catch (IOException e) {
            logger.error("Failed to initialize connection: " + e.getMessage());
            throw new IOException("Failed to initialize connection: " + e.getMessage());
        }
    }

    private void startReader() {
        Thread thread = new Thread(new ReaderThread());
        thread.start();
    }

    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.NEW_GAME) {
            try {
                this.client.gameFinished();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }


    @Override
    public Player login(Player employee,IObserver client) {
        try {
            this.initializeConnection();
            PlayerDTO employeeDTO = DTOUtils.getDTO(employee);
            logger.info("Am intrat in login");
            Request request = new Request.Builder()
                    .type(RequestType.LOGIN)
                    .data(employeeDTO)
                    .build();
            this.sendRequest(request);
            logger.info("Am trimis requestul de login");
            Response response = this.readResponse();
            if (response.type() == ResponseType.OK) {
                this.client= client;
                PlayerDTO playerDTO= (PlayerDTO) response.data();
                return DTOUtils.getFromDTO(playerDTO);
            }
            if (response.type() == ResponseType.ERROR) {
                closeConnection();
                logger.error("Erroare la logare");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Configuration getRandomConfig() {
            Request request= new Request.Builder().type(RequestType.GET_CONFIG).build();
            this.sendRequest(request);
            try{
            Response response = this.readResponse();
            if (response.type() == ResponseType.OK) {
                ConfigurationDTO configurationDTO= (ConfigurationDTO) response.data();
                Configuration configuration =  DTOUtils.getFromDTO(configurationDTO);
                return configuration;
            }
            else{
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveGame(Game game) {
        GameDTO gameDTO=DTOUtils.getDTO(game);
        Request request= new Request.Builder().type(RequestType.SAVE_GAME).data(gameDTO).build();
        this.sendRequest(request);
        try{
            Response response = this.readResponse();
            if (response.type() == ResponseType.ERROR) {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig(Configuration configuration) {
        ConfigurationDTO configDTO=DTOUtils.getDTO(configuration);
        Request request= new Request.Builder().type(RequestType.SAVE_CONFIG).data(configDTO).build();
        this.sendRequest(request);
        try{
            Response response = this.readResponse();
            if (response.type() == ResponseType.ERROR) {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Iterable<Game> getGames(){
        Request request= new Request.Builder().type(RequestType.GET_GAMES).build();
        this.sendRequest(request);
        try{
            Response response=this.readResponse();
            if (response.type() == ResponseType.OK) {
                List<Game> games = new ArrayList<>();
                for (GameDTO game : (List<GameDTO>) response.data()) {
                    games.add(DTOUtils.getFromDTO(game));
                }
                return games;
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.NEW_GAME;
    }

    private void sendRequest(Request request) {
        try {
           logger.info("Sending request: " + request.type());
            output.writeObject(request);
            output.flush();
           logger.info("Request sent successfully");
        } catch (IOException e) {
           logger.error("Failed to send request: " + e.getMessage());
            throw new RuntimeException("Failed to send request: " + e.getMessage());
        }
    }

    private Response readResponse() throws IOException {
        try {
            return this.qResponses.take();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void closeConnection() {
        this.finished = true;
        try {
            this.input.close();
            this.output.close();
            this.socket.close();
            this.client = null;
        } catch (IOException e) {
            logger.error("Error closing connection: " + e.getMessage());
        }
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    logger.info("Response received: " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {
                        try {
                            qResponses.put((Response) response);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.error("PROXY: Error in reader thread: " + e.getMessage());
                    closeConnection();
                }
            }
        }
    }
}