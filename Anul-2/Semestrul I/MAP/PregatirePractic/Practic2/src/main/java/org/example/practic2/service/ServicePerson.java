package org.example.practic2.service;

import org.example.practic2.domain.Person;
import org.example.practic2.repository.RepositoryPerson;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ServicePerson {
    private RepositoryPerson repositoryPerson;
    public ServicePerson(RepositoryPerson repositoryPerson) {
        this.repositoryPerson = repositoryPerson;
    }
    public Iterable<Person> findAll(){
        try {
            return repositoryPerson.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void Save (String firstname, String lastname,String username,String password,String town,String street,String streetNumber,String telephone){
        Person person=new Person(firstname, lastname, username, password, town, street, streetNumber, telephone);
        repositoryPerson.save(person);
    }

    public Person findPersonByUsername(String username) {
        for (Person person : findAll()) {
            if (person.getUsername().equals(username)) {
                return person;
            }
        }
        return null;
    }

    public Iterable<String> getUsernames(){
        Set<String> usernames=new HashSet<>();
        for (Person person : findAll()) {
            usernames.add(person.getUsername());
        }
        return usernames;
    }
}
