package org.example;

import org.example.clientfx.Flight;
import org.example.client.FlightClient;

import java.time.LocalDateTime;

public class StartRestClient {
    private final static FlightClient flightClient = new FlightClient();

    public static void main(String[] args) {
        try {
            //GetALL
            show(() -> {
                Iterable<Flight> res = flightClient.getAll();
                for (Flight u : res) {
                    System.out.println(u.getId() + ": " + u.getDeparture());
                }
                //GetByID
                show(() -> {
                    Flight flight = flightClient.getById(1);
                    System.out.println(flight.getId() + ": " + flight.getDeparture());
                });
            });
            //Update
            Flight flight = new Flight("Suceava", "Chisinau", 201, "SCV",LocalDateTime.now());
            show(() -> System.out.println(flightClient.create(flight).getId()));

            show(() -> {
                Iterable<Flight> res = flightClient.getAll();
                for (Flight u : res) {
                    System.out.println(u.getId() + ": " + u.getDeparture());
                }
            });
            show(() -> System.out.println(flightClient.update(19,flight)));
            show(() -> {
                Iterable<Flight> res = flightClient.getAll();
                for (Flight u : res) {
                    System.out.println(u.getId() + ": " + u.getDeparture());
                }
                });
            //Delete
            show(() -> flightClient.delete(19));
            show(() -> {
                Iterable<Flight> res = flightClient.getAll();
                for (Flight u : res) {
                    System.out.println(u.getId() + ": " + u.getDeparture());
                }
            });

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        private static void show (Runnable task){
            try {
                task.run();
            } catch (Exception e) {
                System.out.println("Service exception" + e);
            }
        }
    }
