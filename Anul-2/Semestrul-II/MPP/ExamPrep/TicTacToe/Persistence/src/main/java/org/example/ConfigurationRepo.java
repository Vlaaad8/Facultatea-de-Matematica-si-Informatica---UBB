package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ConfigurationRepo implements ConfigurationRepository{
    private static final Logger logger = LogManager.getLogger();
    @Override
    public Optional<Configuration> add(Configuration entity) {
        Transaction transaction=null;
        try(Session session=HibernateUtils.getSessionFactory().openSession()){
            transaction=session.beginTransaction();
            session.merge(entity);
            transaction.commit();
            return Optional.of(entity);

        }
        catch(Exception e){
            logger.error(e.getMessage());
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
}
