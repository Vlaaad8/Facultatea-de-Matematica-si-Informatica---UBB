package org.example.iss.repository;


import org.example.iss.domain.Role;
import org.example.iss.domain.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserRepository {
    public User save(User user) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
                throw e;
            }
        }

    public User login(User user) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from User where username=:username and password=:password",User.class)
                    .setParameter("username", user.getUsername())
                    .setParameter("password", user.getPassword())
                    .getSingleResultOrNull();
        }
    }
    public User findByID(int id){
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }
}
