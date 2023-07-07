package com.application.dao.jdbc;

import com.application.dao.PublisherDao;
import com.application.exceptions.NotFoundException;
import com.application.model.Author;
import com.application.model.Publisher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.application.util.Constants.*;

public class PublisherDaoJDBC implements PublisherDao {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Publisher get(Long id) {
        String sql = "SELECT * from publisher p where p.id=?";
        Publisher publisher = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long publisher_id = resultSet.getLong("id");
                String publisher_name = resultSet.getString("name");
                String address = resultSet.getString("address");

                publisher = new Publisher(publisher_id, publisher_name, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (publisher == null) {
            throw new NotFoundException("Publisher not found");
        }
        return publisher;
    }

    @Override
    public List<Publisher> getAll() {
        String sql = "SELECT * from publisher p";
        List<Publisher> publishers = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long publisher_id = resultSet.getLong("id");
                String publisher_name = resultSet.getString("name");
                String address = resultSet.getString("address");

                Publisher publisher = new Publisher(publisher_id, publisher_name, address);
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publishers;
    }

    @Override
    public Publisher save(Publisher publisher) {
        String sql = "INSERT INTO publisher (name, address) VALUES( ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, publisher.getName());
            preparedStatement.setString(1, publisher.getAddress());


            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                publisher.setId(generatedKeys.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publisher;
    }

    @Override
    public Publisher update(Publisher publisher) {
        String sql = "UPDATE publisher SET name = ?, address = ? WHERE id =?";
        get(publisher.getId());
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);

            preparedStatement.setString(1, publisher.getName());
            preparedStatement.setString(2, publisher.getAddress());
            preparedStatement.setLong(3, publisher.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publisher;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM publisher WHERE id = ?";
        get(id);
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
