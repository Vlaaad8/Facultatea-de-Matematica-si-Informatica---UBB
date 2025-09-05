package org.example.demo.service;

import org.example.demo.domain.Entity;
import org.example.demo.domain.Looking;
import org.example.demo.domain.event.ChangeEventType;
import org.example.demo.domain.event.LookingEntityChange;
import org.example.demo.observer.Observable;
import org.example.demo.observer.Observer;
import org.example.demo.repository.RepositoryLooking;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceLooking implements Observable<LookingEntityChange> {
    private RepositoryLooking repositoryLooking;
    private List<Observer<LookingEntityChange>> observers = new ArrayList<>();
    public ServiceLooking(RepositoryLooking repositoryLooking) {
        this.repositoryLooking = repositoryLooking;
    }

    public void save(Looking looking) {
        Looking a=repositoryLooking.save(looking);
        LookingEntityChange messageEntityChange = new LookingEntityChange(ChangeEventType.ADD, a);
        notifyObservers(messageEntityChange);
    }
    public void delete(Long looking) {
        try {
            Looking a=repositoryLooking.findOne(looking);
            Looking b=repositoryLooking.delete(a);
            LookingEntityChange messageEntityChange = new LookingEntityChange(ChangeEventType.DELETE, b);
            notifyObservers(messageEntityChange);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Long getLastID(){
        Long max=-1L;
        try {
            for(Looking look: repositoryLooking.findAll()){
                if(look.getId()>max){
                    max=look.getId();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return max;
    }

    public Long getLookingByDestinations(String to,String from) throws SQLException {
        for(Looking look:repositoryLooking.findAll()){
            if(look.getDeparture().equals(from) && look.getDestination().equals(to)){
                return look.getId();
            }
        }
        return 0L;
    }

    @Override
    public void addObserver(Observer<LookingEntityChange> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<LookingEntityChange> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(LookingEntityChange t) {

        observers.stream().forEach(x -> x.update(t));
    }

    public int numberViewers(String to,String from) throws SQLException {
        int nr=0;
        for(Looking looking: repositoryLooking.findAll()){
            if(looking.getDeparture().equals(from) && looking.getDestination().equals(to)){
                nr++;
            }
        }
        return nr;
    }
}
