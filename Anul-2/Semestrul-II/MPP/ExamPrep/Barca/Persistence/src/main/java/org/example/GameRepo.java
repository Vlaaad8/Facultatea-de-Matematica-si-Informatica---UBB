package org.example;

import org.example.GameRepository;
import org.example.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GameRepo implements GameRepository {
    @Override
    public Optional<Game> add(Game entity) {
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Game> delete(Game entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Game> findById(Integer integer) {
        try(Session session=HibernateUtils.getSessionFactory().openSession()){
            Game game=session.get(Game.class, integer);
            return Optional.of(game);
        }
        catch(HibernateException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Iterable<Game> findAll() {
        try(Session session=HibernateUtils.getSessionFactory().openSession()){
            return session.createQuery("from Game").list();
        }
    }

    @Override
    public List<Game> findAllFinished(String name) {
        try(Session session=HibernateUtils.getSessionFactory().openSession()){
            return session.createQuery("from Game where player.name=:nume and points>:param")
                    .setParameter("nume",name)
                    .setParameter("param",-9)
                .list();
        }
    }
}
