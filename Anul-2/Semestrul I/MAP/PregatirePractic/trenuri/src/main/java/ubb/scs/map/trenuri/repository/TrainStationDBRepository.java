package ubb.scs.map.trenuri.repository;

import ubb.scs.map.trenuri.domain.City;
import ubb.scs.map.trenuri.domain.TrainStation;
import ubb.scs.map.trenuri.domain.validation.Validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.sql.DriverManager.getConnection;

public class TrainStationDBRepository extends AbstractDBRepository<Long, TrainStation> {
    public TrainStationDBRepository(String url, String username, String password, Validation<TrainStation> validator) {
        super(url, username, password, validator);
    }
    public Optional<TrainStation> findOne(Long id) {
        try (Connection connection = getConnection(getUrl(), getUsername(), getPassword());) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"trainstation\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long ID1 = resultSet.getLong("id");
                String departureCityId = resultSet.getString("departurecityid");
                String destinationCityId = resultSet.getString("destinationcityid");
                TrainStation station = new TrainStation(departureCityId, destinationCityId);
                station.setID(ID1);
                return Optional.of(station);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Iterable<TrainStation> findAll() {
        Set<TrainStation> stations = new HashSet<>();
        try (Connection connection = getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"trainstation\"");
             ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()) {
                Long ID1 = resultSet.getLong("id");
                String departureCityId = resultSet.getString("departurecityid");
                String destinationCityId = resultSet.getString("destinationcityid");
                TrainStation station = new TrainStation(departureCityId, destinationCityId);
                station.setID(ID1);
                stations.add(station);
            }
            return stations;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }
}