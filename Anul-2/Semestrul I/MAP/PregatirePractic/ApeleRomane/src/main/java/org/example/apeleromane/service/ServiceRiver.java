package org.example.apeleromane.service;

import org.example.apeleromane.domain.River;
import org.example.apeleromane.domain.event.ChangeEventType;
import org.example.apeleromane.domain.event.RiverEntityChange;
import org.example.apeleromane.observer.Observable;
import org.example.apeleromane.observer.Observer;
import org.example.apeleromane.repository.RepositoryRiver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRiver implements Observable<RiverEntityChange> {
    private RepositoryRiver repositoryRiver;
    private List<Observer<RiverEntityChange>> observers = new ArrayList<>();
    public ServiceRiver(RepositoryRiver repositoryRiver) {
        this.repositoryRiver = repositoryRiver;
    }

    public Iterable<River> findAll(){
        try {
            return repositoryRiver.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public River findOne(Long id){
        try {
            return repositoryRiver.findOne(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void update(River river){
        River a= repositoryRiver.update(river);
        RiverEntityChange event = new RiverEntityChange(ChangeEventType.UPDATE, a);
        notifyObservers(event);
    }

    @Override
    public void addObserver(Observer<RiverEntityChange> e) {
    observers.add(e);
    }

    @Override
    public void removeObserver(Observer<RiverEntityChange> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(RiverEntityChange t) {
        observers.stream().forEach(x -> x.update(t));
    }
}
