package org.example.iss.repository;

import org.example.iss.domain.Drug;
import org.example.iss.domain.Order;
import org.example.iss.domain.OrderItem;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OrderItemRepository {
    public void save(OrderItem orderItem) {
        Transaction transaction=null;
        try(Session session=HibernateUtil.getSessionFactory().openSession()){
            transaction=session.beginTransaction();
            session.persist(orderItem);
            transaction.commit();
        }
        catch(Exception e){
            if(transaction!=null){
                transaction.rollback();
            }
            throw e;
        }
    }
    public List<Drug> getDrugsByOrder(Order order){
        String hql =
                "select oi.drug " +
                        "from OrderItem oi " +
                        "where oi.mainOrder = :mainOrder";

        try ( Session session = HibernateUtil.getSessionFactory().openSession() ) {
            return session.createQuery(hql, Drug.class)
                    .setParameter("mainOrder", order)
                    .getResultList();
        }
    }
}
