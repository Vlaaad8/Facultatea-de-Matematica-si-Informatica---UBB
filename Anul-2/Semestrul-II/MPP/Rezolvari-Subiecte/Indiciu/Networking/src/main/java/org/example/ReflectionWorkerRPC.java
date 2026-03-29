package org.example;

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

    public ReflectionWorkerRPC(IServices services, Socket client) {
        this.server = services;
        this.client = client;
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            input = new ObjectInputStream(client.getInputStream());
            connected = true;
            System.out.println("Am fost creat la cererea clientului");
        } catch (IOException e) {
            System.out.println("error");
        }
    }
    private static final Response okResponse = (new Response.Builder()).type(ResponseType.OK).build();
    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                System.out.println("Request received: " + request);
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error in worker run: " + e.getMessage());
                connected = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Error in worker sleep: " + e.getMessage());
            }
        }
        try {
            input.close();
            output.close();
            client.close();
        } catch (IOException e) {
            System.err.println("Error closing worker connection: " + e.getMessage());
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + request.type();
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Handled request: " + request.type());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.err.println("Error handling request: " + e.getMessage());
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
            System.out.println("Angajatul pe care l-am primit are datele "+employee.getName());
            Player employee1= server.login(employee,this);
            if(employee1.getId()>0) {
                PlayerDTO playerDTO= DTOUtils.getDTO(employee1);
                return (new Response.Builder()).type(ResponseType.OK).data(playerDTO).build();
            }
            else {
                System.out.println("Angajatul nu a fost gasit");
                return (new Response.Builder()).type(ResponseType.NOT_FOUND).build();
            }
        } catch (Exception e) {
            this.connected = false;
            System.out.println("Error in login: " + e.getMessage());
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
        System.out.println("Am trimis update!");
    }
}
