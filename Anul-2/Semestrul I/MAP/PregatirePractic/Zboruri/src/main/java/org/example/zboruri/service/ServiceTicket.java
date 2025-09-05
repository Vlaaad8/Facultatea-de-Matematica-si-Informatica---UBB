package org.example.zboruri.service;

import org.example.zboruri.domain.Ticket;
import org.example.zboruri.repository.RepositoryTicket;

import java.time.LocalDateTime;

public class ServiceTicket {
    private RepositoryTicket repositoryTicket;
    public ServiceTicket(RepositoryTicket repositoryTicket) {
        this.repositoryTicket = repositoryTicket;

    }
    public void save(String username, Long flightID, LocalDateTime purchaseTime){
        Ticket ticket=new Ticket(username,flightID,purchaseTime);
        System.out.println(ticket.getUsername());
        repositoryTicket.save(ticket);

    }
}
