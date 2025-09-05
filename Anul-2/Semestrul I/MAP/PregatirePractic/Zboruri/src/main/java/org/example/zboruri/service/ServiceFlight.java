package org.example.zboruri.service;

import org.example.zboruri.domain.Client;
import org.example.zboruri.domain.Flight;
import org.example.zboruri.domain.event.ChangeEventType;
import org.example.zboruri.domain.event.FlighEntityChange;
import org.example.zboruri.observer.Observable;
import org.example.zboruri.observer.Observer;
import org.example.zboruri.paging.Page;
import org.example.zboruri.paging.Pageable;
import org.example.zboruri.repository.RepositoryFlight;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceFlight implements Observable<FlighEntityChange> {
    private RepositoryFlight repositoryFlight;
    private List<Observer<FlighEntityChange>> observers = new ArrayList<>();

    public ServiceFlight(RepositoryFlight repositoryFlight) {
        this.repositoryFlight = repositoryFlight;
    }
    public Iterable<Flight> findAll() throws SQLException {
        return repositoryFlight.findAll();
    }
    public Flight findOne(int id) throws SQLException {
        return repositoryFlight.findOne(id);
    }

    public Iterable<Flight>  sortDestinationArrivalDate(String destination, String arrival, LocalDate departureDay) throws SQLException {
        List<Flight> flights=new ArrayList<>();
        for(Flight flight:findAll()){
            if(flight.getFrom().equals(destination) && flight.getTo().equals(arrival) && flight.getDeparturetime().getMonth().equals(departureDay.getMonth()) &&
                    flight.getDeparturetime().getYear()==departureDay.getYear() && flight.getDeparturetime().getDayOfMonth()==departureDay.getDayOfMonth()) {
                flights.add(flight);
            }
        }
        return flights;
    }
    public Iterable<String> toDestinations() throws SQLException {
        Set<String> destinations=new HashSet<>();
        for(Flight flight:findAll()){
            destinations.add(flight.getTo());

        }
        return destinations;
    }

    public Iterable<String> fromDestinations() throws SQLException {
        Set<String> destinations=new HashSet<>();
        for(Flight flight:findAll()){
            destinations.add(flight.getFrom());

        }
        return destinations;
    }

    public void update (Flight flight) throws SQLException {
        Flight a = repositoryFlight.update(flight);
        FlighEntityChange event = new FlighEntityChange(ChangeEventType.UPDATE, a);
        notifyObservers(event);
    }

    @Override
    public void addObserver(Observer<FlighEntityChange> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FlighEntityChange> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FlighEntityChange t) {
        observers.stream().forEach(x -> x.update(t));
    }
    public Page<Flight> findAllOnPage(Pageable page,String from,String to,LocalDateTime time) throws SQLException {
        return repositoryFlight.findAllOnPage(page,from,to,time);
    }


}
