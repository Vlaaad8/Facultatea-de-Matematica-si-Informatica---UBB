package ubb.scs.map.trenuri.repository;


import ubb.scs.map.trenuri.domain.City;
import ubb.scs.map.trenuri.domain.validation.Validation;
import ubb.scs.map.trenuri.domain.validation.ValidationException;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.sql.DriverManager.getConnection;

public class CityDBRepository extends AbstractDBRepository<Long, City> {
    public CityDBRepository(String url, String username, String password, Validation<City> validator) {
        super(url,username,password,validator);
    }
    @Override
    public Optional<City> findOne(Long id) {
        try (Connection connection = getConnection(getUrl(), getUsername(), getPassword());) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"city\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long ID1 = resultSet.getLong("id");
                String name = resultSet.getString("nume");
                City city = new City(name);
                city.setID(ID1);
                return Optional.of(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Iterable<City> findAll() {
        Set<City> cities = new HashSet<>();
        try (Connection connection = getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"city\"");
             ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()) {
                Long ID1 = resultSet.getLong("id");
                String name = resultSet.getString("nume");
                City city = new City(name);
                city.setID(ID1);
                cities.add(city);
            }
            return cities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }
}
