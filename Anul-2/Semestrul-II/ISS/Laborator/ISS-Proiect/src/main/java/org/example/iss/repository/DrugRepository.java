package org.example.iss.repository;


import org.example.iss.domain.Drug;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DrugRepository {
    public Drug save(Drug drug){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(drug);
            transaction.commit();
            return drug;

        }
        catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw e;
        }

    }
    public Drug delete(Drug drug){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.remove(drug);
            transaction.commit();
            return drug;
        }
        catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw e;
        }
    }
    public Drug update(Drug drug){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.merge(drug);
            transaction.commit();
            return drug;
        }
        catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw e;
        }
    }
    public List<Drug> findAll(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.createQuery("from Drug").list();
        }
        catch(Exception e){
            throw e;
        }
    }
}
