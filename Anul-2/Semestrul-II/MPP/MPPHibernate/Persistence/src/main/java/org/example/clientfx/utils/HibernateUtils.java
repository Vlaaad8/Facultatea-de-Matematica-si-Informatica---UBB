package org.example.clientfx.utils;

import org.example.clientfx.Employee;
import org.example.clientfx.Flight;
import org.example.clientfx.Ticket;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if ((sessionFactory==null)||(sessionFactory.isClosed()))
            sessionFactory=createNewSessionFactory();
        return sessionFactory;
    }

    private static  SessionFactory createNewSessionFactory(){
        sessionFactory = new Configuration()
                .addAnnotatedClass(Ticket.class)
                .addAnnotatedClass(Flight.class)
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static  void closeSessionFactory(){
        if (sessionFactory!=null)
            sessionFactory.close();
    }
}
