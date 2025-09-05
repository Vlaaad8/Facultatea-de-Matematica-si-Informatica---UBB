package org.example.client;

import org.example.clientfx.Flight;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class FlightClient {
    RestClient restClient = RestClient.builder().requestInterceptor(new CustomRestClientInterceptor()).build();

    public static final String URL = "http://localhost:8081/ryanair/flights";

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) { // server down, resource exception
            throw new RuntimeException(e);
        }
    }
    public Iterable<Flight> getAll(){
    Flight[] flights= execute(()-> restClient.get().uri(URL).retrieve().body(Flight[].class));
    return Arrays.asList(flights);
    }
    public Flight getById(int id){
        return execute(()->restClient.get().uri(String.format("%s/%s", URL, id)).retrieve().body(Flight.class));
    }
    public Flight create(Flight flight){
        return execute(()->restClient.post().uri(URL).contentType(MediaType.APPLICATION_JSON).body(flight).retrieve().body(Flight.class));
    }
    public void delete(int id){
        execute(() -> restClient.delete().uri(String.format("%s/%s", URL, id)).retrieve().toBodilessEntity());
    }
    public Flight update(int id,Flight flight){
        return execute(()->restClient.put().uri(String.format("%s/%s", URL, id)).contentType(MediaType.APPLICATION_JSON).body(flight).retrieve().body(Flight.class));
    }
}



