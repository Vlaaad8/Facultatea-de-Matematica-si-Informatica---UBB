package org.example.restaurant.repository;

import org.example.restaurant.domain.Employee;
import org.example.restaurant.domain.Table;

import java.lang.reflect.AnnotatedArrayType;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryEmployee extends AbstractDBRepository<Employee> {
    public RepositoryEmployee(String username, String password,String url) {
        super(username, password,url);
    }

    @Override
    public Iterable<Employee> findAll() throws SQLException {
        Set<Employee> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM angajat");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("nume");
                int age = resultSet.getInt("age");
                Long id = resultSet.getLong("id");
                Employee employee = new Employee(id, name, age);
                tables.add(employee);
            }
        }
        return tables;
    }
    @Override
    public Employee findOne(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"angajat\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id");
                String name= resultSet.getString("name");
                int age= resultSet.getInt("age");
                return new Employee(id1,name,age);
            }
        }
        return null;
    }
}
