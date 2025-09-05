package org.example.clientfx.Hibernate;

import jakarta.persistence.TemporalType;
import org.example.clientfx.Flight;
import org.example.clientfx.FlightRepository;
import org.example.clientfx.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.*;


public class FlightRepositoryH implements FlightRepository {
    @Override
    public List<Flight> findByAvailableSeats() {
        return List.of();
    }

    @Override
    public List<Flight> findByDestination(String origin, String departure, Date departureDate) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            java.sql.Date sqlDate = new java.sql.Date(departureDate.getTime());
            return session.createQuery("from Flight  where origin=:origin and departure=:departure and function('DATE', daytime) = :date  and availableSeats>0", Flight.class)
                    .setParameter("origin",origin)
                    .setParameter("departure",departure)
                    .setParameter("date", sqlDate, TemporalType.DATE)
                    .list();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Set<String> getOrigins() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<String> list=session.createQuery( "select distinct f.origin from Flight f",String.class).list();
            return new HashSet<>(list);
        } catch (Exception e) {
            throw e;
        }
    }


        @Override
        public Set<String> getDepartures () {
            try (Session session = HibernateUtils.getSessionFactory().openSession()) {
                List<String> list=session.createQuery( "select distinct f.departure from Flight f",String.class).list();
                return new HashSet<>(list);
            } catch (Exception e) {
                throw e;
            }
        }

        @Override
        public Optional<Flight> add (Flight entity){
            return Optional.empty();
        }

        @Override
        public Optional<Flight> delete (Flight entity){
            return Optional.empty();
        }

        @Override
        public Optional<Flight> update (Integer integer, Flight entity){
            Transaction transaction=null;
            try(Session session = HibernateUtils.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.merge(entity);
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
        public Optional<Flight> findById (Integer integer){
            return Optional.empty();
        }

        @Override
        public Iterable<Flight> findAll () {
            try(Session session = HibernateUtils.getSessionFactory().openSession()) {
                return session.createQuery("from Flight", Flight.class).list();
            }
        }

        @Override
        public Collection<Flight> getAll () {
            return List.of();
        }
    }
