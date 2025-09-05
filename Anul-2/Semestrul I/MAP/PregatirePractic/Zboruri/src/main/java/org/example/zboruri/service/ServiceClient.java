package org.example.zboruri.service;

import org.example.zboruri.domain.Client;
import org.example.zboruri.repository.RepositoryClient;

import java.awt.image.ReplicateScaleFilter;
import java.sql.SQLException;

public class ServiceClient {
    private RepositoryClient repositoryClient;

    public ServiceClient(RepositoryClient repositoryClient) {
        this.repositoryClient = repositoryClient;
    }

    public Iterable<Client> findAll() throws SQLException {
        return repositoryClient.findAll();
    }
    public Client findOne(int id) throws SQLException {
        return repositoryClient.findOne(id);
    }
    public Client findByUsername(String username) throws SQLException {
        for (Client client : repositoryClient.findAll()) {
            if(client.getUsername().equals(username)){
                return client;
            }
        }
        return null;
    }
}
