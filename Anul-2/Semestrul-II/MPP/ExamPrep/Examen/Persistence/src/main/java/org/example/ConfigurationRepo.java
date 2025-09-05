package org.example;

import org.example.ConfigurationRepository;
import org.example.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ConfigurationRepo implements ConfigurationRepository {
    @Override
    public Optional<Configuration> add(Configuration entity) {
        Transaction transaction=null;
        try(Session session= HibernateUtils.getSessionFactory().openSession()){
            transaction=session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return Optional.of(entity);

        }
        catch(Exception e){
            if (transaction!=null){
                transaction.rollback();
                return Optional.empty();
            }
            else{
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Optional<Configuration> delete(Configuration entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Configuration> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<Configuration> findAll() {
        try(Session session=HibernateUtils.getSessionFactory().openSession()){
            return session.createQuery("from Configuration").list();
        }
    }

    @Override
    public Configuration update(Configuration configuration) {
        Transaction transaction=null;
        try(Session session=HibernateUtils.getSessionFactory().openSession()){
            transaction=session.beginTransaction();
            session.merge(configuration);
            transaction.commit();
            return configuration;
        }
        catch(Exception e){
            if (transaction!=null){
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }
}
