package org.example.restaurantnou.repository;

import com.sun.source.util.TaskListener;
import javafx.scene.control.Tab;
import org.example.restaurantnou.domain.MenuItem;
import org.example.restaurantnou.domain.Staff;
import org.example.restaurantnou.domain.Table;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class RepositoryTable extends AbstractDBRepository{
    public RepositoryTable(String username, String password, String url) {
        super(username, password, url);
    }
    public Iterable<Table> findAll(){
        Set<Table> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"table\"");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Table table=new Table();
                table.setId(id);
                tables.add(table);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }
}
