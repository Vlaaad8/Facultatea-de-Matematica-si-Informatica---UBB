package org.example.iss.repository;

import org.example.iss.domain.Drug;
import org.example.iss.domain.SpecialOrder;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SpecialOrderRepository {



    public SpecialOrder save(SpecialOrder drug){
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
}
