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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ReflectionWorkerRPC implements Runnable, IObserver {
    private IServices server;
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private volatile boolean connected;

    private static final Logger logger = LogManager.getLogger();

    public ReflectionWorkerRPC(IServices services, Socket client) {
        this.server = services;
        this.client = client;
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            input = new ObjectInputStream(client.getInputStream());
            connected = true;
           logger.info("Am fost creat la cererea clientului");
        } catch (IOException e) {
           logger.error("error");
        }
    }
    private static final Response okResponse = (new Response.Builder()).type(ResponseType.OK).build();
    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                logger.info("Request received: {}", request);

                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.info("Error in worker run: {}", e.getMessage());
                connected = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Error in worker sleep: {}", e.getMessage());
            }
        }
        try {
            input.close();
            output.close();
            client.close();
        } catch (IOException e) {
            logger.error("Error closing worker connection: {}", e.getMessage());
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + request.type();
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            logger.info("Handled request: {}", request.type());
        } catch (NoSuchMethodException e) {
            logger.error("No such method: {}", handlerName);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            logger.error("Exception inside method: {}", handlerName);
            e.getCause().printStackTrace();  // ← foarte important
        } catch (IllegalAccessException e) {
            logger.error("Illegal access to method: {}", handlerName);
            e.printStackTrace();
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    private Response handleLOGIN(Request request) {;
        PlayerDTO employeeDTO = (PlayerDTO) request.data();
        Player employee = DTOUtils.getFromDTO(employeeDTO);
        try {
            logger.info("Angajatul pe care l-am primit are datele {}", employee.getName());
            Player employee1= server.login(employee,this);
            if(employee1.getId()>0) {
                PlayerDTO playerDTO= DTOUtils.getDTO(employee1);
                return (new Response.Builder()).type(ResponseType.OK).data(playerDTO).build();
            }
            else {
               logger.info("Angajatul nu a fost gasit");
                return (new Response.Builder()).type(ResponseType.NOT_FOUND).build();
            }
        } catch (Exception e) {
            this.connected = false;
            logger.error("Error in login: {}", e.getMessage());
            return (new Response.Builder()).type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_CONFIG(Request request){
        try{
            Configuration configuration= server.getRandomConfig();
            ConfigurationDTO configurationDTO=DTOUtils.getDTO(configuration);

            return (new Response.Builder()).type(ResponseType.OK).data(configurationDTO).build();
        }
        catch(Exception e){
            throw new RuntimeException("Eroare la get configuration game!");
        }

    }
    private Response handleSAVE_GAME(Request request){
        GameDTO gameDTO =(GameDTO) request.data();
        Game game= DTOUtils.getFromDTO(gameDTO);
        try{
            server.saveGame(game);
            return (new Response.Builder()).type(ResponseType.OK).build();
        }
        catch(Exception e){
            throw new RuntimeException("Eroare la get configuration game!");
        }
    }

    private Response handleGET_GAMES(Request request){
        try{
            Iterable<Game> games= server.getGames();
            List<GameDTO> games1=new ArrayList<>();
            for(Game game : games){
                games1.add(DTOUtils.getDTO(game));
            }
            return (new Response.Builder()).type(ResponseType.OK).data(games1).build();
        }
        catch(Exception e){
            throw new RuntimeException("Eroare la get configuration game!");
        }
    }

    @Override
    public void gameFinished() throws Exception {
        Response response=(new Response.Builder()).type(ResponseType.NEW_GAME).build();
        sendResponse(response);
        logger.info("Am trimis update!");
    }
}
