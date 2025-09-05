package org.example.clientfx.Hibernate;

import org.example.clientfx.Employee;
import org.example.clientfx.EmployeeRepository;
import org.example.clientfx.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class EmployeeRepositoryH implements EmployeeRepository {
    @Override
    public Optional<Employee> login(String user, String password) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Employee employee =session.createQuery("from Employee where user = :user and password = :password",Employee.class)
                    .setParameter("user", user)
                    .setParameter("password", password)
                    .getSingleResultOrNull();
            return Optional.of(employee);
        }
        catch(Exception e) {
            throw e;
        }
    }

    @Override
    public Optional<Employee> add(Employee entity) {
        Transaction transaction = null;
        try(Session session= HibernateUtils.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            return Optional.of(entity);
        }
        catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Employee> delete(Employee entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Employee> update(Integer integer, Employee entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Employee> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<Employee> findAll() {
        return null;
    }

    @Override
    public Collection<Employee> getAll() {
        return List.of();
    }
}
