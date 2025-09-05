package org.example.practic2.service;

import org.example.practic2.domain.Need;
import org.example.practic2.domain.Person;
import org.example.practic2.domain.event.ChangeEventType;
import org.example.practic2.domain.event.NeedEntityChange;
import org.example.practic2.observer.Observable;
import org.example.practic2.observer.Observer;
import org.example.practic2.repository.RepositoryNeed;
import org.example.practic2.repository.RepositoryPerson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServiceNeed  implements Observable<NeedEntityChange> {
    private RepositoryNeed repositoryNeed;
    private RepositoryPerson repositoryPerson;
    private List<Observer<NeedEntityChange>> observers = new ArrayList<>();
    public ServiceNeed(RepositoryNeed repositoryNeed, RepositoryPerson repositoryPerson) {
        this.repositoryNeed = repositoryNeed;
        this.repositoryPerson = repositoryPerson;
    }

    public Iterable<Need> findAll(){
        try {
            return repositoryNeed.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Iterable<Need> findAllByTown(String town,Long id){
        List<Need> needs = new ArrayList<>();
        for(Need need:findAll()){
            try {
                Person publisher=repositoryPerson.findOne(need.getPersoninneed());
                if(publisher.getTown().equals(town) && !Objects.equals(need.getPersoninneed(), id))
                    needs.add(need);
                } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return needs;

    }
    public void update(Need need){
        Need a=repositoryNeed.update(need);
        NeedEntityChange event = new NeedEntityChange(ChangeEventType.UPDATE, a);
        notifyObservers(event);
    }

    public Iterable<Need> findAllByPerson(Long id){
        List<Need> needs = new ArrayList<>();
        for(Need need:findAll()){
            if(need.getPersontosave().equals(id)){
                needs.add(need);
            }
        }
        return needs;
    }

    public void Save(Need need){
        System.out.println(need.getTitle()+" "+need.getPersoninneed()+" "+need.getPersontosave()+" "+need.getDeadline());
        Need a=repositoryNeed.save(need);
        NeedEntityChange event = new NeedEntityChange(ChangeEventType.ADD, a);
        notifyObservers(event);
    }

    @Override
    public void addObserver(Observer<NeedEntityChange> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<NeedEntityChange> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(NeedEntityChange t) {
        observers.stream().forEach(x -> x.update(t));
    }
}
