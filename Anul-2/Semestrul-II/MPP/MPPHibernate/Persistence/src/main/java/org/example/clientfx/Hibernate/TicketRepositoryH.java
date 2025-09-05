package org.example.clientfx.Hibernate;

import org.example.clientfx.Ticket;
import org.example.clientfx.TicketRepository;
import org.example.clientfx.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.tree.TreeNode;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class TicketRepositoryH implements TicketRepository {
    @Override
    public Optional<Ticket> add(Ticket entity) {
        Transaction transaction=null;
        try(Session session= HibernateUtils.getSessionFactory().openSession()){
            transaction=session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return Optional.of(entity);
        }
        catch(Exception e){
            if(transaction!=null){
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Ticket> delete(Ticket entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> update(Integer integer, Ticket entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<Ticket> findAll() {
        return null;
    }

    @Override
    public Collection<Ticket> getAll() {
        return List.of();
    }
}
